package com.x4fare.busdriver.resource;

import com.x4fare.busdriver.domain.BusDriver;
import com.x4fare.busdriver.service.BusDriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * @author finx
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class BusDriverResource {

    private final Logger log = LoggerFactory.getLogger(BusDriverResource.class);

    private static final String ENTITY_NAME = "busDriver";

    @Autowired
    private BusDriverService busDriverService;

    @PostMapping("/bus-drivers")
    public ResponseEntity<BusDriver> createBusDriver(@Valid @RequestBody BusDriver busDriver) throws URISyntaxException {
        log.debug("REST request to save BusDriver : {}", busDriver);
        if (busDriver.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        BusDriver result = busDriverService.save(busDriver);
        return ResponseEntity.created(new URI("/api/bus-drivers/" + result.getId())).body(result);
    }

    @PutMapping("/bus-drivers")
    public ResponseEntity<BusDriver> updateBusDriver(@Valid @RequestBody BusDriver busDriver) throws URISyntaxException {
        log.debug("REST request to update BusDriver : {}", busDriver);
        if (busDriver.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        BusDriver result = busDriverService.save(busDriver);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/bus-drivers")
    public ResponseEntity<List<BusDriver>> getAllBusDrivers() {
        log.debug("REST request to get a list of BusDrivers");
        List<BusDriver> list = busDriverService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/bus-drivers/{id}")
    public ResponseEntity<BusDriver> getBusDriver(@PathVariable Long id) {
        log.debug("REST request to get BusDriver : {}", id);
        Optional<BusDriver> busDriver = busDriverService.findOne(id);
        return ResponseEntity.of(busDriver);
    }

    @DeleteMapping("/bus-drivers/{id}")
    public ResponseEntity<Void> deleteBusDriver(@PathVariable Long id) {
        log.debug("REST request to delete BusDriver : {}", id);
        busDriverService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
