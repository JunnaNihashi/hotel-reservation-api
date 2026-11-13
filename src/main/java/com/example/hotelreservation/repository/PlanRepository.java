package com.example.hotelreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotelreservation.entity.Plan;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

}