package com.ofds.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ofds.entity.Delivery;
import com.ofds.service.DeliveryService;

@RestController
//@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/delivery")
    public Delivery saveDelivery(@Valid @RequestBody Delivery delivery) {
        return deliveryService.saveDelivery(delivery);
    }

    @GetMapping("/delivery")
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }
    
    /*@GetMapping("/agent/{agentId}")
    public List<Delivery> getDeliveriesByAgent(
            @PathVariable String agentId) {

        return deliveryService.getDeliveriesByAgentId(agentId);

    }*/
    

    @GetMapping("/delivery/{delId}")
    public Delivery getDeliveryById(@PathVariable String id) {
        return deliveryService.getDeliveryById(id);
    }

    @DeleteMapping("/delivery/{delId}")
    public String deleteDelivery(@PathVariable String id) {

        deliveryService.deleteDelivery(id);

        return "Delivery Deleted Successfully";
    }
}