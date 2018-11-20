package com.readinessit.bookstore.web.rest;

import com.readinessit.bookstore.BookStoreApp;

import com.readinessit.bookstore.domain.SaleDetails;
import com.readinessit.bookstore.repository.SaleDetailsRepository;
import com.readinessit.bookstore.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.readinessit.bookstore.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SaleDetailsResource REST controller.
 *
 * @see SaleDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookStoreApp.class)
public class SaleDetailsResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private SaleDetailsRepository saleDetailsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSaleDetailsMockMvc;

    private SaleDetails saleDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SaleDetailsResource saleDetailsResource = new SaleDetailsResource(saleDetailsRepository);
        this.restSaleDetailsMockMvc = MockMvcBuilders.standaloneSetup(saleDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaleDetails createEntity(EntityManager em) {
        SaleDetails saleDetails = new SaleDetails()
            .quantity(DEFAULT_QUANTITY);
        return saleDetails;
    }

    @Before
    public void initTest() {
        saleDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaleDetails() throws Exception {
        int databaseSizeBeforeCreate = saleDetailsRepository.findAll().size();

        // Create the SaleDetails
        restSaleDetailsMockMvc.perform(post("/api/sale-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleDetails)))
            .andExpect(status().isCreated());

        // Validate the SaleDetails in the database
        List<SaleDetails> saleDetailsList = saleDetailsRepository.findAll();
        assertThat(saleDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        SaleDetails testSaleDetails = saleDetailsList.get(saleDetailsList.size() - 1);
        assertThat(testSaleDetails.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createSaleDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saleDetailsRepository.findAll().size();

        // Create the SaleDetails with an existing ID
        saleDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleDetailsMockMvc.perform(post("/api/sale-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleDetails)))
            .andExpect(status().isBadRequest());

        // Validate the SaleDetails in the database
        List<SaleDetails> saleDetailsList = saleDetailsRepository.findAll();
        assertThat(saleDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSaleDetails() throws Exception {
        // Initialize the database
        saleDetailsRepository.saveAndFlush(saleDetails);

        // Get all the saleDetailsList
        restSaleDetailsMockMvc.perform(get("/api/sale-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @Test
    @Transactional
    public void getSaleDetails() throws Exception {
        // Initialize the database
        saleDetailsRepository.saveAndFlush(saleDetails);

        // Get the saleDetails
        restSaleDetailsMockMvc.perform(get("/api/sale-details/{id}", saleDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saleDetails.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingSaleDetails() throws Exception {
        // Get the saleDetails
        restSaleDetailsMockMvc.perform(get("/api/sale-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaleDetails() throws Exception {
        // Initialize the database
        saleDetailsRepository.saveAndFlush(saleDetails);

        int databaseSizeBeforeUpdate = saleDetailsRepository.findAll().size();

        // Update the saleDetails
        SaleDetails updatedSaleDetails = saleDetailsRepository.findById(saleDetails.getId()).get();
        // Disconnect from session so that the updates on updatedSaleDetails are not directly saved in db
        em.detach(updatedSaleDetails);
        updatedSaleDetails
            .quantity(UPDATED_QUANTITY);

        restSaleDetailsMockMvc.perform(put("/api/sale-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSaleDetails)))
            .andExpect(status().isOk());

        // Validate the SaleDetails in the database
        List<SaleDetails> saleDetailsList = saleDetailsRepository.findAll();
        assertThat(saleDetailsList).hasSize(databaseSizeBeforeUpdate);
        SaleDetails testSaleDetails = saleDetailsList.get(saleDetailsList.size() - 1);
        assertThat(testSaleDetails.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingSaleDetails() throws Exception {
        int databaseSizeBeforeUpdate = saleDetailsRepository.findAll().size();

        // Create the SaleDetails

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaleDetailsMockMvc.perform(put("/api/sale-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleDetails)))
            .andExpect(status().isBadRequest());

        // Validate the SaleDetails in the database
        List<SaleDetails> saleDetailsList = saleDetailsRepository.findAll();
        assertThat(saleDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSaleDetails() throws Exception {
        // Initialize the database
        saleDetailsRepository.saveAndFlush(saleDetails);

        int databaseSizeBeforeDelete = saleDetailsRepository.findAll().size();

        // Get the saleDetails
        restSaleDetailsMockMvc.perform(delete("/api/sale-details/{id}", saleDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SaleDetails> saleDetailsList = saleDetailsRepository.findAll();
        assertThat(saleDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleDetails.class);
        SaleDetails saleDetails1 = new SaleDetails();
        saleDetails1.setId(1L);
        SaleDetails saleDetails2 = new SaleDetails();
        saleDetails2.setId(saleDetails1.getId());
        assertThat(saleDetails1).isEqualTo(saleDetails2);
        saleDetails2.setId(2L);
        assertThat(saleDetails1).isNotEqualTo(saleDetails2);
        saleDetails1.setId(null);
        assertThat(saleDetails1).isNotEqualTo(saleDetails2);
    }
}
