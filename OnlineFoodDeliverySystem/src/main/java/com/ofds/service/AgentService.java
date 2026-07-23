package com.ofds.service;

import java.util.List;

import com.ofds.entity.Agent;

public interface AgentService {

    Agent saveAgent(Agent agent);

    List<Agent> getAllAgents();

    Agent getAgentById(String agentId);

    void deleteAgent(String agentId);
}