package com.library.librarymanagement.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity(name = "issue_details")
@Getter
@Setter
public class IssueDetails {

    @Id
    @GeneratedValue
    private int issueId;

    private String isbn;

    private int issuedTo;

    private LocalDate issueDateFrom;

    private LocalDate issueDateTo;

    private String status;

    private LocalDate updatedAt;

    private LocalDate createdAt;
}
