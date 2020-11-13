package com.x4fare.busdriver.repository;

import com.x4fare.busdriver.domain.BusDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author finx
 */
@Repository
public interface BusDriverRepository extends JpaRepository<BusDriver, Long> {
}
