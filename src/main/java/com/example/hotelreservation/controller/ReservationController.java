package com.example.hotelreservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.hotelreservation.form.ReservationForm;



@Controller
public class ReservationController {
	
	// トップページ（index.html）をThymeleafで表示する処理
	
	@GetMapping("/")
	public String showPage() {
		return "index";
	}

	// POSTされた入力情報をReservationFormオブジェクトとして受け取る
	@PostMapping("/reservations")
	public String saveReservation(@ModelAttribute ReservationForm form) {
		
		 // 受け取った情報をEclipseのコンソールに表示して確認する
		System.out.println("お名前:" + form.getGuestName());
		System.out.println("メール：" + form.getEmail());
        System.out.println("電話番号：" + form.getPhone());
        System.out.println("住所：" + form.getAddress());
        System.out.println("チェックイン日：" + form.getCheckin());
        System.out.println("チェックアウト日：" + form.getCheckout());
        System.out.println("お部屋ID：" + form.getRoomTypeId());
        System.out.println("宿泊プランID：" + form.getPlan());
        System.out.println("ご要望：" + form.getRequests());

        
        return "redirect:/thanks";
	}
	
	 // GETリクエストを受け取り、thanks.htmlを表示する
	@GetMapping("/thanks")
	public String thanks(){
		return "thanks";
	}
	
	
	
}
