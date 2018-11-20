package com.readinessit.bookstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.readinessit.bookstore.domain.Author;
import com.readinessit.bookstore.domain.Genre;
import com.readinessit.bookstore.repository.GenreRepository;
import com.readinessit.bookstore.service.GenreService;
import com.readinessit.bookstore.service.UserService;
import com.readinessit.bookstore.web.rest.errors.BadRequestAlertException;
import com.readinessit.bookstore.web.rest.errors.ConflictResourceStateAlertException;
import com.readinessit.bookstore.web.rest.errors.ForbiddenActionAlertException;
import com.readinessit.bookstore.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Genre.
 */
@RestController
@RequestMapping("/api")
public class GenreResource {

    private final Logger log = LoggerFactory.getLogger(GenreResource.class);

    private static final String ENTITY_NAME = "genre";

    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;
    @Autowired
    private UserService userService;

    public GenreResource(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * POST  /genres : Create a new genre.
     *
     * @param genre the genre to create
     * @return the ResponseEntity with status 201 (Created) and with body the new genre, or with status 400 (Bad Request) if the genre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/genres")
    @Timed
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) throws URISyntaxException {

        log.debug("REST request to save Genre : {}", genre);

        // validates if logged user has the required admin privilege to this action
        if(!userService.isAdmin()) {
            throw new ForbiddenActionAlertException("Forbidden action", ENTITY_NAME, "You are not allowed to create new authors");
        }

        if (genre.getId() != null) {
            throw new BadRequestAlertException("A new genre cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if(!genreService.isGenreValid(genre)) {
            throw new BadRequestAlertException(genreService.getGenreErrors(genre), ENTITY_NAME, genreService.getGenreErrors(genre));
        }

        Genre result = genreRepository.save(genre);
        return ResponseEntity.created(new URI("/api/genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /genres : Updates an existing genre.
     *
     * @param genre the genre to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated genre,
     * or with status 400 (Bad Request) if the genre is not valid,
     * or with status 500 (Internal Server Error) if the genre couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/genres")
    @Timed
    public ResponseEntity<Genre> updateGenre(@RequestBody Genre genre) throws URISyntaxException {

        log.debug("REST request to update Genre : {}", genre);


        // validates if logged user has the required admin privilege to this action
        if(!userService.isAdmin()) {
            throw new ForbiddenActionAlertException("Forbidden action", ENTITY_NAME, "You are not allowed to update genres");
        }


        if (genre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }


        if(!genreService.isGenreValidWithId(genre)) {
            throw new BadRequestAlertException(genreService.getGenreErrorsWithId(genre)  , ENTITY_NAME, genreService.getGenreErrorsWithId(genre));
        }


        Genre result = genreRepository.save(genre);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, genre.getId().toString()))
            .body(result);
    }

    /**
     * GET  /genres : get all the genres.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of genres in body
     */
    @GetMapping("/genres")
    @Timed
    public List<Genre> getAllGenres() {
        log.debug("REST request to get all Genres");
        return genreRepository.findAll();
    }

    /**
     * GET  /genres/:id : get the "id" genre.
     *
     * @param id the id of the genre to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the genre, or with status 404 (Not Found)
     */
    @GetMapping("/genres/{id}")
    @Timed
    public ResponseEntity<Genre> getGenre(@PathVariable Long id) {
        log.debug("REST request to get Genre : {}", id);
        Optional<Genre> genre = genreRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(genre);
    }

    /**
     * DELETE  /genres/:id : delete the "id" genre.
     *
     * @param id the id of the genre to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/genres/{id}")
    @Timed
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.debug("REST request to delete Genre : {}", id);


        // validates if logged user has the required admin privilege to this action
        if(!userService.isAdmin()) {
            throw new ForbiddenActionAlertException("Forbidden action", ENTITY_NAME, "You are not allowed to delete genres");
        }


        // validates if the author to be deleted is a dependencie for any related table
        Genre genre = genreRepository.findById(id).get();
        if(!genreService.isGenreDeletable(genre)) {
            throw new ConflictResourceStateAlertException(genreService.getGenreDeletableErrors(genre), ENTITY_NAME, genreService.getGenreDeletableErrors(genre));
        }


        genreRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
