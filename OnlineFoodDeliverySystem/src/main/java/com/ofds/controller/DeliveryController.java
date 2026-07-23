package com.ofds.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ofds.entity.Delivery;
import com.ofds.service.DeliveryService;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping
    public Delivery saveDelivery(@RequestBody Delivery delivery) {
        return deliveryService.saveDelivery(delivery);
    }

    @GetMapping
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }
    
    @GetMapping("/agent/{agentId}")
    public List<Delivery> getDeliveriesByAgent(
            @PathVariable String agentId) {

        return deliveryService.getDeliveriesByAgentId(agentId);

    }

    @GetMapping("/{id}")
    public Delivery getDeliveryById(@PathVariable String id) {
        return deliveryService.getDeliveryById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteDelivery(@PathVariable String id) {

        deliveryService.deleteDelivery(id);

        return "Delivery Deleted Successfully";
    }
}