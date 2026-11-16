
package com.example.hotelreservation.form;

import java.time.LocalDate;

// 入力値のバリデーションに使用するアノテーション
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// HTMLフォームの日付文字列をLocalDateに変換するため
import org.springframework.format.annotation.DateTimeFormat;

public class ReservationForm {

    // お名前：必須・100文字以内
    @NotBlank(message = "お名前を入力してください。")
    @Size(max = 100, message = "お名前は100文字以内で入力してください。")
    private String guestName;

    // メールアドレス：必須・メール形式
    @NotBlank(message = "メールアドレスを入力してください。")
    @Email(message = "正しいメールアドレスを入力してください。")
    private String email;

    // 電話番号：必須・数字とハイフンのみ
    // ハイフンを除いた数字が0から始まる10〜11桁
    @NotBlank(message = "電話番号を入力してください。")
    @Pattern(
        regexp = "(?=(?:[^0-9]*[0-9]){10,11}[^0-9]*$)0[0-9-]*",
        message = "電話番号は数字とハイフンで10〜11桁の形式で入力してください。"
    )
    private String phone;

    // 住所：必須・200文字以内
    @NotBlank(message = "住所を入力してください。")
    @Size(max = 200, message = "住所は200文字以内で入力してください。")
    private String address;

    // チェックイン日：必須
    @NotNull(message = "チェックイン日を選択してください。")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkin;

    // チェックアウト日：必須
    @NotNull(message = "チェックアウト日を選択してください。")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkout;

    // 部屋タイプ：必須
    @NotNull(message = "お部屋を選択してください。")
    private Integer roomTypeId;

    // 宿泊プラン：必須
    @NotNull(message = "宿泊プランを選択してください。")
    private Integer plan;

    // 宿泊人数：必須・1〜6名
    @NotNull(message = "宿泊人数を選択してください。")
    @Min(value = 1, message = "宿泊人数は1名以上にしてください。")
    @Max(value = 6, message = "宿泊人数は6名以内にしてください。")
    private Integer numberOfGuests;

    // ご要望：500文字以内（未入力は許可）
    @Size(max = 500, message = "ご要望は500文字以内で入力してください。")
    private String requests;

    // ========================================
    // getter・setter
    // ========================================

    // お名前を取得
    public String getGuestName() {
        return guestName;
    }

    // お名前を設定
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    // メールアドレスを取得
    public String getEmail() {
        return email;
    }

    // メールアドレスを設定
    public void setEmail(String email) {
        this.email = email;
    }

    // 電話番号を取得
    public String getPhone() {
        return phone;
    }

    // 電話番号を設定
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // 住所を取得
    public String getAddress() {
        return address;
    }

    // 住所を設定
    public void setAddress(String address) {
        this.address = address;
    }

    // チェックイン日を取得
    public LocalDate getCheckin() {
        return checkin;
    }

    // チェックイン日を設定
    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    // チェックアウト日を取得
    public LocalDate getCheckout() {
        return checkout;
    }

    // チェックアウト日を設定
    public void setCheckout(LocalDate checkout) {
        this.checkout = checkout;
    }

    // 部屋タイプIDを取得
    public Integer getRoomTypeId() {
        return roomTypeId;
    }

    // 部屋タイプIDを設定
    public void setRoomTypeId(Integer roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    // 宿泊プランIDを取得
    public Integer getPlan() {
        return plan;
    }

    // 宿泊プランIDを設定
    public void setPlan(Integer plan) {
        this.plan = plan;
    }

    // 宿泊人数を取得
    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    // 宿泊人数を設定
    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    // ご要望を取得
    public String getRequests() {
        return requests;
    }

    // ご要望を設定
    public void setRequests(String requests) {
        this.requests = requests;
    }
}
