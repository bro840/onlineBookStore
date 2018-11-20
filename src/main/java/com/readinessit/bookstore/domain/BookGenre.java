package com.readinessit.bookstore.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "book_genre")
public class BookGenre {

    @EmbeddedId
    private BookGenreId bookGenreId;


    public BookGenre() {}

    public BookGenreId getBookGenreId() {
        return bookGenreId;
    }
    public void setBookGenreId(BookGenreId bookGenreId) {
        this.bookGenreId = bookGenreId;
    }
}



