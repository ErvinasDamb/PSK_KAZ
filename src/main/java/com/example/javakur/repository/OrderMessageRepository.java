package com.example.javakur.repository;

import com.example.javakur.model.OrderMessage;

public class OrderMessageRepository extends BaseRepository<OrderMessage, Long> {
    public OrderMessageRepository() {
        super(OrderMessage.class);
    }
}
