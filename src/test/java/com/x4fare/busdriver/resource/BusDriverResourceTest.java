package com.x4fare.busdriver.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.x4fare.busdriver.BusDriverApplication;
import com.x4fare.busdriver.domain.BusDriver;
import com.x4fare.busdriver.repository.BusDriverRepository;
import com.x4fare.busdriver.service.BusDriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author finx
 */
@SpringBootTest(classes = BusDriverApplication.class)
@AutoConfigureMockMvc
//@WithMockUser
class BusDriverResourceTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private BusDriverRepository busDriverRepository;

    @Autowired
    private BusDriverService busDriverService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusDriverMockMvc;

    private BusDriver busDriver;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusDriver createEntity(EntityManager em) {
        BusDriver busDriver = new BusDriver()
                .name(DEFAULT_NAME)
                .age(DEFAULT_AGE)
                .birthdate(DEFAULT_BIRTHDATE);
        return busDriver;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusDriver createUpdatedEntity(EntityManager em) {
        BusDriver busDriver = new BusDriver()
                .name(UPDATED_NAME)
                .age(UPDATED_AGE)
                .birthdate(UPDATED_BIRTHDATE);
        return busDriver;
    }

    @BeforeEach
    public void initTest() {
        busDriver = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusDriver() throws Exception {
        int databaseSizeBeforeCreate = busDriverRepository.findAll().size();
        // Create the BusDriver
        restBusDriverMockMvc.perform(post("/api/bus-drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getObjectAsJson(busDriver)))
                .andExpect(status().isCreated());

        // Validate the BusDriver in the database
        List<BusDriver> busDriverList = busDriverRepository.findAll();
        assertThat(busDriverList).hasSize(databaseSizeBeforeCreate + 1);
        BusDriver testBusDriver = busDriverList.get(busDriverList.size() - 1);
        assertThat(testBusDriver.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusDriver.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testBusDriver.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
    }

    @Test
    @Transactional
    public void createBusDriverWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = busDriverRepository.findAll().size();

        // Create the BusDriver with an existing ID
        busDriver.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusDriverMockMvc.perform(post("/api/bus-drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getObjectAsJson(busDriver)))
                .andExpect(status().isBadRequest());

        // Validate the BusDriver in the database
        List<BusDriver> busDriverList = busDriverRepository.findAll();
        assertThat(busDriverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBusDrivers() throws Exception {
        // Initialize the database
        busDriverRepository.saveAndFlush(busDriver);

        // Get all the busDriverList
        restBusDriverMockMvc.perform(get("/api/bus-drivers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(busDriver.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
                .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())));
    }

    @Test
    @Transactional
    public void getBusDriver() throws Exception {
        // Initialize the database
        busDriverRepository.saveAndFlush(busDriver);

        // Get the busDriver
        restBusDriverMockMvc.perform(get("/api/bus-drivers/{id}", busDriver.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(busDriver.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
                .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBusDriver() throws Exception {
        // Get the busDriver
        restBusDriverMockMvc.perform(get("/api/bus-drivers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusDriver() throws Exception {
        // Initialize the database
        busDriverService.save(busDriver);

        int databaseSizeBeforeUpdate = busDriverRepository.findAll().size();

        // Update the busDriver
        BusDriver updatedBusDriver = busDriverRepository.findById(busDriver.getId()).get();
        // Disconnect from session so that the updates on updatedBusDriver are not directly saved in db
        em.detach(updatedBusDriver);
        updatedBusDriver
                .name(UPDATED_NAME)
                .age(UPDATED_AGE)
                .birthdate(UPDATED_BIRTHDATE);

        restBusDriverMockMvc.perform(put("/api/bus-drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getObjectAsJson(busDriver)))
                .andExpect(status().isOk());

        // Validate the BusDriver in the database
        List<BusDriver> busDriverList = busDriverRepository.findAll();
        assertThat(busDriverList).hasSize(databaseSizeBeforeUpdate);
        BusDriver testBusDriver = busDriverList.get(busDriverList.size() - 1);
        assertThat(testBusDriver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusDriver.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testBusDriver.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBusDriver() throws Exception {
        int databaseSizeBeforeUpdate = busDriverRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusDriverMockMvc.perform(put("/api/bus-drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getObjectAsJson(busDriver)))
                .andExpect(status().isBadRequest());

        // Validate the BusDriver in the database
        List<BusDriver> busDriverList = busDriverRepository.findAll();
        assertThat(busDriverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBusDriver() throws Exception {
        // Initialize the database
        busDriverService.save(busDriver);

        int databaseSizeBeforeDelete = busDriverRepository.findAll().size();

        // Delete the busDriver
        restBusDriverMockMvc.perform(delete("/api/bus-drivers/{id}", busDriver.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusDriver> busDriverList = busDriverRepository.findAll();
        assertThat(busDriverList).hasSize(databaseSizeBeforeDelete - 1);
    }

    private String getObjectAsJson(BusDriver busDriver) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(busDriver);
    }

}