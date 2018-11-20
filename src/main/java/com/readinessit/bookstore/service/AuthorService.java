package com.readinessit.bookstore.service;

import com.readinessit.bookstore.domain.Author;
import com.readinessit.bookstore.domain.Book;
import com.readinessit.bookstore.repository.BookRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;
import com.readinessit.bookstore.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AuthorService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;



    /*********************************************************************************************************************
     * author validations
     *********************************************************************************************************************/
    // id
    public boolean isIdValid(Author author) {

        if(author.getId() == null) {
            return false;
        }
        else if(author.getId() <= 0) {
            return false;
        }

        return true;
    }
    // name
    public boolean isNameValid(Author author) {

        if(author.getName() == null) {
            return false;
        }
        else if(author.getName().isEmpty()) {
            return false;
        }
        else if (author.getName().trim().length() == 0) {
            return false;
        }
        else if(author.getName().length() > 50) {
            return false;
        }
        else if(authorRepository.findByName(author.getName()) != null) {
            return false;
        }

        return true;
    }
    // country
    public boolean isCountryValid(Author author) {

        if(author.getCountry() == null) {
            return false;
        }
        else if(!author.getCountry().getClass().isEnum() ) {
            return false;
        }

        return true;
    }




    /*********************************************************************************************************************
     * author get errors
     *********************************************************************************************************************/
    // id
    public String getIdError(Author author) {

        if(author.getId() == null) {
            return "Author id cant be null";
        }
        else if(author.getId() <= 0) {
            return "Author id must be bigger than 0";
        }

        return null;
    }
    // name
    public String getNameError(Author author) {

        if(author.getName() == null) {
            return "Author name cant be null";
        }
        else if(author.getName().isEmpty()) {
            return "Author name cant be empty";
        }
        else if (author.getName().trim().length() == 0) {
            return "Author name cant be white spaces";
        }
        else if(author.getName().length() > 50) {
            return "Author name max length: 50";
        }
        else if(authorRepository.findByName(author.getName()) != null) {
            return "Author name already exists";
        }

        return null;
    }
    // country
    public String getCountryError(Author author) {

        if(author.getCountry() == null) {
            return "Author country cant be null";
        }
        else if(!author.getCountry().getClass().isEnum() ) {
            return "Author country must be of type Country Enum";
        }

        return null;
    }




    /*********************************************************************************************************************
     * book deletable validation
     *********************************************************************************************************************/
    public boolean isAuthorDeletable(Author author) {

        // if author is nut null and if he has at least one book then return false
        if(author != null && !author.getBooks().isEmpty()) {
            return false;
        }

        return true;
    }
    /*********************************************************************************************************************
     * book deletable errors
     *********************************************************************************************************************/
    public String getAuthorDeletableErrors(Author author) {

        String error = "";
        List<String> errors = new ArrayList<String>();


        for(Book book : author.getBooks()) {
            errors.add("Author id: " + author.getId() + " is a dependency on table BookAuthor with Book id: " + book.getId());
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
    public boolean isAuthorValid(Author author){

        return (isNameValid(author)  &&
            isCountryValid(author));
    }
    public boolean isAuthorValidWithId(Author author){

        return (isIdValid(author)    &&
                isNameValid(author)  &&
                isCountryValid(author));
    }

    public String getAuthorErrors(Author author) {

        String error = "";
        List<String> errors = new ArrayList<String>();


        if(!isNameValid(author)) {
            errors.add(getNameError(author));
        }
        if(!isCountryValid(author)) {
            errors.add(getCountryError(author));
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
    public String getAuthorErrorsWithId(Author author) {

        String error = "";
        List<String> errors = new ArrayList<String>();


        if(!isIdValid(author)) {
            errors.add(getIdError(author));
        }
        if(!isNameValid(author)) {
            errors.add(getNameError(author));
        }
        if(!isCountryValid(author)) {
            errors.add(getCountryError(author));
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
     * crud methods
     *********************************************************************************************************************/
    public List<Author> getByAuthorName(String authorName){

        return authorRepository.findByNameContainingIgnoreCase(authorName);
    }
}
