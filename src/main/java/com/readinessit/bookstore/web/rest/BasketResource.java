package com.readinessit.bookstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.readinessit.bookstore.domain.Basket;
import com.readinessit.bookstore.repository.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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

}
