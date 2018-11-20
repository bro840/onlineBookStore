package com.readinessit.bookstore.domain;

import javax.persistence.*;


@Entity
@Table(name = "book_author")
public class BookAuthor {

    @EmbeddedId
    private BookAuthorId bookAuthorId;


    public BookAuthor() {}


    public BookAuthorId getBookAuthorId() {
        return bookAuthorId;
    }
    public void setBookAuthorId(BookAuthorId bookAuthorId) {
        this.bookAuthorId = bookAuthorId;
    }
}



