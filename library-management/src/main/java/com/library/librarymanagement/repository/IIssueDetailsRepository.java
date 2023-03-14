package com.library.librarymanagement.repository;

import com.library.librarymanagement.model.IssueDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IIssueDetailsRepository extends JpaRepository<IssueDetails, Integer> {
    IssueDetails findByIsbnAndIssuedToAndStatusNot(String isbn, Integer userId, String status);

    List<IssueDetails> findByIssuedToAndStatusNot(int id, String status);

    List<IssueDetails> findByIsbnAndStatusNot(String isbn, String status);

}
