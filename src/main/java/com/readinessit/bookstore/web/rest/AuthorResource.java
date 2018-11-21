package com.readinessit.bookstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.readinessit.bookstore.domain.Author;
import com.readinessit.bookstore.domain.BookAuthor;
import com.readinessit.bookstore.repository.AuthorRepository;
import com.readinessit.bookstore.repository.BookAuthorRepository;
import com.readinessit.bookstore.service.AuthorService;
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
 * REST controller for managing Author.
 */
@RestController
@RequestMapping("/api")
public class AuthorResource {

    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    private static final String ENTITY_NAME = "author";

    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;
    @Autowired
    private UserService userService;


    public AuthorResource(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * POST  /authors : Create a new author.
     *
     * @param author the author to create
     * @return the ResponseEntity with status 201 (Created) and with body the new author, or with status 400 (Bad Request) if the author has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/authors")
    @Timed
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) throws URISyntaxException {

        log.debug("REST request to save Author : {}", author);

        // validates if logged user has the required admin privilege to this action
        if(!userService.isAdmin()) {
            throw new ForbiddenActionAlertException("Unauthorized action", ENTITY_NAME, "You are not allowed to create new authors");
        }

        if (author.getId() != null) {
            throw new BadRequestAlertException("A new author cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // validates if author fields respects business logic
        if(!authorService.isAuthorValid(author)) {
            throw new BadRequestAlertException(authorService.getAuthorErrors(author) , ENTITY_NAME, authorService.getAuthorErrors(author) );
        }

        Author result = authorRepository.save(author);
        return ResponseEntity.created(new URI("/api/authors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /authors : Updates an existing author.
     *
     * @param author the author to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated author,
     * or with status 400 (Bad Request) if the author is not valid,
     * or with status 500 (Internal Server Error) if the author couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/authors")
    @Timed
    public ResponseEntity<Author> updateAuthor(@RequestBody Author author) throws URISyntaxException {

        log.debug("REST request to update Author : {}", author);


        // validates if logged user has the required admin privilege to this action
        if(!userService.isAdmin()) {
            throw new ForbiddenActionAlertException("Unauthorized action", ENTITY_NAME, "You are not allowed to update authors");
        }


        if (author.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // validate if id exists in the database
        if(!authorRepository.findById(author.getId()).isPresent()) {
            throw new BadRequestAlertException("Author id does not match any database Author id" , ENTITY_NAME, "Author id does not match any database Author id");
        }

        // validates if author fields respects business logic
        if(!authorService.isAuthorValidWithId(author)) {
            throw new BadRequestAlertException(authorService.getAuthorErrorsWithId(author)  , ENTITY_NAME, authorService.getAuthorErrors(author));
        }


        Author result = authorRepository.save(author);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, author.getId().toString()))
            .body(result);
    }

    /**
     * GET  /authors : get all the authors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of authors in body
     */
    @GetMapping("/authors")
    @Timed
    public List<Author> getAllAuthors(@RequestParam(name = "name", required = false, defaultValue = "") String name) {

        log.debug("REST request to get all Authors");


        if(name != null && !name.isEmpty()) {
            return authorService.getByAuthorName(name);
        }

        return authorRepository.findAll();
    }

    /**
     * GET  /authors/:id : get the "id" author.
     *
     * @param id the id of the author to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the author, or with status 404 (Not Found)
     */
    @GetMapping("/authors/{id}")
    @Timed
    public ResponseEntity<Author> getAuthor(@PathVariable Long id) {
        log.debug("REST request to get Author : {}", id);
        Optional<Author> author = authorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(author);
    }

    /**
     * DELETE  /authors/:id : delete the "id" author.
     *
     * @param id the id of the author to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/authors/{id}")
    @Timed
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        log.debug("REST request to delete Author : {}", id);


        // validates if logged user has the required admin privilege to this action
        if(!userService.isAdmin()) {
            throw new ForbiddenActionAlertException("Forbidden action", ENTITY_NAME, "You are not allowed to delete authors");
        }


        // validates if the author to be deleted is a dependencie for any related table
        Author author = authorRepository.findById(id).get();
        if(!authorService.isAuthorDeletable(author)) {
            throw new ConflictResourceStateAlertException(authorService.getAuthorDeletableErrors(author), ENTITY_NAME, authorService.getAuthorDeletableErrors(author));
        }


        authorRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
