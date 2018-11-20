package com.readinessit.bookstore.repository;

import com.readinessit.bookstore.domain.Sale;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Sale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("select sale from Sale sale where sale.user.login = ?#{principal.username}")
    List<Sale> findByUserIsCurrentUser();


    List<Sale> findByUserId(long userId);

}
