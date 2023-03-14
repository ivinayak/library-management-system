package com.library.librarymanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Entity(name = "books")
@Getter
@Setter
@JsonIgnoreProperties (ignoreUnknown = true)
public class Book {

    @Id
    @GeneratedValue
    private int bookId;

    private String name;

    private String isbn;

    private byte[] image;

    private Long quantity;

    private String author;

    private String publication;

    private String subject;

    private String category;

    @Transient
    private List<Integer> borrowedBy;

}
