package com.gyumin.springreactive0329.dto;

import lombok.Data;

@Data
public class Dish {
    private String description;
    private boolean deliverd = false;

    public Dish(String description) {
        this.description = description;
    }
    public static Dish deliver(Dish dish) {
        Dish deliverdDish = new Dish(dish.description);
        deliverdDish.deliverd = true;
        return deliverdDish;
    }
}