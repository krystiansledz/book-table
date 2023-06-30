package com.krystiansledz.booktable.dto;

public class RatingDTO {
    private Integer rating;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        this.rating = rating;
    }
}
