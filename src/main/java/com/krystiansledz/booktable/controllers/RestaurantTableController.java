package com.krystiansledz.booktable.controllers;

import com.krystiansledz.booktable.dto.RestaurantTableDTO;
import com.krystiansledz.booktable.models.RestaurantTable;
import com.krystiansledz.booktable.security.services.RestaurantTableService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/tables")
public class RestaurantTableController {

    @Autowired
    private RestaurantTableService restaurantTableService;

    @GetMapping
    public ResponseEntity<List<RestaurantTableDTO>> getAllTables(@PathVariable Long restaurantId) {
        List<RestaurantTable> restaurantTableList = restaurantTableService.getAllTables(restaurantId);

        // Map RestaurantTable to RestaurantTableDTO
        List<RestaurantTableDTO> restaurantTableDTOList = restaurantTableList.stream()
                .map(restaurantTable -> {
                    RestaurantTableDTO restaurantTableDTO = new RestaurantTableDTO();
                    BeanUtils.copyProperties(restaurantTable, restaurantTableDTO);
                    restaurantTableDTO.setRestaurant_id(restaurantTable.getRestaurant().getId());
                    return restaurantTableDTO;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(restaurantTableDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTableById(@PathVariable Long id) {
        try {
            RestaurantTable restaurantTable = restaurantTableService.getRestaurantTableById(id);

            RestaurantTableDTO restaurantTableDTO = new RestaurantTableDTO();
            BeanUtils.copyProperties(restaurantTable, restaurantTableDTO);
            restaurantTableDTO.setRestaurant_id(restaurantTable.getRestaurant().getId());

            return new ResponseEntity<>(restaurantTableDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTable(@PathVariable Long restaurantId, @RequestBody RestaurantTableDTO restaurantTableDTO) {
        try {
            RestaurantTable restaurantTable = new RestaurantTable();
            restaurantTable.setCapacity(restaurantTableDTO.getCapacity());
            restaurantTable.setNumber(restaurantTableDTO.getNumber());

            return new ResponseEntity<>(restaurantTableService.createRestaurantTable(restaurantTable, restaurantId), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurantTable(@PathVariable Long id, @RequestBody RestaurantTableDTO restaurantTableDTO) {
        try {
            RestaurantTable restaurantTable = restaurantTableService.getRestaurantTableById(id);
            restaurantTable.setNumber(restaurantTableDTO.getNumber());
            restaurantTable.setCapacity(restaurantTableDTO.getCapacity());
            return new ResponseEntity<>(restaurantTableService.updateRestaurantTable(restaurantTable), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateRestaurantTable(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            return new ResponseEntity<>(restaurantTableService.partialUpdateRestaurantTable(id, updates), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable Long id) {
        try {
            restaurantTableService.deleteTable(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

