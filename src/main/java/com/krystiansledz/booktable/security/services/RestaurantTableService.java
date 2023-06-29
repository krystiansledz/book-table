package com.krystiansledz.booktable.security.services;

import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.models.RestaurantTable;
import com.krystiansledz.booktable.repository.RestaurantRepository;
import com.krystiansledz.booktable.repository.RestaurantTableRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class RestaurantTableService {

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<RestaurantTable> getAllTables(Long restaurantId) {
        return restaurantTableRepository.findAllByRestaurantId(restaurantId);
    }

    public RestaurantTable getRestaurantTableById(Long id) {
        return restaurantTableRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public RestaurantTable getRestaurantTableByRestaurantIdAndId(Long restaurantId, Long restaurantTableId) {
        return restaurantTableRepository.findByRestaurantIdAndId(restaurantId, restaurantTableId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find RestaurantTable with ID " + restaurantTableId + " for Restaurant with ID " + restaurantId));
    }

    public RestaurantTable createRestaurantTable(RestaurantTable restaurantTable, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(EntityNotFoundException::new);
        restaurantTable.setRestaurant(restaurant);

        Optional<RestaurantTable> existingTableWithNumber = restaurantTableRepository.findByNumberAndRestaurantId(restaurantTable.getNumber(), restaurantTable.getRestaurant().getId());

        if (existingTableWithNumber.isPresent()) {
            throw new IllegalArgumentException("Table already exists with this number for this restaurant");
        }

        return restaurantTableRepository.save(restaurantTable);
    }

    public RestaurantTable updateRestaurantTable(RestaurantTable restaurantTable) {
        Optional<RestaurantTable> existingTableWithNumber = restaurantTableRepository.findByNumberAndRestaurantId(restaurantTable.getNumber(), restaurantTable.getRestaurant().getId());

        if (existingTableWithNumber.isPresent() && !Objects.equals(existingTableWithNumber.get().getId(), restaurantTable.getId())) {
            throw new IllegalArgumentException("Table already exists with this number for this restaurant");
        }

        return restaurantTableRepository.save(restaurantTable);
    }

    public RestaurantTable partialUpdateRestaurantTable(Long id, Map<String, Object> updates) {
        RestaurantTable restaurantTable = getRestaurantTableById(id);

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            switch (key) {
                case "capacity":
                    restaurantTable.setCapacity((Integer) entry.getValue());
                    break;
                case "number":
                    Integer number = (Integer) entry.getValue();

                    Optional<RestaurantTable> existingRestaurantTable = restaurantTableRepository.findByNumberAndRestaurantId(number, restaurantTable.getRestaurant().getId());

                    if (existingRestaurantTable.isPresent() && !Objects.equals(existingRestaurantTable.get().getId(), id)) {
                        throw new IllegalArgumentException("RestaurantTable with this number already exists for this restaurant");
                    }

                    restaurantTable.setNumber(number);
                    break;
            }
        }

        return restaurantTableRepository.save(restaurantTable);
    }

    public void deleteTable(Long id) {
        restaurantTableRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        restaurantTableRepository.deleteById(id);
    }
}
