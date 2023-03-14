package com.library.librarymanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.librarymanagement.model.Book;
import com.library.librarymanagement.model.IssueDetails;
import com.library.librarymanagement.model.MessageResponse;
import com.library.librarymanagement.service.BookServiceImpl;
import com.library.librarymanagement.service.IssueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private IssueServiceImpl issueService;

    @GetMapping("/all")
    public ResponseEntity<List<Book>> getBooks(){
        List<Book> books = bookService.getAllBooks();
        if(!CollectionUtils.isEmpty(books)){
            List<String> isbns = books.stream().map(item -> item.getIsbn()).collect(Collectors.toList());
            for(String isbn : isbns){
                List<IssueDetails> issueDetails = issueService.findBorrowedBooksByIsbn(isbn);
                if(!CollectionUtils.isEmpty(issueDetails)){
                    List<Integer> userIds = issueDetails.stream().map(item-> item.getIssuedTo()).collect(Collectors.toList());
                    for(Book book : books){
                        if(book.getIsbn().equals(isbn)){
                            book.setBorrowedBy(userIds);
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable(name = "id") int id){
        Optional<Book> book = bookService.findById(id);
        if(book.isPresent()){
            List<String> isbns = Arrays.asList(book.get().getIsbn());
            for(String isbn : isbns){
                List<IssueDetails> issueDetails = issueService.findBorrowedBooksByIsbn(isbn);
                if(!CollectionUtils.isEmpty(issueDetails)){
                    List<Integer> userIds = issueDetails.stream().map(item-> item.getIssuedTo()).collect(Collectors.toList());
//                    for(Book book : books){
//                        if(book.getIsbn().equals(isbn)){
//                            book.setBorrowedBy(userIds);
//                        }
//                    }
                    book.get().setBorrowedBy(userIds);
                }
            }
        }
        if(book.isPresent()){
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/get/{name}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_STAFF')")
    public ResponseEntity<Book> getBook(@PathVariable("name") String name){
        return new ResponseEntity<>(bookService.getBookByName(name), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<Book> addBook(@RequestBody Book book) throws JsonProcessingException {
        //Book book = new ObjectMapper().readValue(bookString, Book.class);
        return new ResponseEntity<>(bookService.add(book), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<Book> update(@RequestBody Book book, @PathVariable("id") int id){
        Optional<Book> existingBook = bookService.findById(id);
        if(existingBook.isPresent()){
            return new ResponseEntity<>(bookService.update(book, existingBook.get()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<?> deleteBook(@PathVariable("id") int id){
        try{
            bookService.deleteById(id);
            return ResponseEntity.ok().body(new MessageResponse("Book removed successfully!"));
        } catch(Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse("Error while removing book!"));
        }
    }
}
