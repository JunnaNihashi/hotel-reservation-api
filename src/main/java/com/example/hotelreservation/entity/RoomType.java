package com.example.hotelreservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "room_types")
public class RoomType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "room_type")
	private String roomType;
	
	@Column(name = "price")
	private Integer price;

	public Integer getId() {
		return id;
	}

	public String getRoomType() {
		return roomType;
	}

	public Integer getPrice() {
		return price;
	}

}
