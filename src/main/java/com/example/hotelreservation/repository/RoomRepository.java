
package com.example.hotelreservation.repository; 

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotelreservation.entity.Room; 

public interface RoomRepository extends JpaRepository<Room, Integer> { 
	
}
