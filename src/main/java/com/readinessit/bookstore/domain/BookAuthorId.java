package com.readinessit.bookstore.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class BookAuthorId implements Serializable {


    @Column(name = "books_id")
    private long bookId;
    @Column(name = "authors_id")
    private long authorId;

    public BookAuthorId() {}

    public BookAuthorId(long bookId, long authorId) {
        this.bookId = bookId;
        this.authorId = authorId;
    }


    public long getBookId() {
        return bookId;
    }
    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookAuthorId)) return false;
        BookAuthorId that = (BookAuthorId) o;
        return getBookId() == that.getBookId() &&
            getAuthorId() == that.getAuthorId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookId(), getAuthorId());
    }
}
