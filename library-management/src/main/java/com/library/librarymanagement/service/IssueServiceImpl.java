package com.library.librarymanagement.service;

import com.library.librarymanagement.model.Book;
import com.library.librarymanagement.model.IssueDetails;
import com.library.librarymanagement.model.User;
import com.library.librarymanagement.repository.IIssueDetailsRepository;
import com.library.librarymanagement.util.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IssueServiceImpl {

    @Autowired
    private IIssueDetailsRepository issueDetailsRepository;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private RestTemplateUtil restTemplateUtil;


    public Optional<List<Book>> findBorrowedBooksByUserId(int id) {
        List<IssueDetails> issueDetails = issueDetailsRepository.findByIssuedToAndStatusNot(id, "Returned");
        Optional<List<Book>> books = null;
        if(!CollectionUtils.isEmpty(issueDetails)){
            List<String> bookIds = issueDetails.stream().map(item -> item.getIsbn()).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(bookIds)){
                books = bookService.findByIsbnIn(bookIds);
            }
        }
        return books;
    }

    public void issueBook(String isbn, String email, String header) {
        IssueDetails issueDetails = new IssueDetails();
        User user = restTemplateUtil.getUser(email, header);

        if(user != null){
            issueDetails.setIssuedTo(user.getUserId());
            issueDetails.setIssueDateFrom(LocalDate.now());
            issueDetails.setCreatedAt(LocalDate.now());
            issueDetails.setUpdatedAt(LocalDate.now());
            issueDetails.setIssueDateTo(LocalDate.now().plusDays(7));
            issueDetails.setIsbn(isbn);
            issueDetails.setStatus("Issued");

            Book book = bookService.findByIsbn(isbn);
            if(book != null){
                book.setQuantity(book.getQuantity() - 1);
                bookService.add(book);
                issueDetailsRepository.save(issueDetails);
            }

        }
    }

    public void returnBook(String isbn, String email, String header) {

        User user = restTemplateUtil.getUser(email, header);

        if(user != null){
            IssueDetails issueDetails = issueDetailsRepository.findByIsbnAndIssuedToAndStatusNot(isbn, user.getUserId(), "Returned");
            issueDetails.setUpdatedAt(LocalDate.now());
            issueDetails.setStatus("Returned");

            Book book = bookService.findByIsbn(isbn);
            if(book != null){
                book.setQuantity(book.getQuantity() + 1);
                bookService.add(book);
                issueDetailsRepository.save(issueDetails);
            }

        }
    }

    public void renewBook(String isbn, String email, String header) {
        User user = restTemplateUtil.getUser(email, header);

        if(user != null){
            IssueDetails issueDetails = issueDetailsRepository.findByIsbnAndIssuedToAndStatusNot(isbn, user.getUserId(), "Returned");
            issueDetails.setUpdatedAt(LocalDate.now());
            issueDetails.setIssueDateTo(LocalDate.now().plusDays(7));
            issueDetails.setStatus("Renewed");
            issueDetailsRepository.save(issueDetails);
        }
    }


    public List<IssueDetails> findBorrowedBooksByIsbn(String isbn) {
        return issueDetailsRepository.findByIsbnAndStatusNot(isbn, "Returned");
    }
}
