package com.readinessit.bookstore.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class BookGenreId implements Serializable {


    @Column(name = "books_id")
    private long bookId;
    @Column(name = "genres_id")
    private long genreId;

    public  BookGenreId () {}
    public BookGenreId(long bookId, long genreId) {
        this.bookId = bookId;
        this.genreId = genreId;
    }

    public long getBookId() {
        return bookId;
    }
    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getGenreId() {
        return genreId;
    }
    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookGenreId)) return false;
        BookGenreId that = (BookGenreId) o;
        return getBookId() == that.getBookId() &&
            getGenreId() == that.getGenreId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookId(), getGenreId());
    }


    @Override
    public String toString() {
        return "BookGenreId{" +
            "bookId=" + bookId +
            ", genreId=" + genreId +
            '}';
    }
}
