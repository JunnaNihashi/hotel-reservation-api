
package com.example.hotelreservation.repository; 

//JPAの基本的なCRUD機能を利用
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotelreservation.entity.Reservation; 


//Reservationテーブルのデータ操作を担当するインターフェース
public interface ReservationRepository extends JpaRepository<Reservation, Integer> { 

}
