package com.readinessit.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.readinessit.bookstore.repository.BookRepository;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A SaleDetails.
 */
@Entity
@Table(name = "sale_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SaleDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne()
    @JsonIgnoreProperties("saleDetails")
    private Sale sale;

    @ManyToOne()
    @JsonIgnoreProperties("saleDetails")
    private Book book;



    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public SaleDetails quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Sale getSale() {
        return sale;
    }
    public SaleDetails sale(Sale sale) {
        this.sale = sale;
        return this;
    }
    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Book getBook() {
        return book;
    }
    public SaleDetails book(Book book) {
        this.book = book;
        return this;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SaleDetails saleDetails = (SaleDetails) o;
        if (saleDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saleDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaleDetails{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
