package com.library.librarymanagement.service;

import com.library.librarymanagement.model.Book;
import com.library.librarymanagement.model.IssueDetails;
import com.library.librarymanagement.repository.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl {

    @Autowired
    private IBookRepository bookRepository;

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book add(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    public Book update(Book book, Book existingBook) {
        if(null != book.getName()){
            existingBook.setName(book.getName());
        }
        if(null != book.getIsbn()){
            existingBook.setIsbn(book.getIsbn());
        }
        if(null != book.getImage()){
            existingBook.setImage(book.getImage());
        }
        if(null != book.getPublication()){
            existingBook.setPublication(book.getPublication());
        }
        if(null != book.getQuantity()){
            existingBook.setQuantity(book.getQuantity());
        }
        if(null != book.getAuthor()){
            existingBook.setAuthor(book.getAuthor());
        }
        if(null != book.getSubject()){
            existingBook.setSubject(book.getSubject());
        }
        if(null != book.getCategory()){
            existingBook.setCategory(book.getCategory());
        }

        return bookRepository.save(existingBook);
    }

    public void deleteById(int id) throws Exception{
        bookRepository.deleteById(id);
    }

    public Book getBookByName(String name) {
        return bookRepository.findByName(name);
    }

    public List<Book> findBySubject(String subject){
        return bookRepository.findBySubject(subject);
    }

    public int findQuantity(String isbn){
        Book book = bookRepository.findQuantityByIsbn(isbn);
        return book.getQuantity().intValue();
    }

    public List<Book> findByAuthor(String author){
        return bookRepository.findByAuthor(author);
    }

    public Book findByIsbn(String isbn){
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> findByCategory(String category){
        return bookRepository.findByCategory(category);
    }


    public Optional<List<Book>> findByIsbnIn(List<String> bookIds) {
        return bookRepository.findByIsbnIn(bookIds);
    }
}
