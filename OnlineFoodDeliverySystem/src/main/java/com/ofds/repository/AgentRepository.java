package com.ofds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofds.entity.Agent;


@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {

}