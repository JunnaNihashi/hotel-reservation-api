
package com.example.hotelreservation.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;


//予約フォームから送信された情報を保持するクラス

public class ReservationForm {

	// ===== フォームの入力項目 =====

	private String guestName;

	private String email;

	private String phone;
	
	private String address;

	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)

	private LocalDate checkin;

	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)

	private LocalDate checkout;


	private Integer roomTypeId;

	private Integer plan;

	private Integer numberOfGuests;

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
	
	public String getAddress() {

	    return address;
	}
	
	public void setAddress(String address) {

	    this.address = address;

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

	public Integer getRoomTypeId() {

		return roomTypeId;

	}

	public void setRoomTypeId(Integer roomTypeId) {

		this.roomTypeId=roomTypeId;

	}

	public Integer getPlan() {

		return plan;

	}

	public void setPlan(Integer plan) {

		this.plan=plan;

	}

	public Integer getNumberOfGuests() {

		return numberOfGuests;

	}

	public void setNumberOfGuests(Integer numberOfGuests) {

		this.numberOfGuests=numberOfGuests;

	}

	public String getRequests() {

		return requests;

	}

	public void setRequests(String requests) {

		this.requests=requests;

	}

}
