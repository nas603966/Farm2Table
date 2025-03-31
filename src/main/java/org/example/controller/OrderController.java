package org.example.controller;

import org.example.model.Order;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {
         private List<Order> orders = new ArrayList<>();

         public OrderController() {
             orders.add(new Order("#12406", "John Parks", "Apples x20", 20.00, "3/21/25", "Delivered", "1234567890", "usps"));
             orders.add(new Order("#19247", "John Parks", "Wheat x10", 10.00, "3/22/25", "In Transit", "2468101214", "usps"));
             orders.add(new Order("#15562", "John Parks", "Carrots x15", 15.00, "3/23/25", "Canceled", "3691215182", "usps" ));
             orders.add(new Order("#13055", "John Parks", "Potatoes x5", 5.00, "3/24/25", "Confirmed", "4812162024", "usps"));
             orders.add(new Order("#18324", "John Parks", "Corn x10", 10.00, "3/25/25", "Pending", "5101520253", "usps"));
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
                 if (o.getOrderId().replace("#", "").equals(id.replace("#", ""))) {
                     o.setStatus(status);
                     return "Order Status Updated!";
                 }
             }
             return "Order Not Found!";
         }

         // Delete Order
         @DeleteMapping("/{id}")
         public String deleteOrder(@PathVariable String id) {
             boolean removed = orders.removeIf(o -> o.getOrderId().replace("#", "").equals(id.replace("#", "")));
             return removed ? "Order Deleted!" : "Order Not Found!";
         }


    @GetMapping("/track/{id}")
    public String trackOrder(@PathVariable String id) throws Exception {
        for (Order o : orders) {
            if (o.getOrderId().equals(id)) {
                if (o.getTrackingNumber() == null) return "No Tracking Number Found";

                String apiKey = "shippo_test_c733fad602f27bb64dc89efb8f04d7b50c5033ff"; // Replace with my Shippo Test Key
                URL url = new URL("https://api.goshippo.com/tracks/" + o.getCarrier() + "/" + o.getTrackingNumber() + "/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "ShippoToken " + apiKey);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String output;
                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
                return response.toString();
            }
        }
        return "Order Not Found!";
    }
}
