
package com.example.hotelreservation.repository;

// 日付を扱うクラス
import java.time.LocalDate;
// 検索結果が存在しない可能性を扱う
import java.util.Optional;

// JPAの基本的なCRUD機能を利用
import org.springframework.data.jpa.repository.JpaRepository;
// JPQLを指定する
import org.springframework.data.jpa.repository.Query;
// JPQLのパラメータを指定する
import org.springframework.data.repository.query.Param;

// 予約情報のEntity
import com.example.hotelreservation.entity.Reservation;

// reservationsテーブルのデータ操作を担当するインターフェース
public interface ReservationRepository
        extends JpaRepository<Reservation, Integer> {

    // ===== 宿泊期間の重複確認 =====

    // 指定した部屋に、宿泊期間が重複する予約が存在するか調べる
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Reservation r
        WHERE r.room.id = :roomId
          AND r.checkInDate < :checkOutDate
          AND r.checkOutDate > :checkInDate
    """)
    boolean existsOverlappingReservation(
            @Param("roomId") Integer roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );

    // ===== 予約完了画面用の情報取得 =====

    // 予約IDを指定して、関連する情報もまとめて取得する
    @Query("""
        SELECT r
        FROM Reservation r
        JOIN FETCH r.customer
        JOIN FETCH r.room room
        JOIN FETCH room.roomType
        JOIN FETCH r.plan
        WHERE r.id = :reservationId
    """)
    Optional<Reservation> findByIdWithDetails(
            @Param("reservationId") Integer reservationId
    );
}
