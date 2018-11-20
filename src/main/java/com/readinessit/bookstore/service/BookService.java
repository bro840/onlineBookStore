package com.readinessit.bookstore.service;

import com.readinessit.bookstore.domain.*;
import com.readinessit.bookstore.repository.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Component
public class BookService {

    @Autowired
    private GenreService genreService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookAuthorRepository bookAuthorRepository;
    @Autowired
    private BookGenreRepository bookGenreRepository;




    // validate ISBN
    private boolean validateIsbn( String isbn )
    {
        if ( isbn == null ) {
            return false;
        }

        //remove any hyphens
        isbn = isbn.replaceAll( "-", "" );

        //must be a 13 digit ISBN
        if ( isbn.length() != 13 ) {
            return false;
        }

        try
        {
            int tot = 0;
            for ( int i = 0; i < 12; i++ ) {
                int digit = Integer.parseInt( isbn.substring( i, i + 1 ) );
                tot += (i % 2 == 0) ? digit * 1 : digit * 3;
            }

            //checksum must be 0-9. If calculated as 10 then = 0
            int checksum = 10 - (tot % 10);
            if ( checksum == 10 ) {
                checksum = 0;
            }

            return checksum == Integer.parseInt( isbn.substring( 12 ) );
        }
        catch ( NumberFormatException nfe ) {
            //to catch invalid ISBNs that have non-numeric characters in them
            return false;
        }
    }




    /*********************************************************************************************************************
     * book fields validations
     *********************************************************************************************************************/
    // id
    public boolean isIdValid(Book book) {

        if(book.getId() == null) {
            return false;
        }
        else if(book.getId() <= 0) {
            return false;
        }

        return true;
    }
    // title
    public boolean isTitleValid(Book book) {

        if(book.getTitle() == null) {
           return false;
        }
        else if(book.getTitle().isEmpty()) {
            return false;
        }
        else if (book.getTitle().trim().length() == 0) {
            return false;
        }
        else if(book.getTitle().length() > 50) {
            return false;
        }

        return true;
    }
    // isbn
    public boolean isIsbnValid(Book book) {

        if(book.getIsbn() == null) {
            return false;
        }
        else if(book.getIsbn().isEmpty()) {
            return false;
        }
        else if(!validateIsbn(book.getIsbn())) {
            return false;
        }

        return true;
    }
    // price
    public boolean isPriceValid(Book book) {

        if(book.getPrice() == null) {
            return false;
        }
        else if(book.getPrice().isNaN()) {
            return false;
        }
        else if(book.getPrice() <= 0) {
            return false;
        }

        return true;
    }
    // quantity
    public boolean isQuantityValid(Book book) {

        if(book.getQuantity() == null) {
            return false;
        }
        else if(book.getQuantity() <= 0) {
            return false;
        }

        return true;
    }
    // authors
    public boolean isAuthorsValid(Book book) {

        if(book.getAuthors() == null) {
            return false;
        }
        else if(book.getAuthors().isEmpty()) {
            return false;
        }
        else {

            Iterator<Author> authors = book.getAuthors().iterator();
            while (authors.hasNext()) {
                if(!authorService.isIdValid(authors.next())) {
                    return false;
                }
            }
        }

        return true;
    }
    // genres
    public boolean isGenresValid(Book book) {

        if(book.getGenres() == null) {
            return false;
        }
        else if(book.getGenres().isEmpty()) {
            return false;
        }
        else {

            Iterator<Genre> genres = book.getGenres().iterator();
            while (genres.hasNext()) {
                if(!genreService.isIdValid(genres.next())) {
                    return false;
                }
            }
        }

        return true;
    }
    /*********************************************************************************************************************
     * book field errors
     *********************************************************************************************************************/
    // id
    public String getIdError(Book book) {

        if(book.getId() == null) {
            return "Book id cant be null";
        }
        else if(book.getId() <= 0) {
            return "Book id must be bigger than 0";
        }

        return null;
    }
    // title
    public String getTitleError(Book book) {

        if(book.getTitle() == null) {
            return "Book title cant be null";
        }
        else if(book.getTitle().isEmpty()) {
            return "Book title cant be empty";
        }
        else if (book.getTitle().trim().length() == 0) {
            return "Book title cant be white spaces";
        }
        else if(book.getTitle().length() > 50) {
            return "Book title max length: 50";
        }

        return null;
    }
    // isbn
    public String getIsbnError(Book book) {

        if(book.getIsbn() == null) {
            return "Book isbn cant be null";
        }
        else if(book.getIsbn().isEmpty()) {
            return "Book isbn cant be empty";
        }
        else if(!validateIsbn(book.getIsbn())) {
            return "Book isbn is invalid";
        }

        return null;
    }
    // price
    public String getPriceError(Book book) {

        if(book.getPrice() == null) {
            return "Book price cant be null";
        }
        else if(book.getPrice().isNaN()) {
            return "Book price must be a number";
        }
        else if(book.getPrice() <= 0) {
            return "Book price must be bigger than 0";
        }

        return null;
    }
    // quantity
    public String getQuantityError(Book book) {

        if(book.getQuantity() == null) {
            return "Book quantity cant be null";
        }
        else if(book.getQuantity() <= 0) {
            return "Book quantity must be bigger than 0";
        }

        return null;
    }
    // authors
    public List<String> getAuthorsError(Book book) {

        List<String> errors = new ArrayList<String>();

        if(book.getAuthors() == null) {
            errors.add("Book authors cant be null");
        }
        else if(book.getAuthors().isEmpty()) {
            errors.add("Book authors cant be empty");
        }
        else {

            Iterator<Author> authors = book.getAuthors().iterator();
            while (authors.hasNext()) {

                Author author = authors.next();
                if(!authorService.isIdValid(author)) {
                    errors.add(authorService.getIdError(author));
                }
            }
        }

        return errors.isEmpty() ? null : errors;
    }
    // genres
    public List<String> getGenresError(Book book) {

        List<String> errors = new ArrayList<String>();

        if(book.getGenres() == null) {
            errors.add("Book genres cant be null");
        }
        else if(book.getGenres().isEmpty()) {
            errors.add("Book genres cant be empty");
        }
        else {

            Iterator<Genre> genres = book.getGenres().iterator();
            while (genres.hasNext()) {

                Genre genre = genres.next();
                if(!genreService.isGenreValid(genre)) {
                    errors.add(genreService.getIdError(genre));
                }
            }
        }

        return errors.isEmpty() ? null : errors;
    }




    /*********************************************************************************************************************
     * book updatable validation
     *********************************************************************************************************************/
    // id
    public boolean isIdUpdatable(Book book) {
        return bookRepository.existsById(book.getId());
    }
    // authors
    public boolean isAuthorsUpdatable(Book book) {

        for (Author author : book.getAuthors()) {
            if(!authorRepository.existsById(author.getId())){
                return false;
            }
        }

        return true;
    }
    // genres
    public boolean isGenresUpdatable(Book book) {

        for (Genre genre : book.getGenres()) {
            if(!genreRepository.existsById(genre.getId())){
                return false;
            }
        }

        return true;
    }
    /*********************************************************************************************************************
     * book updatable errors
     *********************************************************************************************************************/
    // id
    public String getIdUpdatableError(Book book) {
        return "Book id does not exists into the database";
    }
    // authors
    public List<String> getAuthorsUpdatableError(Book book) {

        List<String> errors = new ArrayList<String>();

        for (Author author : book.getAuthors()) {
            if(!authorRepository.existsById(author.getId())){
                errors.add("Author id " + author.getId() + " does not exists in the database");
            }
        }

        return errors;
    }
    // genres
    public List<String> getGenresUpdatableError(Book book) {

        List<String> errors = new ArrayList<String>();

        for (Genre genre : book.getGenres()) {
            if(!genreRepository.existsById(genre.getId())){
                errors.add("Genre id " + genre.getId() + " does not exists in the database");
            }
        }

        return errors;
    }




    /*********************************************************************************************************************
     * book deletable validation
     *********************************************************************************************************************/
     public boolean isBookDeletable(Book book) {

         if(!book.getSaleDetails().isEmpty()) {
             return false;
         }

         return true;
     }
    /*********************************************************************************************************************
     * book deletable errors
     *********************************************************************************************************************/
    public String getBookDeletableErrors(Book book) {

        String error = "";
        List<String> errors = new ArrayList<String>();


        if(!book.getSaleDetails().isEmpty()) {
            for (SaleDetails sd : book.getSaleDetails()) {
                errors.add("Book id: " + book.getId() + " is a dependency on table SALE_DETAILS on record id: " + sd.getId());
            }
        }


        for(int i = 0; i < errors.size(); i++) {

            if(i == errors.size() - 1) {
                error += errors.get(i);
            }
            else {
                error += errors.get(i) + " | ";
            }
        }

        return error;
    }






    /*********************************************************************************************************************
     * public methods
     *********************************************************************************************************************/
    public boolean isBookValid(Book book) {

        return (isTitleValid(book)      &&
                isIsbnValid(book)       &&
                isPriceValid(book)      &&
                isQuantityValid(book)   &&
                isAuthorsValid(book)    &&
                isGenresValid(book));
    }
    public boolean isBookValidWithId(Book book) {

        return (isIdValid(book)      &&
                isTitleValid(book)      &&
                isIsbnValid(book)       &&
                isPriceValid(book)      &&
                isQuantityValid(book)   &&
                isAuthorsValid(book)    &&
                isGenresValid(book));
    }


    public String getBookErrors(Book book) {

        String error = "";
        List<String> errors = new ArrayList<String>();

        if(!isTitleValid(book)) {
            errors.add(getTitleError(book));
        }
        if(!isIsbnValid(book)) {
            errors.add(getIsbnError(book));
        }
        if(!isPriceValid(book)) {
            errors.add(getPriceError(book));
        }
        if(!isQuantityValid(book)) {
            errors.add(getQuantityError(book));
        }
        if(!isAuthorsValid(book)) {
            errors.addAll(getAuthorsError(book));
        }
        if(!isGenresValid(book)) {
            errors.addAll(getGenresError(book));
        }

        for(int i = 0; i < errors.size(); i++) {

            if(i == errors.size() - 1) {
                error += errors.get(i);
            }
            else {
                error += errors.get(i) + " | ";
            }
        }

        return error;
    }
    public String getBookErrorsWithId(Book book) {

        String error = "";
        List<String> errors = new ArrayList<String>();

        if(!isIdValid(book)) {
            errors.add(getIdError(book));
        }
        if(!isTitleValid(book)) {
            errors.add(getTitleError(book));
        }
        if(!isIsbnValid(book)) {
            errors.add(getIsbnError(book));
        }
        if(!isPriceValid(book)) {
            errors.add(getPriceError(book));
        }
        if(!isQuantityValid(book)) {
            errors.add(getQuantityError(book));
        }
        if(!isAuthorsValid(book)) {
            errors.addAll(getAuthorsError(book));
        }
        if(!isGenresValid(book)) {
            errors.addAll(getGenresError(book));
        }

        for(int i = 0; i < errors.size(); i++) {

            if(i == errors.size() - 1) {
                error += errors.get(i);
            }
            else {
                error += errors.get(i) + " | ";
            }
        }

        return error;
    }


    public boolean isBookUpdatable(Book book)
    {
        return (isIdUpdatable(book)      &&
                isAuthorsUpdatable(book) &&
                isGenresUpdatable(book));
    }
    public String getBookUpdatableErrors(Book book)
    {
        String error = "";
        List<String> errors = new ArrayList<String>();

        if(!isIdUpdatable(book)) {
            errors.add(getIdUpdatableError(book));
        }
        if(!isAuthorsUpdatable(book)) {
            errors.addAll(getAuthorsUpdatableError(book));
        }
        if(!isGenresUpdatable(book)) {
            errors.addAll(getGenresUpdatableError(book));
        }

        for(int i = 0; i < errors.size(); i++) {

            if(i == errors.size() - 1) {
                error += errors.get(i);
            }
            else {
                error += errors.get(i) + " | ";
            }
        }

        return error;
    }


    public boolean deleteBookAuthors(Book book) {

        for(Author author : book.getAuthors()) {

            BookAuthorId baId = new BookAuthorId();
            baId.setAuthorId(author.getId());
            baId.setBookId(book.getId());

            BookAuthor ba = new BookAuthor();
            ba.setBookAuthorId(baId);

            bookAuthorRepository.delete(ba);
        }

        return true;
    }
    public boolean deleteBookGenres(Book book) {

        for(Genre genre : book.getGenres()) {

            BookGenreId bgId = new BookGenreId();
            bgId.setGenreId(genre.getId());
            bgId.setBookId(book.getId());

            BookGenre bg = new BookGenre();
            bg.setBookGenreId(bgId);

            bookGenreRepository.delete(bg);
        }

        return true;
    }


    @Autowired
    private BasketRepository basketRepository;
    public void deleteBasket(Book book) {

       basketRepository.deleteByBookId(book.getId());
    }

    public boolean bookExists(long bookId) {
        return bookRepository.existsById(bookId);
    }
    public int getBookQuantity(long bookId) {
        return bookRepository.findById(bookId).get().getQuantity();
    }
    public void decreaseBookQuantity(long bookId, int quantity) {

        Book book = bookRepository.findById(bookId).get();
        int availableQuantity = book.getQuantity();

        if(availableQuantity >= quantity) {

            book.setQuantity(availableQuantity - quantity);
            bookRepository.save(book);
        }
    }
    public void increaseBookQuantity(long bookId, int quantity) {

        Book book = bookRepository.findById(bookId).get();

        int newQuantity = book.getQuantity() + quantity;

        book.setQuantity(newQuantity);

        bookRepository.save(book);
    }
}








