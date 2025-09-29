package org.mosesidowu.boxdelivery.data.repository;

import org.mosesidowu.boxdelivery.data.model.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BoxRepository extends JpaRepository<Box, Long> {

    List<Box> findByState(Box.State state);
    Optional<Box> findByTxref(String txref);

}