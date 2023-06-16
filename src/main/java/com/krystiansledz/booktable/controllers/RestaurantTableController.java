package com.krystiansledz.booktable.controllers;

import com.krystiansledz.booktable.models.RestaurantTable;
import com.krystiansledz.booktable.security.services.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/restaurant-tables")
public class RestaurantTableController {

    @Autowired
    private RestaurantTableService tableService;

    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getAllTables() {
        return new ResponseEntity<>(tableService.getAllTables(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTable> getTableById(@PathVariable Long id) {
        Optional<RestaurantTable> table = tableService.getTableById(id);
        return table.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<RestaurantTable> createTable(@RequestBody RestaurantTable restaurantTable) {
        return new ResponseEntity<>(tableService.createTable(restaurantTable), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantTable> updateTable(@PathVariable Long id, @RequestBody RestaurantTable restaurantTable) {
        Optional<RestaurantTable> tableData = tableService.getTableById(id);
        if (tableData.isPresent()) {
            RestaurantTable updatedTable = tableData.get();
            updatedTable.setNumber(restaurantTable.getNumber());
            updatedTable.setCapacity(restaurantTable.getCapacity());
            return new ResponseEntity<>(tableService.updateTable(updatedTable), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RestaurantTable> partialUpdateTable(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<RestaurantTable> tableData = tableService.getTableById(id);
        if (tableData.isPresent()) {
            RestaurantTable updatedTable = tableData.get();

            for (String key : updates.keySet()) {
                switch (key) {
                    case "number" -> updatedTable.setNumber((Integer) updates.get(key));
                    case "capacity" -> updatedTable.setCapacity((Integer) updates.get(key));
                }
            }
            return new ResponseEntity<>(tableService.updateTable(updatedTable), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTable(@PathVariable Long id) {
        try {
            tableService.deleteTable(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

