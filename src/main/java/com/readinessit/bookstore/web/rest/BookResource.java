package com.readinessit.bookstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.readinessit.bookstore.domain.Author;
import com.readinessit.bookstore.domain.Book;
import com.readinessit.bookstore.domain.Genre;
import com.readinessit.bookstore.repository.AuthorRepository;
import com.readinessit.bookstore.repository.BookRepository;
import com.readinessit.bookstore.repository.GenreRepository;
import com.readinessit.bookstore.repository.SaleDetailsRepository;
import com.readinessit.bookstore.service.BookService;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Book.
 */
@RestController
@RequestMapping("/api")
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    private static final String ENTITY_NAME = "book";

    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private SaleDetailsRepository saleDetailsRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;



    /**
     * GET  /books : get all the books.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of books in body
     */
    @GetMapping("/books")
    @Timed
    public List<Book> getAllBooks( @RequestParam(required = false, defaultValue = "false") boolean eagerload,
                                   @RequestParam(name = "title", required = false, defaultValue = "") String title) {

        log.debug("REST request to get all Books");


        if(title != null && !title.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCase(title);
        }
        return bookRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /books/:id : get the "id" book.
     *
     * @param id the id of the book to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the book, or with status 404 (Not Found)
     */
    @GetMapping("/books/{id}")
    @Timed
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        Optional<Book> book = bookRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(book);
    }




    public BookResource(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * POST  /books : Create a new book.
     *
     * @param book the book to create
     * @return the ResponseEntity with status 201 (Created) and with body the new book, or with status 400 (Bad Request) if the book has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/books")
    @Timed
    public ResponseEntity<Object> createBook(@RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to save Book : {}", book);

        // validates if logged user has the required admin privilege to this action
        if(!userService.isAdmin()) {
            throw new ForbiddenActionAlertException("Forbidden action", ENTITY_NAME, "You are not allowed to create new books");
        }

        if (book.getId() != null) {
            throw new BadRequestAlertException("A new book cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // validates if book fields respects business logic
        if(!bookService.isBookValid(book)) {
            throw new BadRequestAlertException(bookService.getBookErrors(book) , ENTITY_NAME, bookService.getBookErrors(book));
        }


        // saves
        Book result = bookRepository.save(book);

        // gets authors
        Set<Author> auths = new HashSet<>();
        for(Author author : result.getAuthors()) {
            auths.add(authorRepository.findById(author.getId()).get());
        }
        result.setAuthors(auths);

        // gets genres
        Set<Genre> genres = new HashSet<>();
        for(Genre genre : result.getGenres()) {
            genres.add(genreRepository.findById(genre.getId()).get());
        }
        result.setGenres(genres);

        return ResponseEntity.created(new URI("/api/books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /books : Updates an existing book.
     *
     * @param book the book to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated book,
     * or with status 400 (Bad Request) if the book is not valid,
     * or with status 500 (Internal Server Error) if the book couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/books")
    @Timed
    public ResponseEntity<Book> updateBook(@RequestBody Book book) throws URISyntaxException {

        log.debug("REST request to update Book : {}", book);

        // validates if logged user has the required admin privilege to this action
        if(!userService.isAdmin()) {
            throw new ForbiddenActionAlertException("Forbidden action", ENTITY_NAME, "You are not allowed to update books");
        }

        if (book.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // validates if book fields respects business logic
        if(!bookService.isBookValidWithId(book)) {
            throw new BadRequestAlertException(bookService.getBookErrorsWithId(book), ENTITY_NAME, bookService.getBookErrorsWithId(book));
        }
        // validates if book and its dependencies fields ids exists in the db
        if(!bookService.isBookUpdatable(book)) {
            throw new BadRequestAlertException(bookService.getBookUpdatableErrors(book), ENTITY_NAME, bookService.getBookUpdatableErrors(book));
        }

        Book result = bookRepository.save(book);
        // gets all sale details for the updated book
        result.setSaleDetails(saleDetailsRepository.findByBookId(book.getId()));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, book.getId().toString()))
            .body(result);
    }

    /**
     * DELETE  /books/:id : delete the "id" book.
     *
     * @param id the id of the book to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/books/{id}")
    @Timed
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete Book : {}", id);

        // validates if logged user has the required admin privilege to this action
        if(!userService.isAdmin()) {
            throw new ForbiddenActionAlertException("Forbidden action", ENTITY_NAME, "You are not allowed to delete books");
        }

        // validates if the book to be deleted is a dependencie on sale_details table
        Book book = bookRepository.findById(id).get();
        if(!bookService.isBookDeletable(book)) {
            throw new ConflictResourceStateAlertException(bookService.getBookDeletableErrors(book), ENTITY_NAME, bookService.getBookDeletableErrors(book));
        }

        // delete all book_author, book_genre and basket entries.
        bookService.deleteBasket(book);
        bookService.deleteBookAuthors(book);
        bookService.deleteBookGenres(book);


        bookRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
