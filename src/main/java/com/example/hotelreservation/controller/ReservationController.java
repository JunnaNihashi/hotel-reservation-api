
package com.example.hotelreservation.controller;

// 日付を扱う
import java.time.LocalDate;
// 複数のデータを扱う
import java.util.List;
// JSONのデータを組み立てる
import java.util.Map;

//フォームのバリデーションを実行する
import jakarta.validation.Valid;

// 日付パラメータをLocalDateに変換する
import org.springframework.format.annotation.DateTimeFormat;
// Controllerとして登録する
import org.springframework.stereotype.Controller;
// HTMLへデータを渡す
import org.springframework.ui.Model;
//バリデーション結果を受け取る
import org.springframework.validation.BindingResult;
// GETリクエストを受け取る
import org.springframework.web.bind.annotation.GetMapping;
// フォームの情報を受け取る
import org.springframework.web.bind.annotation.ModelAttribute;
// POSTリクエストを受け取る
import org.springframework.web.bind.annotation.PostMapping;
// URLのパラメータを受け取る
import org.springframework.web.bind.annotation.RequestParam;
// JSONデータを返す
import org.springframework.web.bind.annotation.ResponseBody;
// リダイレクト先へ一時的にデータを渡す
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// 予約情報のEntity
import com.example.hotelreservation.entity.Reservation;
// 部屋タイプのEntity
import com.example.hotelreservation.entity.RoomType;
// 予約フォーム
import com.example.hotelreservation.form.ReservationForm;
// 宿泊プランのRepository
import com.example.hotelreservation.repository.PlanRepository;
// 部屋タイプのRepository
import com.example.hotelreservation.repository.RoomTypeRepository;
// 予約処理を担当するService
import com.example.hotelreservation.service.ReservationService;

@Controller
public class ReservationController {

    // ===== 使用するServiceとRepository =====

    // 予約登録・空室確認を担当
    private final ReservationService reservationService;

    // 部屋タイプ一覧の取得に使用
    private final RoomTypeRepository roomTypeRepository;

    // 宿泊プラン一覧の取得に使用
    private final PlanRepository planRepository;

    // ===== コンストラクタ =====

    // Spring Bootから必要なオブジェクトを受け取る
    public ReservationController(
            ReservationService reservationService,
            RoomTypeRepository roomTypeRepository,
            PlanRepository planRepository) {

        this.reservationService = reservationService;
        this.roomTypeRepository = roomTypeRepository;
        this.planRepository = planRepository;
    }

    // ===== 予約画面の表示 =====


    // GET / でトップページを表示する
    @GetMapping("/")
    public String showPage(Model model) {

        // 予約フォーム用の空のオブジェクトを作成してHTMLへ渡す
        model.addAttribute(
                "reservationForm",
                new ReservationForm()
        );

        // DBから部屋タイプ一覧を取得してHTMLへ渡す
        model.addAttribute(
                "roomTypes",
                roomTypeRepository.findAll()
        );

        // DBから宿泊プラン一覧を取得してHTMLへ渡す
        model.addAttribute(
                "plans",
                planRepository.findAll()
        );

        // Thymeleafでindex.htmlを表示する
        return "index";
    }


    // ===== 予約フォームの送信 =====



@PostMapping("/reservations")
public String saveReservation(
        // 入力値を受け取り、アノテーションによる検証を実行
        @Valid @ModelAttribute("reservationForm") ReservationForm form,

        // 入力エラーを管理
        BindingResult bindingResult,

        // HTMLに必要なデータを渡す
        Model model,

        // 予約成功時の情報を渡す
        RedirectAttributes redirectAttributes) {
	
	// チェックイン日が入力されている場合
	if (form.getCheckin() != null) {

	    // チェックイン日が今日より前か確認
	    if (form.getCheckin().isBefore(LocalDate.now())) {

	        // チェックイン日の入力エラーとして登録
	        bindingResult.rejectValue(
	                "checkin",
	                "pastDate",
	                "過去の日付は選択できません。"
	        );
	    }
	}

    // チェックイン日とチェックアウト日が入力されている場合
    if (form.getCheckin() != null && form.getCheckout() != null) {

        // チェックアウト日がチェックイン日より後でない場合
        if (!form.getCheckout().isAfter(form.getCheckin())) {

            // チェックアウト日の入力エラーとして登録
            bindingResult.rejectValue(
                    "checkout",
                    "invalidDate",
                    "チェックアウト日はチェックイン日より後にしてください。"
            );
        }
    }

    // 部屋タイプと宿泊人数が入力されている場合
    if (form.getRoomTypeId() != null && form.getNumberOfGuests() != null) {

        try {
            // Serviceの定員チェックを実行
            reservationService.validateGuests(
                    form.getRoomTypeId(),
                    form.getNumberOfGuests()
            );

        } catch (IllegalArgumentException e) {

            // 宿泊人数の入力エラーとして登録
            bindingResult.rejectValue(
                    "numberOfGuests",
                    "invalidGuests",
                    e.getMessage()
            );
        }
    }

    // 入力エラーがある場合
    if (bindingResult.hasErrors()) {

        // 部屋タイプの選択肢を再取得
        model.addAttribute("roomTypes", roomTypeRepository.findAll());

        // 宿泊プランの選択肢を再取得
        model.addAttribute("plans", planRepository.findAll());

        // 入力画面を再表示
        return "index";
    }

    // 入力内容に問題がなければ予約を登録
    Reservation reservation = reservationService.createReservation(form);

    // 予約IDを完了画面に渡す
    redirectAttributes.addFlashAttribute(
            "reservationId",
            reservation.getId()
    );

    // 予約完了画面へ移動
    return "redirect:/thanks";
}




    // ===== 予約完了画面の表示 =====

    // GET /thanks で予約完了画面を表示する
    @GetMapping("/thanks")
    public String thanks(
            // リダイレクト時に渡された予約IDを受け取る
            @ModelAttribute("reservationId") Integer reservationId,

            // Thymeleafへデータを渡すためのModel
            Model model) {

        // 予約IDがない場合は、予約情報なしで画面を表示する
        if (reservationId == null) {
            return "thanks";
        }

        // DBから予約情報と関連する顧客・客室・プランを取得する
        Reservation reservation =
                reservationService.getReservationDetails(reservationId);

        // 取得した予約情報をThymeleafへ渡す
        model.addAttribute("reservation", reservation);

        // thanks.htmlを表示する
        return "thanks";
    }


    // ===== 日程選択時の空室確認API =====

    // JavaScriptからGETリクエストを受け取る
    @GetMapping("/api/rooms/availability")

    // HTMLではなくJSONデータを返す
    @ResponseBody
    public Map<String, Object> checkAvailability(

            // URLのcheckInを受け取りLocalDateに変換する
            @RequestParam("checkIn")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkInDate,

            // URLのcheckOutを受け取りLocalDateに変換する
            @RequestParam("checkOut")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkOutDate) {

        // 宿泊日程が正しいかServiceで確認する
        reservationService.calculateNights(
                checkInDate,
                checkOutDate
        );

        // DBからすべての部屋タイプを取得する
        List<RoomType> roomTypes = roomTypeRepository.findAll();

        // 各部屋タイプの空室状況を確認する
        List<Map<String, Object>> results = roomTypes.stream()
                .map(roomType -> {

                    // Serviceで空室の有無を判定する
                    boolean available = reservationService.isAvailable(
                            roomType.getId(),
                            checkInDate,
                            checkOutDate
                    );

                    // 部屋タイプIDと空室状況をまとめる
                    return Map.<String, Object>of(
                            "roomTypeId", roomType.getId(),
                            "available", available
                    );
                })
                .toList();

        // JavaScriptにJSONとして返す
        return Map.of("roomTypes", results);
    }
}
