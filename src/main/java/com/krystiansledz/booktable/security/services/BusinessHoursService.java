package com.krystiansledz.booktable.security.services;

import com.krystiansledz.booktable.models.BusinessHours;
import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.repository.BusinessHoursRepository;
import com.krystiansledz.booktable.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BusinessHoursService {

    @Autowired
    private BusinessHoursRepository businessHoursRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<BusinessHours> getAllBusinessHours() {
        return businessHoursRepository.findAll();
    }

    public BusinessHours getBusinessHoursById(Long id) {
        return businessHoursRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public BusinessHours createBusinessHours(BusinessHours businessHours, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(EntityNotFoundException::new);
        businessHours.setRestaurant(restaurant);

        Optional<BusinessHours> existingBusinessHours = businessHoursRepository.findByDayOfWeekAndRestaurantId(businessHours.getDayOfWeek(), businessHours.getRestaurant().getId());
        System.out.println(existingBusinessHours);
        if (existingBusinessHours.isPresent()) {
            throw new IllegalArgumentException("BusinessHours already exists for this day and restaurant");
        }

        return businessHoursRepository.save(businessHours);
    }

    public BusinessHours updateBusinessHours(Long id, BusinessHours businessHours) {
        // Here should be your implementation of the update operation
        return businessHoursRepository.save(businessHours);
    }

    public BusinessHours partialUpdateBusinessHours(Long id, Map<String, Object> updates) {
        BusinessHours businessHours = getBusinessHoursById(id);

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            if (entry.getKey().equals("openingTime") || entry.getKey().equals("closingTime")) {
                updates.put(entry.getKey(), LocalTime.parse((String) entry.getValue()));
            }
            String key = entry.getKey();
            switch (key) {
                case "openingTime":
                    businessHours.setOpeningTime((LocalTime) entry.getValue());
                    break;
                case "closingTime":
                    businessHours.setClosingTime((LocalTime) entry.getValue());
                    break;
                case "dayOfWeek":
                    businessHours.setDayOfWeek((DayOfWeek) entry.getValue());
                    break;
                // continue with other fields if needed
            }
        }

        return businessHoursRepository.save(businessHours);
    }

    public void deleteBusinessHours(Long id) {
        BusinessHours businessHours = getBusinessHoursById(id);
        businessHoursRepository.delete(businessHours);
    }
}


