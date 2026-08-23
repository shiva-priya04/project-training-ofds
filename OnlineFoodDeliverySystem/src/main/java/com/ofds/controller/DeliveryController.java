package com.ofds.controller;

import java.util.List;
import java.util.Map;
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
    
    @GetMapping("/delivery/agent/{agentId}")
    public List<Delivery> getDeliveriesByAgent(
            @PathVariable String agentId) {

        return deliveryService.getDeliveriesByAgentId(agentId);

    }
    

    @GetMapping("/delivery/{delId}")
    public Delivery getDeliveryById(@PathVariable String id) {
        return deliveryService.getDeliveryById(id);
    }

    @PatchMapping("/delivery/{delId}/status")
    public Delivery updateDeliveryStatus(@PathVariable String delId, @RequestBody Map<String, String> body) {
        return deliveryService.updateDeliveryStatus(delId, body.get("status"));
    }

    @DeleteMapping("/delivery/{delId}")
    public String deleteDelivery(@PathVariable String id) {

        deliveryService.deleteDelivery(id);

        return "Delivery Deleted Successfully";
    }
}