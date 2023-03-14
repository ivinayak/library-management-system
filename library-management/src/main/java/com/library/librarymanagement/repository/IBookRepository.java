package com.library.librarymanagement.repository;


import com.library.librarymanagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookRepository extends JpaRepository<Book, Integer> {

    Book findByName(String name);

    List<Book> findBySubject(String subject);

    List<Book> findByAuthor(String author);

    Book findByIsbn(String isbn);

    List<Book> findByCategory(String category);

    Book findQuantityByIsbn(String isbn);

    Optional<List<Book>> findByIsbnIn(List<String> bookIds);

}
