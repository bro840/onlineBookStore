package com.readinessit.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sale.
 */
@Entity
@Table(name = "sale")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class Sale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name="truncate")
    private boolean truncate;

    @OneToMany(mappedBy = "sale", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    @JsonIgnoreProperties("sale")
    private Set<SaleDetails> saleDetails = new HashSet<>();

    @ManyToOne
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Sale date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<SaleDetails> getSaleDetails() {
        return saleDetails;
    }

    public Sale saleDetails(Set<SaleDetails> saleDetails) {
        this.saleDetails = saleDetails;
        return this;
    }

    public Sale addSaleDetails(SaleDetails saleDetails) {
        this.saleDetails.add(saleDetails);
        saleDetails.setSale(this);
        return this;
    }

    public Sale removeSaleDetails(SaleDetails saleDetails) {
        this.saleDetails.remove(saleDetails);
        saleDetails.setSale(null);
        return this;
    }

    public void setSaleDetails(Set<SaleDetails> saleDetails) {
        this.saleDetails = saleDetails;
    }

    public User getUser() {
        return user;
    }

    public Sale user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public boolean isTruncate() {
        return truncate;
    }

    public void setTruncate(boolean truncate) {
        this.truncate = truncate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sale sale = (Sale) o;
        if (sale.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sale.getId());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sale{" +
            "id=" + id +
            ", date=" + date +
            ", truncate=" + truncate +
            ", saleDetails=" + saleDetails +
            ", user=" + user +
            '}';
    }
}
