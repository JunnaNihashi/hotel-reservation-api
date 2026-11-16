
package com.example.hotelreservation.repository; 


//複数の部屋を扱うList
import java.util.List;

//ロックの種類
import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
//DBのロックを指定
import org.springframework.data.jpa.repository.Lock;
//JPQLを書くためのアノテーション
import org.springframework.data.jpa.repository.Query;
//クエリの因数を指定
import org.springframework.data.repository.query.Param;

import com.example.hotelreservation.entity.Room; 

public interface RoomRepository extends JpaRepository<Room, Integer> { 
	
	 // 同じ部屋タイプの客室をID順で取得し、予約処理中はロックする
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Room r WHERE r.roomType.id = :roomTypeId ORDER BY r.id")
    List<Room> findRoomsForReservation(
            @Param("roomTypeId") Integer roomTypeId
    );
    
    List<Room> findRoomsByRoomTypeId(
    	    @Param("roomTypeId") Integer roomTypeId
    	);
	
}
