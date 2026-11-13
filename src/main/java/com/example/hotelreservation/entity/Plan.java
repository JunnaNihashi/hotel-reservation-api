package com.example.hotelreservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "stay_plan")
    private String stayPlan;

    @Column(name = "price")
    private Integer price;

	public Integer getId() {
		return id;
	}

	public String getStayPlan() {
		return stayPlan;
	}

	public Integer getPrice() {
		return price;
	}
}