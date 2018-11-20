package com.readinessit.bookstore.service;

import com.readinessit.bookstore.domain.Author;
import com.readinessit.bookstore.domain.Book;
import com.readinessit.bookstore.domain.Genre;
import com.readinessit.bookstore.repository.BookRepository;
import org.springframework.stereotype.Component;
import com.readinessit.bookstore.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@Component
public class GenreService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;



    /*********************************************************************************************************************
     * genre validations
     *********************************************************************************************************************/
    // validate id
    public boolean isIdValid(Genre genre) {

        if(genre.getId() == null) {
            return false;
        }
        else if(genre.getId() <= 0) {
            return false;
        }

        return true;
    }
    // validate name
    public boolean isNameValid(Genre genre) {

        if(genre.getName() == null) {
            return false;
        }
        else if(genre.getName().isEmpty()) {
            return false;
        }
        else if (genre.getName().trim().length() == 0) {
            return false;
        }
        else if(genre.getName().length() > 50) {
            return false;
        }
        else if(genreRepository.findByName(genre.getName()) != null) {
            return false;
        }


        return true;
    }




    /*********************************************************************************************************************
     * genre get errors
     *********************************************************************************************************************/
    // id
    public String getIdError(Genre genre) {

        if(genre.getId() == null) {
            return "Genre id cant be null";
        }
        else if(genre.getId() <= 0) {
            return "Genre id must be bigger than 0";
        }

        return null;
    }
    // name
    public String getNameError(Genre genre) {

        if(genre.getName() == null) {
            return "Genre name cant be null";
        }
        else if(genre.getName().isEmpty()) {
            return "Genre name cant be empty";
        }
        else if (genre.getName().trim().length() == 0) {
            return "Genre name cant be white spaces";
        }
        else if(genre.getName().length() > 50) {
            return "Genre name max length: 50";
        }
        else if(genreRepository.findByName(genre.getName()) != null) {
            return "Genre name already exists";
        }

        return null;
    }



    /*********************************************************************************************************************
     * book deletable validation
     *********************************************************************************************************************/
    public boolean isGenreDeletable(Genre genre) {

        List<Book> books = bookRepository.findAll();

        for (Book book : books) {
            for (Genre genr : book.getGenres()) {
                if(genre.getId().equals(genr.getId())) {
                    return false;
                }
            }
        }

        return true;
    }
    /*********************************************************************************************************************
     * book deletable errors
     *********************************************************************************************************************/
    public String getGenreDeletableErrors(Genre genre) {

        String error = "";
        List<String> errors = new ArrayList<String>();


        List<Book> books = bookRepository.findAll();
        for (Book book : books) {
            for (Genre genr : book.getGenres()) {
                if(genre.getId().equals(genr.getId())) {
                    errors.add("Genre id: " + genr.getId() + " is a dependency on table BookGenre with Book id: " + book.getId());
                }
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
    public boolean isGenreValid(Genre genre){

        return isNameValid(genre);
    }
    public boolean isGenreValidWithId(Genre genre){

        return (isIdValid(genre)  &&
                isNameValid(genre));
    }

    public String getGenreErrors(Genre genre) {

        String error = "";
        List<String> errors = new ArrayList<String>();


        if(!isNameValid(genre)) {
            errors.add(getNameError(genre));
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
    public String getGenreErrorsWithId(Genre genre) {

        String error = "";
        List<String> errors = new ArrayList<String>();


        if(!isIdValid(genre)) {
            errors.add(getIdError(genre));
        }
        if(!isNameValid(genre)) {
            errors.add(getNameError(genre));
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
}
