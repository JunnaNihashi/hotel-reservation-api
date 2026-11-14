
package com.example.hotelreservation.repository; 

//JPAの基本的なCRUD機能を利用
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotelreservation.entity.Customer; 

//Customerテーブルのデータ操作を担当するインターフェース
public interface CustomerRepository extends JpaRepository<Customer, Integer> { 
}
