package com.readinessit.bookstore.service;

import com.readinessit.bookstore.domain.Sale;
import com.readinessit.bookstore.domain.SaleDetails;
import com.readinessit.bookstore.repository.BookRepository;
import com.readinessit.bookstore.repository.SaleDetailsRepository;
import com.readinessit.bookstore.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class SaleDetailsService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private SaleDetailsRepository saleDetailsRepository;



    /*********************************************************************************************************************
     * book fields validations
     *********************************************************************************************************************/
    // id
    public boolean isIdValid(SaleDetails saleDetails) {

        if(saleDetails.getId() == null) {
            return false;
        }
        else if(saleDetails.getId() <= 0) {
            return false;
        }

        return true;
    }
    // quantity
    public boolean isQuantityValid(SaleDetails saleDetails) {

        if(saleDetails.getQuantity() == null) {
            return false;
        }
        else if(saleDetails.getQuantity() <= 0) {
            return false;
        }
        else if (bookService.getBookQuantity(saleDetails.getBook().getId()) < saleDetails.getQuantity()) {
            return false;
        }

        return true;
    }
    // sale
    public boolean isSaleValid(SaleDetails saleDetails) {

        if(saleDetails.getSale() == null) {
            return false;
        }
        else if(saleDetails.getSale().getId() == null) {
            return false;
        }
        else if(saleDetails.getSale().getId() <= 0) {
            return false;
        }
        else if(!saleRepository.existsById(saleDetails.getSale().getId())) {
            return false;
        }

        return true;
    }
    // book
    public boolean isBookValid(SaleDetails saleDetails) {

        if(saleDetails.getBook() == null) {
            return false;
        }
        else if(saleDetails.getBook().getId() == null) {
            return false;
        }
        else if(saleDetails.getBook().getId() <= 0) {
            return false;
        }
        else if(!bookRepository.existsById(saleDetails.getBook().getId())) {
            return false;
        }

        return true;
    }
    /*********************************************************************************************************************
     * book fields erros
     *********************************************************************************************************************/
    // id
    public String getIdError(SaleDetails saleDetails) {

        if(saleDetails.getId() == null) {
            return "Sale Details id cant be null";
        }
        else if(saleDetails.getId() <= 0) {
            return "Sale Details id must be bigger than 0";
        }

        return null;
    }
    // quantity
    public String getQuantityError(SaleDetails saleDetails) {

        if(saleDetails.getQuantity() == null) {
            return "Sale Details quantity cant be null";
        }
        else if(saleDetails.getQuantity() <= 0) {
            return "Sale Details quantity must be bigger than 0";
        }
        else {

            int bookQntRequired  = saleDetails.getQuantity();
            int bookQntAvailable = bookService.getBookQuantity(saleDetails.getBook().getId());

            if(bookQntRequired > bookQntAvailable) {
                return "Sale Details quantity(" + bookQntRequired +") is bigger than book quantity available(" + bookQntAvailable  +")";
            }
        }

        return null;
    }
    // sale
    public String getSaleError(SaleDetails saleDetails) {

        if(saleDetails.getSale() == null) {
            return "Sale cant be null";
        }
        else if(saleDetails.getSale().getId() == null) {
            return "Sale id cant be null";
        }
        else if(saleDetails.getSale().getId() <= 0) {
            return "Sale is must be bigger than 0";
        }
        else if(!saleRepository.existsById(saleDetails.getSale().getId())) {
            return "The Sale id provided does not match any database Sale id";
        }

        return null;
    }
    // book
    public String getBookError(SaleDetails saleDetails) {

        if(saleDetails.getBook() == null) {
            return "Book cant be null";
        }
        else if(saleDetails.getBook().getId() == null) {
            return "Book id cant be null";
        }
        else if(saleDetails.getBook().getId() <= 0) {
            return "Book id must be bigger than 0";
        }
        else if(!bookRepository.existsById(saleDetails.getBook().getId())) {
            return "The Book id provided does not match any database Book id";
        }

        return null;
    }




    /*********************************************************************************************************************
     * public methods
     *********************************************************************************************************************/
    public boolean isSaleDetailsValid(SaleDetails saleDetails) {

        return (isQuantityValid(saleDetails)  &&
                isSaleValid(saleDetails)      &&
                isBookValid(saleDetails));
    }
    public String getSaleDetailsErrors(SaleDetails saleDetails) {

        String error = "";
        List<String> errors = new ArrayList<String>();

        if(!isQuantityValid(saleDetails)) {
            errors.add(getQuantityError(saleDetails));
        }
        if(!isBookValid(saleDetails)) {
            errors.add(getBookError(saleDetails));
        }
        if(!isSaleValid(saleDetails)) {
            errors.add(getSaleError(saleDetails));
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
     * repository methods
     *********************************************************************************************************************/
    public SaleDetails save(SaleDetails saleDetails) {

        // saves sale detail
        SaleDetails result = saleDetailsRepository.save(saleDetails);

        // update book quantity
        bookService.decreaseBookQuantity(saleDetails.getBook().getId(), saleDetails.getQuantity());

        // get objs.
        result.setBook(bookRepository.findById(result.getBook().getId()).get());
        result.setSale(saleRepository.findById(result.getSale().getId()).get());

        return result;
    }
    public SaleDetails getSaleDetails(Long saleDetailsId) {

        if(saleDetailsRepository.existsById(saleDetailsId)) {
            return saleDetailsRepository.findById(saleDetailsId).get();
        }

        return null;
    }
    public Set<SaleDetails> getSaleDetailsBySale(Long saleId) {

        return saleDetailsRepository.findBySaleId(saleId);

    }
    public void deleteSaleDetails(SaleDetails saleDetails) {

        // updates book quantity
        bookService.increaseBookQuantity(saleDetails.getBook().getId(), saleDetails.getQuantity());

        // deletes saleDetails
        saleDetailsRepository.deleteById(saleDetails.getId());
    }

}

