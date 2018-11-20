package com.readinessit.bookstore.repository;

import com.readinessit.bookstore.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Book entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "select distinct book from Book book left join fetch book.authors left join fetch book.genres",
        countQuery = "select count(distinct book) from Book book")
    Page<Book> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct book from Book book left join fetch book.authors left join fetch book.genres")
    List<Book> findAllWithEagerRelationships();

    @Query("select book from Book book left join fetch book.authors left join fetch book.genres where book.id =:id")
    Optional<Book> findOneWithEagerRelationships(@Param("id") Long id);


    List<Book> findByTitleContainingIgnoreCase(String title);

    boolean existsByIsbn(String isnb);

}
