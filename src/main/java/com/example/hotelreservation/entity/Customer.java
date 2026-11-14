package com.example.hotelreservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// ===== customersテーブルに対応するEntity =====

@Entity
@Table(name = "customers")
public class Customer {

    // ===== テーブルの列 =====

    // 主キー（DB側で自動採番）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // お客様の名前
    @Column(name = "name", nullable = false)
    private String name;

    // 電話番号
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    // メールアドレス
    @Column(name = "email", nullable = false)
    private String email;

    // 住所
    @Column(name = "address", nullable = false)
    private String address;


    // ===== コンストラクタ =====

    public Customer() {

    }


    // ===== Getter：保持している値を取得する =====

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }


    // ===== Setter：受け取った値を設定する =====

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}