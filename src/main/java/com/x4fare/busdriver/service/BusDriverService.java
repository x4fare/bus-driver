package com.x4fare.busdriver.service;

import com.x4fare.busdriver.domain.BusDriver;
import com.x4fare.busdriver.repository.BusDriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author finx
 */
@Service
@Transactional
public class BusDriverService {

    private final Logger log = LoggerFactory.getLogger(BusDriverService.class);

    @Autowired
    private BusDriverRepository busDriverRepository;

    public BusDriver save(BusDriver busDriver) {
        log.debug("Request to save BusDriver : {}", busDriver);
        return busDriverRepository.save(busDriver);
    }

    @Transactional(readOnly = true)
    public List<BusDriver> findAll() {
        log.debug("Request to get all BusDrivers");
        return busDriverRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<BusDriver> findOne(Long id) {
        log.debug("Request to get BusDriver : {}", id);
        return busDriverRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete BusDriver : {}", id);
        busDriverRepository.deleteById(id);
    }
}
