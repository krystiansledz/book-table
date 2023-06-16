package com.krystiansledz.booktable.controllers;

import com.krystiansledz.booktable.dto.BusinessHoursDTO;
import com.krystiansledz.booktable.models.BusinessHours;
import com.krystiansledz.booktable.security.services.BusinessHoursService;
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
@RequestMapping("/api/v1/business-hours")
public class BusinessHoursController {

    @Autowired
    BusinessHoursService businessHoursService;

    @GetMapping
    public ResponseEntity<List<BusinessHoursDTO>> getBusinessHours() {
        List<BusinessHours> businessHoursList = businessHoursService.getAllBusinessHours();

        // Map BusinessHours to BusinessHoursDTO
        List<BusinessHoursDTO> businessHoursDTOList = businessHoursList.stream()
                .map(businessHours -> {
                    BusinessHoursDTO businessHoursDTO = new BusinessHoursDTO();
                    BeanUtils.copyProperties(businessHours, businessHoursDTO);
                    businessHoursDTO.setRestaurant(businessHours.getRestaurant());
                    return businessHoursDTO;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(businessHoursDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBusinessHoursById(@PathVariable Long id) {
        try {
            BusinessHours businessHours = businessHoursService.getBusinessHoursById(id);

            BusinessHoursDTO businessHoursDTO = new BusinessHoursDTO();
            BeanUtils.copyProperties(businessHours, businessHoursDTO);
            businessHoursDTO.setRestaurant(businessHours.getRestaurant());

            return new ResponseEntity<>(businessHoursDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBusinessHours(@RequestBody BusinessHoursDTO businessHoursDTO) {
        try {
            BusinessHours businessHours = new BusinessHours();
            businessHours.setDayOfWeek(businessHoursDTO.getDayOfWeek());
            businessHours.setOpeningTime(businessHoursDTO.getOpeningTime());
            businessHours.setClosingTime(businessHoursDTO.getClosingTime());

            return new ResponseEntity<>(businessHoursService.createBusinessHours(businessHours, businessHoursDTO.getRestaurant_id()), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBusinessHours(@PathVariable Long id, @RequestBody BusinessHoursDTO businessHoursDTO) {
        try {
            BusinessHours businessHours = businessHoursService.getBusinessHoursById(id);
            businessHours.setDayOfWeek(businessHoursDTO.getDayOfWeek());
            businessHours.setOpeningTime(businessHoursDTO.getOpeningTime());
            businessHours.setClosingTime(businessHoursDTO.getClosingTime());
            return new ResponseEntity<>(businessHoursService.updateBusinessHours(businessHours), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateBusinessHours(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            return new ResponseEntity<>(businessHoursService.partialUpdateBusinessHours(id, updates), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessHours(@PathVariable Long id) {
        try {
            businessHoursService.deleteBusinessHours(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}