package com.readinessit.bookstore.cucumber.stepdefs;

import com.readinessit.bookstore.BookStoreApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = BookStoreApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
