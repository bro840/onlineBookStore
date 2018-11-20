package com.readinessit.bookstore.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private long book_id;
    private long user_id;


    public Basket() {}
    public Basket(long book_id, long user_id) {
        this.book_id = book_id;
        this.user_id = user_id;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getBook_id() {
        return book_id;
    }
    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }
    public long getUser_id() {
        return user_id;
    }
    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Basket)) return false;
        Basket basket = (Basket) o;
        return getId() == basket.getId() &&
            getBook_id() == basket.getBook_id() &&
            getUser_id() == basket.getUser_id();
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBook_id(), getUser_id());
    }
}
