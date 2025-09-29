package org.mosesidowu.boxdelivery.data.repository;

import org.mosesidowu.boxdelivery.data.model.Box;
import org.mosesidowu.boxdelivery.data.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

        List<Item> findByBox(Box box);
}
