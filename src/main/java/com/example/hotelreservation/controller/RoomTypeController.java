package com.example.hotelreservation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotelreservation.entity.RoomType;
import com.example.hotelreservation.repository.RoomTypeRepository;

@RestController
@RequestMapping("/api/room-types")

public class RoomTypeController {
	
	private final RoomTypeRepository roomTypeRepository;
	
	public RoomTypeController(RoomTypeRepository roomTypeRepository) {
		this.roomTypeRepository = roomTypeRepository;
	}
	
	@GetMapping
	public List<RoomType> getRoomTypes(){
		return roomTypeRepository.findAll();
	}

}
