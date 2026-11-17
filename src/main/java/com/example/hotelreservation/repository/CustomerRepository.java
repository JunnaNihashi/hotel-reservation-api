
package com.example.hotelreservation.repository; 

//検索結果が存在しない場合も扱えるようにする
import java.util.Optional;

//JPAの基本的なCRUD機能を利用
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotelreservation.entity.Customer; 

//Customerテーブルのデータ操作を担当するインターフェース
public interface CustomerRepository extends JpaRepository<Customer, Integer> { 
	
	
	// 名前・電話番号・メールアドレスがすべて一致する顧客を検索する
	Optional<Customer> findByNameAndPhoneNumberAndEmail(
	        String name,
	        String phoneNumber,
	        String email
	);
}
