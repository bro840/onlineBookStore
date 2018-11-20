package com.readinessit.bookstore.repository;

import com.readinessit.bookstore.domain.Genre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Genre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Genre findByName(String name);
}
