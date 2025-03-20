package org.example.controller;

import org.example.model.Order;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {
         private List<Order> orders = new ArrayList<>();

         public OrderController() {
             orders.add(new Order("#12406", "John Parks", "Apples x20", 20.00, "3/10/25", "Delivered"));
             orders.add(new Order("#19247", "Anna Smith", "Wheat x10", 10.00, "3/11/25", "In Transit"));
             orders.add(new Order("#15562", "Jim Green", "Carrots x15", 15.00, "3/12/25", "Canceled"));
             orders.add(new Order("#13055", "Jaylen Barry", "Potatoes x5", 5.00, "3/13/25", "Confirmed"));
             orders.add(new Order("#18324", "Kayla Sims", "Corn x10", 10.00, "3/14/25", "Pending"));
         }

         // Get All Orders
         @GetMapping
         public List<Order> getAllOrders() {
             return orders;
         }

         // Add New Order
         @PostMapping
         public String addOrder(@RequestBody Order order) {
             orders.add(order);
             return "Order Added Successfully!";
         }

         // Update Status of Order
         @PutMapping("/{id}")
         public String updateOrderStatus(@PathVariable String id, @RequestParam String status) {
             for (Order o : orders) {
                 if (o.getOrderId().equals(id)) {
                     o.setStatus(status);
                     return "Order Status Updated!";
                 }
             }
             return "Order Not Found!";
         }

         // Delete Order
         @DeleteMapping("/{id}")
         public String deleteOrder(@PathVariable String id) {
             orders.removeIf(o -> o.getOrderId().equals(id));
             return "Order Deleted!";
         }
}
