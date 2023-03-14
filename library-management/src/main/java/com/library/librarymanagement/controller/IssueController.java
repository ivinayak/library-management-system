package com.library.librarymanagement.controller;

import com.library.librarymanagement.model.Book;
import com.library.librarymanagement.model.MessageResponse;
import com.library.librarymanagement.service.BookServiceImpl;
import com.library.librarymanagement.service.IssueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class IssueController {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private IssueServiceImpl issueService;

    @PostMapping("/issue/{isbn}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> issueNewBook(@PathVariable(name = "isbn") String isbn,  @RequestHeader(value="user") String user,
                                          @RequestHeader(value="Authorization") String header ){
        int qty = bookService.findQuantity(isbn);
        if(qty > 0){
            issueService.issueBook(isbn, user, header);
            return new ResponseEntity<>(new MessageResponse("Book issued successfully"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Error while issuing book!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/renew/{isbn}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> renewBook(@PathVariable(name = "isbn") String isbn,  @RequestHeader(value="user") String user,
                                       @RequestHeader(value="Authorization") String header){
        try{
            issueService.renewBook(isbn, user, header);
            return new ResponseEntity<>(new MessageResponse("Book renewed successfully"), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(new MessageResponse("Error while renewing book!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/return/{isbn}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> returnBook(@PathVariable(name = "isbn") String isbn,  @RequestHeader(value="user") String user,
                                        @RequestHeader(value="Authorization") String header){
        try{
            issueService.returnBook(isbn, user, header);
            return new ResponseEntity<>(new MessageResponse("Book returned successfully"), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(new MessageResponse("Error while returning book!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/borrowed-books/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Book>> getBorrowedBooks(@PathVariable("id") int id){
        Optional<List<Book>> books = issueService.findBorrowedBooksByUserId(id);
        if(books.isPresent()){
            return new ResponseEntity<>(books.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
