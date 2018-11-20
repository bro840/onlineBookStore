package com.readinessit.bookstore.service;

import com.readinessit.bookstore.domain.Book;
import com.readinessit.bookstore.domain.Sale;
import com.readinessit.bookstore.domain.SaleDetails;
import com.readinessit.bookstore.domain.User;
import com.readinessit.bookstore.repository.BookRepository;
import com.readinessit.bookstore.repository.SaleRepository;
import com.readinessit.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SaleService {


    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    /*********************************************************************************************************************
     * sale fields validations
     *********************************************************************************************************************/
    // user
    public boolean isUserValid(Sale sale) {

        if(sale.getUser() == null) {
            return false;
        }
        else if(sale.getUser().getId() == null) {
            return false;
        }
        else if(sale.getUser().getId() <= 0) {
            return false;
        }
        else if(!userRepository.existsById(sale.getUser().getId())) {
            return false;
        }

        return true;
    }
    // date
    public boolean isDateValid(Sale sale) {

        LocalDate today = LocalDate.now();

        if(sale.getDate() == null) {
            return false;
        }
        else if(sale.getDate().isAfter(today)) {
            return false;
        }
        else if(sale.getDate().isBefore(today)) {
            return false;
        }

        return true;
    }
    /*********************************************************************************************************************
     * sale fields errors
     *********************************************************************************************************************/
     // user
    public String getUserError(Sale sale) {

        if(sale.getUser() == null) {
            return "User cant be null";
        }
        else if(sale.getUser().getId() == null) {
            return "User id cant be null";
        }
        else if(sale.getUser().getId() <= 0) {
            return "User id must be bigger than 0";
        }
        else if(!userRepository.existsById(sale.getUser().getId())) {
            return "The user id provided does not match any database user id";
        }

        return null;
    }
    // date
    public String getDateError(Sale sale) {

        LocalDate today = LocalDate.now();

        if(sale.getDate() == null) {
            return "Date cant be null";
        }
        else if(sale.getDate().isAfter(today)) {
            return "Date cant be after today";
        }
        else if(sale.getDate().isBefore(today)) {
            return "Date cant be before today";
        }

        return null;
    }



    /*********************************************************************************************************************
     * sale deletable validation
     *********************************************************************************************************************/
    public boolean isSaleDeletable(Long saleId) {

        if(saleId <= 0) {
            return false;
        }
        else if(!saleRepository.existsById(saleId)) {
            return false;
        }
        else {
            Sale sale = saleRepository.findById(saleId).get();

            if(!sale.getSaleDetails().isEmpty()) {
                return false;
            }
        }

        return true;
    }
    /*********************************************************************************************************************
     * sale deletable errors
     *********************************************************************************************************************/
    public String getSaleDeletableError(Long saleId) {

        List<String> errors = new ArrayList<String>();

        if(saleId <= 0) {
            return "Sale id must be bigger than 0";
        }
        else if(!saleRepository.existsById(saleId)) {
            return "The sale id provided does not match any database sale id";
        }
        else {
            Sale sale = saleRepository.findById(saleId).get();

            if(!sale.getSaleDetails().isEmpty()) {
                return "Cant delete Sale because it has one or more dependencies on table sale_details";
            }
        }

        return null;
    }




    /*********************************************************************************************************************
     * public methods
     *********************************************************************************************************************/
    public boolean isSaleValid(Sale sale) {

        return (isUserValid(sale)      &&
                isDateValid(sale));
    }
    public String getSaleErrors(Sale sale) {

        String error = "";
        List<String> errors = new ArrayList<String>();

        if(!isDateValid(sale)) {
            errors.add(getDateError(sale));
        }
        if(!isUserValid(sale)) {
            errors.add(getUserError(sale));
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

    public boolean isLoggedUserTheOwner(long saleId) {

        Optional<Sale> sale = saleRepository.findById(saleId);

        if(sale.isPresent()) {
            if (sale.get().getUser().getId().equals(userService.getUserId())) {
                return true;
            }
        }

        return false;
    }
    public Boolean isSaleTruncate(long saleId) {

        Optional<Sale> sale = saleRepository.findById(saleId);

        if(sale.isPresent()) {
            return  sale.get().isTruncate();
        }

        return null;
    }
}
