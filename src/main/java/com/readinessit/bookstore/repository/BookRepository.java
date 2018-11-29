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

    @Query(value =  "select * from book " +
                    "inner join book_author on book.id = book_author.books_id " +
                    "inner join author on book_author.authors_id = author.id " +
                    "where LOWER(author.name) like %?1%", nativeQuery = true)
    List<Book> findByAuthorNameContainingIgnoreCase(String authorName);



    @Query(value =  "select * from book " +
        "inner join book_author on book.id = book_author.books_id " +
        "inner join author on book_author.authors_id = author.id " +
        "where LOWER(title) like %?1% " +
        "and   LOWER(author.name) like %?2%", nativeQuery = true)
    List<Book> findByTitleAndByAuthorNameContainingIgnoreCase(String title, String author);



    boolean existsByIsbn(String isnb);
}
