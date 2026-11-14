package com.example.hotelreservation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

// ===== reservationsテーブルに対応するEntity =====

@Entity
@Table(name = "reservations")
public class Reservation {

    // ===== テーブルの列 =====

    // 主キー（DB側で自動採番）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 予約したお客様との関連付け
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // 自動割り当てされた実際の客室との関連付け
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // 宿泊プランとの関連付け
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    // 宿泊人数
    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests;

    // チェックイン日
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    // チェックアウト日
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    // 宿泊料金の合計
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    // 予約登録日時
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // お客様からのご要望
    @Column(name = "requests", columnDefinition = "TEXT")
    private String requests;


    // ===== コンストラクタ =====

    public Reservation() {

    }


    // ===== 登録日時を自動設定する処理 =====

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }


    // ===== Getter：保持している値を取得する =====

    public Integer getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public Plan getPlan() {
        return plan;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getRequests() {
        return requests;
    }


    // ===== Setter：受け取った値を設定する =====

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setRequests(String requests) {
        this.requests = requests;
    }

}