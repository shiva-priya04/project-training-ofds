package com.ofds.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ofds.entity.Agent;
import com.ofds.service.AgentService;

import jakarta.validation.Valid;

@RestController
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PostMapping("/agent")
    public Agent saveAgent(@Valid @RequestBody Agent agent) {
        return agentService.saveAgent(agent);
    }

    @GetMapping("/agent")
    public List<Agent> getAllAgents() {
        return agentService.getAllAgents();
    }

    @GetMapping("/agent/{agentId}")
    public Agent getAgentById(@PathVariable String id) {
        return agentService.getAgentById(id);
    }

    @DeleteMapping("/agent/{agentId}")
    public String deleteAgent(@PathVariable String id) {

        agentService.deleteAgent(id);

        return "Agent Deleted Successfully";
    }
}
