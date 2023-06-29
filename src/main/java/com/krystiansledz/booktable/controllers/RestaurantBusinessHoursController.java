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
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/businessHours")
public class RestaurantBusinessHoursController {

    @Autowired
    BusinessHoursService businessHoursService;

    @GetMapping
    public ResponseEntity<List<BusinessHoursDTO>> getBusinessHours(@PathVariable Long restaurantId) {
        List<BusinessHours> businessHoursList = businessHoursService.getAllBusinessHours(restaurantId);

        // Map BusinessHours to BusinessHoursDTO
        List<BusinessHoursDTO> businessHoursDTOList = businessHoursList.stream()
                .map(businessHours -> {
                    BusinessHoursDTO businessHoursDTO = new BusinessHoursDTO();
                    BeanUtils.copyProperties(businessHours, businessHoursDTO);
                    businessHoursDTO.setRestaurant(businessHours.getRestaurant().getId());
                    return businessHoursDTO;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(businessHoursDTOList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createBusinessHours(@PathVariable Long restaurantId, @RequestBody BusinessHoursDTO businessHoursDTO) {
        try {
            BusinessHours businessHours = new BusinessHours();
            businessHours.setDayOfWeek(businessHoursDTO.getDayOfWeek());
            businessHours.setOpeningTime(businessHoursDTO.getOpeningTime());
            businessHours.setClosingTime(businessHoursDTO.getClosingTime());

            return new ResponseEntity<>(businessHoursService.createBusinessHours(businessHours, restaurantId), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}