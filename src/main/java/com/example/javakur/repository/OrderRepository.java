package com.example.javakur.repository;

import com.example.javakur.model.FoodOrder;

public class OrderRepository extends BaseRepository<FoodOrder, Long> {
    public OrderRepository() {
        super(FoodOrder.class);
    }
}
