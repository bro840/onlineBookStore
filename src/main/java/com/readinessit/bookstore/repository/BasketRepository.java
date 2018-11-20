package com.readinessit.bookstore.repository;


import com.readinessit.bookstore.domain.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;



/**
 * Spring Data  repository for the Basket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Modifying
    @Transactional
    @Query(value = "delete from basket where book_id = ?1", nativeQuery = true)
    void deleteByBookId(long bookId);
}
