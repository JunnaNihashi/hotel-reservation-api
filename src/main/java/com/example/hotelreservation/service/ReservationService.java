
package com.example.hotelreservation.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hotelreservation.entity.Customer;
import com.example.hotelreservation.entity.Plan;
import com.example.hotelreservation.entity.Reservation;
import com.example.hotelreservation.entity.Room;
import com.example.hotelreservation.entity.RoomType;
import com.example.hotelreservation.form.ReservationForm;
import com.example.hotelreservation.repository.CustomerRepository;
import com.example.hotelreservation.repository.PlanRepository;
import com.example.hotelreservation.repository.ReservationRepository;
import com.example.hotelreservation.repository.RoomRepository;
import com.example.hotelreservation.repository.RoomTypeRepository;

@Service
public class ReservationService {

    // ===== DBにアクセスするRepository =====

    private final CustomerRepository customerRepository;
    private final PlanRepository planRepository;
    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final ReservationRepository reservationRepository;

    // ===== コンストラクタ =====

    // Spring Bootから各Repositoryを受け取る
    public ReservationService(
            CustomerRepository customerRepository,
            PlanRepository planRepository,
            RoomRepository roomRepository,
            RoomTypeRepository roomTypeRepository,
            ReservationRepository reservationRepository) {

        this.customerRepository = customerRepository;
        this.planRepository = planRepository;
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.reservationRepository = reservationRepository;
    }

    // ===== 宿泊日数の計算 =====

    public long calculateNights(
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        // 日付が入力されているか確認
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("宿泊日を入力してください");
        }
     // チェックイン日が過去の日付か確認
        if (checkInDate.isBefore(LocalDate.now())) {

            // 過去の日付の場合は例外を発生させる
            throw new IllegalArgumentException("過去の日付では予約できません");

        }
        // チェックアウト日がチェックイン日より後か確認
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("宿泊日を正しく指定してください");
        }

        // 2つの日付の差を宿泊日数として返す
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    // ===== 宿泊人数の確認 =====

    public void validateGuests(Integer roomTypeId, int guests) {

        // 1名以上か確認
        if (guests < 1) {
            throw new IllegalArgumentException("宿泊人数は1名以上です");
        }

        // 部屋タイプが選択されているか確認
        if (roomTypeId == null) {
            throw new IllegalArgumentException("部屋タイプを選択してください");
        }

        // DBから部屋タイプを取得
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("部屋タイプが見つかりません"));

        // DBに登録されている最大定員を取得
        Integer maxGuests = roomType.getCapacity();

        // 最大定員が設定されているか確認
        if (maxGuests == null || maxGuests < 1) {
            throw new IllegalStateException("部屋タイプの最大定員が設定されていません");
        }

        // 宿泊人数が最大定員を超えていないか確認
        if (guests > maxGuests) {
            throw new IllegalArgumentException(
                    "この部屋の最大定員は" + maxGuests + "名です");
        }
    }

    // ===== 宿泊料金の計算 =====

    public long calculateTotalPrice(
            int roomPrice,
            int planPrice,
            int guests,
            long nights) {

        // 1泊あたりの食事料金を計算
        long mealPrice = (long) planPrice * guests;

        // 部屋料金と食事料金を合計し、宿泊日数をかける
        return ((long) roomPrice + mealPrice) * nights;
    }

    // ===== 空いている客室の検索 =====

    // 予約登録のトランザクション内で使用する
    private Room findAvailableRoom(
            Integer roomTypeId,
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        // 指定した部屋タイプの客室を取得してロック
        List<Room> rooms =
                roomRepository.findRoomsForReservation(roomTypeId);

        // 客室を1室ずつ確認
        for (Room room : rooms) {

            // 同じ期間に予約が存在するか確認
            boolean reserved =
                    reservationRepository.existsOverlappingReservation(
                            room.getId(),
                            checkInDate,
                            checkOutDate);

            // 予約がなければ、その客室を返す
            if (!reserved) {
                return room;
            }
        }

        // 空室が見つからなければエラー
        throw new IllegalStateException("希望する期間に空室がありません");
    }

    // ===== 予約登録のメイン処理 =====

    // 顧客登録と予約登録を1つのトランザクションで管理する
    @Transactional
    public Reservation createReservation(ReservationForm form) {

        // フォームが存在するか確認
        if (form == null) {
            throw new IllegalArgumentException("予約情報がありません");
        }

        // フォームから宿泊日を取得
        LocalDate checkInDate = form.getCheckin();
        LocalDate checkOutDate = form.getCheckout();

        // フォームから部屋タイプIDを取得
        Integer roomTypeId = form.getRoomTypeId();

        // フォームからプランIDを取得
        Integer planId = form.getPlan();

        // フォームから宿泊人数を取得
        Integer guests = form.getNumberOfGuests();

        // 宿泊日数を計算し、日付も確認
        long nights = calculateNights(checkInDate, checkOutDate);

        // 宿泊人数が入力されているか確認
        if (guests == null) {
            throw new IllegalArgumentException("宿泊人数を選択してください");
        }

        // 宿泊人数が定員内か確認
        validateGuests(roomTypeId, guests);

        // プランが選択されているか確認
        if (planId == null) {
            throw new IllegalArgumentException("宿泊プランを選択してください");
        }

        // DBから部屋タイプを取得
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("部屋タイプが見つかりません"));

        // DBから宿泊プランを取得
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() ->
                        new IllegalArgumentException("宿泊プランが見つかりません"));

        // 空いている客室を検索して確保
        Room room = findAvailableRoom(
                roomTypeId,
                checkInDate,
                checkOutDate);

        // 宿泊料金を計算
        long totalPrice = calculateTotalPrice(
                roomType.getPrice(),
                plan.getPrice(),
                guests,
                nights);

     // ===== 顧客情報の取得・登録 =====

     // 名前・電話番号・メールアドレスが一致する顧客を検索
     Customer customer = customerRepository
             .findByNameAndPhoneNumberAndEmail(
                     form.getGuestName(),
                     form.getPhone(),
                     form.getEmail()
             )

             // 既存顧客が見つからない場合だけ新しく作成する
             .orElseGet(() -> {

                 // 新しい顧客インスタンスを作成
                 Customer newCustomer = new Customer();

                 // フォームの名前を設定
                 newCustomer.setName(form.getGuestName());

                 // フォームの電話番号を設定
                 newCustomer.setPhoneNumber(form.getPhone());

                 // フォームのメールアドレスを設定
                 newCustomer.setEmail(form.getEmail());

                 // フォームの住所を設定
                 newCustomer.setAddress(form.getAddress());

                 // 新しい顧客をDBに保存して返す
                 return customerRepository.save(newCustomer);

             });

     // 既存顧客または新規登録した顧客を予約に使用
     Customer savedCustomer = customer;

        // ===== 予約情報の登録 =====

        // 新しいReservationインスタンスを作成
        Reservation reservation = new Reservation();

        // 保存した顧客情報を予約に設定
        reservation.setCustomer(savedCustomer);

        // 空室から選ばれた客室を設定
        reservation.setRoom(room);

        // 宿泊プランを設定
        reservation.setPlan(plan);

        // 宿泊人数を設定
        reservation.setNumberOfGuests(guests);

        // チェックイン日を設定
        reservation.setCheckInDate(checkInDate);

        // チェックアウト日を設定
        reservation.setCheckOutDate(checkOutDate);


        // 合計料金を設定
        reservation.setTotalPrice(Math.toIntExact(totalPrice));

        // フォームから受け取ったご要望を設定
        reservation.setRequests(form.getRequests());

        // DBのreservationsテーブルに予約情報を保存
        return reservationRepository.save(reservation);

    }

    // ===== 空室確認API用の処理 =====

    @Transactional(readOnly = true)
    public boolean isAvailable(
            Integer roomTypeId,
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        // 宿泊日程が正しいか確認
        if (checkInDate == null || checkOutDate == null
                || !checkInDate.isBefore(checkOutDate)) {

            throw new IllegalArgumentException("宿泊日程が正しくありません。");
        }

        // 指定された部屋タイプの客室を取得
        List<Room> rooms =
                roomRepository.findRoomsByRoomTypeId(roomTypeId);

        // 客室を1室ずつ確認
        for (Room room : rooms) {

            // 予約期間が重複しているか確認
            boolean reserved =
                    reservationRepository.existsOverlappingReservation(
                            room.getId(),
                            checkInDate,
                            checkOutDate);

            // 1室でも空いていればtrueを返す
            if (!reserved) {
                return true;
            }
        }

        // 空室がなければfalseを返す
        return false;
    }
    

    // ===== 予約完了画面用の情報取得 =====

    // DBから予約IDに対応する予約情報を取得する
    @Transactional(readOnly = true)
    public Reservation getReservationDetails(Integer reservationId) {

        // 予約IDが指定されているか確認する
        if (reservationId == null) {
            throw new IllegalArgumentException("予約IDが指定されていません");
        }

        // 予約情報と関連する顧客・客室・宿泊プランを取得する
        return reservationRepository.findByIdWithDetails(reservationId)

                // 指定した予約が存在しない場合はエラーにする
                .orElseThrow(() ->
                        new IllegalArgumentException("予約情報が見つかりません"));
    }

}
