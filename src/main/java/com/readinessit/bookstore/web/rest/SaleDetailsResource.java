package com.readinessit.bookstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.readinessit.bookstore.domain.Sale;
import com.readinessit.bookstore.domain.SaleDetails;
import com.readinessit.bookstore.domain.User;
import com.readinessit.bookstore.repository.BookRepository;
import com.readinessit.bookstore.repository.SaleDetailsRepository;
import com.readinessit.bookstore.repository.SaleRepository;
import com.readinessit.bookstore.service.BookService;
import com.readinessit.bookstore.service.SaleDetailsService;
import com.readinessit.bookstore.service.SaleService;
import com.readinessit.bookstore.service.UserService;
import com.readinessit.bookstore.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing SaleDetails.
 */
@RestController
@RequestMapping("/api")
public class SaleDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SaleDetailsResource.class);

    private static final String ENTITY_NAME = "saleDetails";

    private SaleDetailsRepository saleDetailsRepository;

    @Autowired
    private SaleDetailsService saleDetailsService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SaleService saleService;


    public SaleDetailsResource(SaleDetailsRepository saleDetailsRepository) {
        this.saleDetailsRepository = saleDetailsRepository;
    }



    /**
     * GET  /sale-details : get all the saleDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of saleDetails in body
     */
    @GetMapping("/sale-details")
    @Timed
    public List<SaleDetails> getAllSaleDetails() {
        log.debug("REST request to get all SaleDetails");
        return saleDetailsRepository.findAll();
    }

    /**
     * GET  /sale-details/:id : get the "id" saleDetails.
     *
     * @param id the id of the saleDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saleDetails, or with status 404 (Not Found)
     */
    @GetMapping("/sale-details/{id}")
    @Timed
    public ResponseEntity<SaleDetails> getSaleDetails(@PathVariable Long id) {
        log.debug("REST request to get SaleDetails : {}", id);
        Optional<SaleDetails> saleDetails = saleDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(saleDetails);
    }




    /**
     * POST  /sale-details : Create a new saleDetails.
     *
     * @param saleDetails the saleDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saleDetails, or with status 400 (Bad Request) if the saleDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-details")
    @Timed
    public ResponseEntity<SaleDetails> createSaleDetails(@RequestBody SaleDetails saleDetails) throws URISyntaxException {
        log.debug("REST request to save SaleDetails : {}", saleDetails);


        if (saleDetails.getId() != null) {
            throw new BadRequestAlertException("A new saleDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }


        // validates if saleDetails fields respects business logic
        if(!saleDetailsService.isSaleDetailsValid(saleDetails)) {
            throw new BadRequestAlertException(saleDetailsService.getSaleDetailsErrors(saleDetails) , ENTITY_NAME, saleDetailsService.getSaleDetailsErrors(saleDetails) );
        }


        // validate if the user trying to create sale_details is the same of the sale table
        if(!saleService.isLoggedUserTheOwner(saleDetails.getSale().getId())) {
            throw new BadRequestAlertException("You have not permission to create Sales_Details on the provided Sale" , ENTITY_NAME, "You have not permission to create Sales_Details on the provided Sale");
        }


        // validate if the sale is not truncate
        if(saleService.isSaleTruncate(saleDetails.getSale().getId()) == null || saleService.isSaleTruncate(saleDetails.getSale().getId())) {
            throw new BadRequestAlertException("The provided Sale is truncated" , ENTITY_NAME, "The provided Sale is truncated");
        }


        // saves sale detail
        SaleDetails result = saleDetailsService.save(saleDetails);


        return ResponseEntity.created(new URI("/api/sale-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * DELETE  /sale-details/:id : delete the "id" saleDetails.
     *
     * @param id the id of the saleDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sale-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaleDetails(@PathVariable Long id) {

        log.debug("REST request to delete SaleDetails : {}", id);


        // gets saleDetails object from database.
        SaleDetails saleDetails = saleDetailsService.getSaleDetails((id));


        // validates if saleDetails is null
        if(saleDetails == null) {
            throw new BadRequestAlertException("The Sale Detail id provided does not match any database Sale Detail id"  , ENTITY_NAME, "Method saleDetailsService.isSaleDetailsValid(saleDetails) returned false");
        }


        // Deletes sale detail and updates books quantity.
        saleDetailsService.deleteSaleDetails(saleDetails);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();

    }





    /**
     * PUT  /sale-details : Updates an existing saleDetails.
     *
     * @param saleDetails the saleDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saleDetails,
     * or with status 400 (Bad Request) if the saleDetails is not valid,
     * or with status 500 (Internal Server Error) if the saleDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect

    @PutMapping("/sale-details")
    @Timed
    public ResponseEntity<SaleDetails> updateSaleDetails(@RequestBody SaleDetails saleDetails) throws URISyntaxException {
        log.debug("REST request to update SaleDetails : {}", saleDetails);


        if (saleDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SaleDetails result = saleDetailsRepository.save(saleDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saleDetails.getId().toString()))
            .body(result);
    }

     */
}
