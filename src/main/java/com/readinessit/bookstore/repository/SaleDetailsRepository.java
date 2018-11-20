package com.readinessit.bookstore.repository;

import com.readinessit.bookstore.domain.SaleDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;


/**
 * Spring Data  repository for the SaleDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleDetailsRepository extends JpaRepository<SaleDetails, Long> {

    Set<SaleDetails> findByBookId(long bookId);

    Set<SaleDetails> findBySaleId(long saleId);
}
