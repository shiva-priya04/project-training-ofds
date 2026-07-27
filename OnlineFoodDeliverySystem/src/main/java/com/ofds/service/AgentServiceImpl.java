package com.ofds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofds.entity.Agent;
import com.ofds.repository.AgentRepository;

@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public Agent saveAgent(Agent agent) {
        return agentRepository.save(agent);
    }

    @Override
    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    @Override
    public Agent getAgentById(String agentId) {
        return agentRepository.findById(agentId).orElse(null);
    }

    @Override
    public void deleteAgent(String agentId) {
        agentRepository.deleteById(agentId);
    }
    
    @Override
    public List<String> getCustomerAddresses(String agentId) {

        Agent agent = agentRepository.findById(agentId).orElseThrow(() -> new RuntimeException("Agent not found"));

        return agent.getDeliveries().stream().map(delivery ->delivery.getOrder().getCustomer().getCustomerAddress()).toList();
    }
}