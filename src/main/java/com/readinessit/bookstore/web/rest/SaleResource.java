package com.readinessit.bookstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.readinessit.bookstore.domain.Sale;
import com.readinessit.bookstore.domain.User;
import com.readinessit.bookstore.repository.SaleRepository;
import com.readinessit.bookstore.repository.UserRepository;
import com.readinessit.bookstore.service.SaleDetailsService;
import com.readinessit.bookstore.service.SaleService;
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
 * REST controller for managing Sale.
 */
@RestController
@RequestMapping("/api")
public class SaleResource {

    private final Logger log = LoggerFactory.getLogger(SaleResource.class);

    private static final String ENTITY_NAME = "sale";

    private SaleRepository saleRepository;

    @Autowired
    private SaleService saleService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SaleDetailsService saleDetailsService;

    public SaleResource(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }


    /**
     * GET  /sales : get all the sales.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sales in body
     */
    @GetMapping("/sales")
    @Timed
    public List<Sale> getAllSales() {
        log.debug("REST request to get all Sales");
        return saleRepository.findAll();
    }

    /**
     * GET  /sales/:id : get the "id" sale.
     *
     * @param id the id of the sale to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sale, or with status 404 (Not Found)
     */
    @GetMapping("/sales/{id}")
    @Timed
    public ResponseEntity<Sale> getSale(@PathVariable Long id) {
        log.debug("REST request to get Sale : {}", id);
        Optional<Sale> sale = saleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sale);
    }

    @GetMapping("/sales/user/{userId}")
    @Timed
    public List<Sale> getSaleByUserId(@PathVariable Long userId) {

        log.debug("REST request to get Sale by userId : {}", userId);

        // get sales by user, and sets user to null in order to not be serialized
        List<Sale> salesByUser = saleRepository.findByUserId(userId);
        for (Sale sale : salesByUser) {
            sale.setUser(null);
        }
        return  salesByUser;
    }




    /**
     * POST  /sales : Create a new sale.
     *
     * @param sale the sale to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sale, or with status 400 (Bad Request) if the sale has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sales")
    @Timed
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) throws URISyntaxException {

        log.debug("REST request to save Sale : {}", sale);


        // if id provided then its an update not a create.
        if (sale.getId() != null) {
            throw new BadRequestAlertException("A new sale cannot already have an ID", ENTITY_NAME, "idexists");
        }


        // validates if sale fields respects business logic
        if(!saleService.isSaleValid(sale)) {
            throw new BadRequestAlertException(saleService.getSaleErrors(sale) , ENTITY_NAME, saleService.getSaleErrors(sale));
        }


        // reset user with current logged user
        sale.setUser(userRepository.findById(userService.getUserId()).get());

        Sale result = saleRepository.save(sale);
        result.setUser(userRepository.findById(result.getUser().getId()).get());
        return ResponseEntity.created(new URI("/api/sales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * DELETE  /sales/:id : delete the "id" sale.
     *
     * @param id the id of the sale to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sales/{id}")
    @Timed
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        log.debug("REST request to delete Sale : {}", id);


        // validates if logged user has the required admin privilege to this action
        if(!userService.isAdmin()) {
            throw new ForbiddenActionAlertException("Forbidden action", ENTITY_NAME, "You are not allowed to delete sales");
        }


        // validates if the current sale has dependencies on table sale_details
        if(!saleService.isSaleDeletable(id)) {
            throw new BadRequestAlertException(saleService.getSaleDeletableError(id), ENTITY_NAME, saleService.getSaleDeletableError(id));
        }


        saleRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }



    @PutMapping("/sales/{id}/truncate")
    @Timed
    public ResponseEntity<Sale> truncateSale(@PathVariable Long id) {

        Optional<Sale> sale = saleRepository.findById(id);

        if(sale.isPresent()) {
            sale.get().setTruncate(true);
            saleRepository.save(sale.get());

            sale.get().setSaleDetails(saleDetailsService.getSaleDetailsBySale(sale.get().getId()));

            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sale.get().getId().toString()))
                .body(sale.get());
        }
        else {
            throw new BadRequestAlertException("Sale id does not match any database Sale id", ENTITY_NAME, "Sale id does not match any database Sale id");
        }
    }


     /*
     * PUT  /sales : Updates an existing sale.
     *
     * @param sale the sale to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sale,
     * or with status 400 (Bad Request) if the sale is not valid,
     * or with status 500 (Internal Server Error) if the sale couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect

    @PutMapping("/sales")
    @Timed
    /**
    public ResponseEntity<Sale> updateSale(@RequestBody Sale sale) throws URISyntaxException {
        log.debug("REST request to update Sale : {}", sale);
        if (sale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sale result = saleRepository.save(sale);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sale.getId().toString()))
            .body(result);
    }
    */
}
