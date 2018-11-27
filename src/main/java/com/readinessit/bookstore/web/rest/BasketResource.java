package com.readinessit.bookstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.readinessit.bookstore.domain.Basket;
import com.readinessit.bookstore.repository.BasketRepository;
import com.readinessit.bookstore.web.rest.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;


@RestController
@RequestMapping("/api")
public class BasketResource {

    @Autowired
    private BasketRepository basketRepository;


    @GetMapping("/baskets")
    @Timed
    public List<Basket> getAllBaskets(){
        return basketRepository.findAll();
    }


    @PostMapping("/baskets")
    @Timed
    public ResponseEntity<Basket> createBasket(@RequestBody Basket basket) throws URISyntaxException {


        Basket result = basketRepository.save(basket);
        return ResponseEntity.created(new URI("/api/baskets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, ENTITY_NAME.toString()))
            .body(result);
    }



}
