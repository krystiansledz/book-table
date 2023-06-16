package com.krystiansledz.booktable.security.services;

import com.krystiansledz.booktable.models.RestaurantTable;
import com.krystiansledz.booktable.repository.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantTableService {

    @Autowired
    private RestaurantTableRepository tableRepository;

    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }

    public Optional<RestaurantTable> getTableById(Long id) {
        return tableRepository.findById(id);
    }

    public RestaurantTable createTable(RestaurantTable restaurantTable) {
        return tableRepository.save(restaurantTable);
    }

    public RestaurantTable updateTable(RestaurantTable restaurantTable) {
        return tableRepository.save(restaurantTable);
    }

    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }
}
