package com.example.hotelreservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// ===== roomsテーブルに対応するEntity =====

@Entity
@Table(name = "rooms")
public class Room {

    // ===== テーブルの列 =====

    // 主キー（DB側で自動採番）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 部屋タイプとの関連付け
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;
    
 // 部屋番号（重複不可）
    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;


    // ===== コンストラクタ =====

    public Room() {

    }


    // ===== Getter：保持している値を取得する =====

    public Integer getId() {
        return id;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }


    // ===== Setter：受け取った値を設定する =====

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

}