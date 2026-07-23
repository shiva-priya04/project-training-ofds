package com.ofds.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ofds.entity.Agent;
import com.ofds.service.AgentService;

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PostMapping
    public Agent saveAgent(@RequestBody Agent agent) {
        return agentService.saveAgent(agent);
    }

    @GetMapping
    public List<Agent> getAllAgents() {
        return agentService.getAllAgents();
    }

    @GetMapping("/{id}")
    public Agent getAgentById(@PathVariable String id) {
        return agentService.getAgentById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteAgent(@PathVariable String id) {

        agentService.deleteAgent(id);

        return "Agent Deleted Successfully";
    }
}
