package com.farm2table.service;

import com.farm2table.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByBuyerId(Long buyerId);
    Order placeOrder(Order order);
    void cancelOrder(Long orderId);
}