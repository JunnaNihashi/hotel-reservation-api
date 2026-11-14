package com.example.hotelreservation.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;



//予約フォームから送信された情報を保持するクラス
public class ReservationForm {
	
	// ===== フォームの入力項目 =====
	private String guestName;
	private String email;
	private String phone;

	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
	private LocalDate checkin;
	
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
	private LocalDate checkout;
	
	
	private Long room;
	private Long plan;
	private String requests;

	
	public ReservationForm() {
	}
	
	// ===== Getter：保持している値を "取得" する =====
	// ===== Setter：受け取った値を変数に "設定" する =====

	public String getGuestName() {
		return guestName;
	}
	
	public void setGuestName(String guestName) {
		this.guestName=guestName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email=email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone=phone;
	}
	
	public LocalDate getCheckin() {
		return checkin;
	}
	
	public void setCheckin(LocalDate checkin) {
		this.checkin=checkin;
	}
	
	public LocalDate getCheckout() {
		return checkout;
	}
	
	public void setCheckout(LocalDate checkout) {
		this.checkout=checkout;
	}
	
	public Long getRoom() {
		return room;
	}
	
	public void setRoom(Long room) {
		this.room=room;
	}
	
	public Long getPlan() {
		return plan;
	}
	
	public void setPlan(Long plan) {
		this.plan=plan;
	}
	
	public String getRequests() {
		return requests;
	}
	
	public void setRequests(String requests) {
		this.requests=requests;
	}
	
	
	

}
