package com.readinessit.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Book.
 */
@Entity
@Table(name = "book")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;


    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    @JsonIgnoreProperties("book")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<SaleDetails> saleDetails = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "book_author",
               joinColumns = @JoinColumn(name = "books_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "authors_id", referencedColumnName = "id"))
    @JsonIgnoreProperties("books")
    private Set<Author> authors = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "book_genre",
               joinColumns = @JoinColumn(name = "books_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "genres_id", referencedColumnName = "id"))
    private Set<Genre> genres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public Book isbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Book quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }
    public Book price(Double price) {
        this.price = price;
        return this;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<SaleDetails> getSaleDetails() {
        return saleDetails;
    }

    public Book saleDetails(Set<SaleDetails> saleDetails) {
        this.saleDetails = saleDetails;
        return this;
    }

    public Book addSaleDetails(SaleDetails saleDetails) {
        this.saleDetails.add(saleDetails);
        saleDetails.setBook(this);
        return this;
    }

    public Book removeSaleDetails(SaleDetails saleDetails) {
        this.saleDetails.remove(saleDetails);
        saleDetails.setBook(null);
        return this;
    }

    public void setSaleDetails(Set<SaleDetails> saleDetails) {
        this.saleDetails = saleDetails;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public Book authors(Set<Author> authors) {
        this.authors = authors;
        return this;
    }

    public Book addAuthor(Author author) {
        this.authors.add(author);
        return this;
    }

    public Book removeAuthor(Author author) {
        this.authors.remove(author);
        return this;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public Book genres(Set<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public Book addGenre(Genre genre) {
        this.genres.add(genre);
        return this;
    }

    public Book removeGenre(Genre genre) {
        this.genres.remove(genre);
        return this;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
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
        Book book = (Book) o;
        if (book.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), book.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", isbn='" + getIsbn() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            "}";
    }
}
