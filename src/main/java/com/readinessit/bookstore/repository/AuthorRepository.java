package com.readinessit.bookstore.repository;

import com.readinessit.bookstore.domain.Author;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Author entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByNameContainingIgnoreCase(String name);

    Author findByName(String name);
}
