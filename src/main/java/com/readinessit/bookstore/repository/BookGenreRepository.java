package com.readinessit.bookstore.repository;

import com.readinessit.bookstore.domain.BookAuthor;
import com.readinessit.bookstore.domain.BookAuthorId;
import com.readinessit.bookstore.domain.BookGenre;
import com.readinessit.bookstore.domain.BookGenreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Book entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, BookGenreId> {

    @Query(value = "select * from book_genre where genres_id = ?1", nativeQuery = true)
    List<BookGenre> getByGenreId(long genreId);

}
