package com.farm2table.service.impl;

import com.farm2table.model.Order;
import com.farm2table.repository.OrderRepository;
import com.farm2table.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> getOrdersByBuyerId(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    @Override
    public Order placeOrder(Order order) {
        order.setStatus("PLACED");
        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}