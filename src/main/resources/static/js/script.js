
"use strict";

// ========================================
// 1. モバイル用ナビゲーション
// ========================================

// メニューボタンを取得
const menuToggle = document.querySelector(".menu-toggle");

// ナビゲーションを取得
const globalNav = document.querySelector(".global-nav");

// 両方の要素が存在する場合だけ実行
if (menuToggle && globalNav) {

  // メニューボタンをクリックしたとき
  menuToggle.addEventListener("click", () => {

    // 現在メニューが開いているか確認
    const isExpanded =
      menuToggle.getAttribute("aria-expanded") === "true";

    // 開閉状態を切り替える
    menuToggle.setAttribute("aria-expanded", String(!isExpanded));

    // ボタンの説明を変更
    menuToggle.setAttribute(
      "aria-label",
      isExpanded ? "メニューを開く" : "メニューを閉じる"
    );

    // メニューの表示を切り替える
    globalNav.classList.toggle("is-open", !isExpanded);
  });

  // ナビゲーションのリンクを取得
  globalNav.querySelectorAll("a").forEach((link) => {

    // リンクを押したらメニューを閉じる
    link.addEventListener("click", () => {
      menuToggle.setAttribute("aria-expanded", "false");
      menuToggle.setAttribute("aria-label", "メニューを開く");
      globalNav.classList.remove("is-open");
    });
  });
}

// ========================================
// 2. 予約フォーム
// ========================================

// 予約フォームを取得
const reservationForm = document.querySelector("#reservation-form");

// 予約フォームが存在する場合だけ実行
if (reservationForm) {

  // ブラウザ標準の入力チェックを無効にする
  // JavaScriptでエラーメッセージを表示するため
  reservationForm.noValidate = true;

  // 入力チェックを行う項目
  const fieldIds = [
    "guest-name",
    "guest-email",
    "guest-phone",
    "guest-address",
    "checkin",
    "checkout",
    "room-choice",
    "plan-choice",
    "number-of-guests"
  ];

  // フォーム内の各要素を取得
  const requests = document.querySelector("#requests");
  const count = document.querySelector("#requests-count");
  const feedback = document.querySelector("#form-feedback");

  // チェックイン日を取得
  const checkinInput = document.querySelector("#checkin");

  // チェックアウト日を取得
  const checkoutInput = document.querySelector("#checkout");

  // 部屋タイプを取得
  const roomSelect = document.querySelector("#room-choice");

  // 宿泊人数を取得
  const guestSelect = document.querySelector("#number-of-guests");

  // 空室確認メッセージを取得
  const availabilityMessage =
    document.querySelector("#availability-message");

  // 予約送信ボタンを取得
  const submitButton =
    reservationForm.querySelector('button[type="submit"]');

  // 空室確認の成功状態を保存
  let availabilityConfirmed = false;

  // 最後に確認した日程を保存
  let confirmedDates = "";

  // 古い通信を区別する番号
  let availabilityRequestId = 0;

  // ========================================
  // 3. 入力エラーの表示
  // ========================================

  // 指定した入力欄にエラーを表示する関数
  function setError(id, message) {

    // 入力欄を取得
    const input = document.getElementById(id);

    // エラー表示欄を取得
    const error = document.getElementById(id + "-error");

    // 要素がなければ処理を終了
    if (!input || !error) return;

    // エラーメッセージを表示
    error.textContent = message;

    // エラーがある場合
    if (message) {

      // 入力欄にエラー状態を設定
      input.setAttribute("aria-invalid", "true");

    } else {

      // エラーがなければ状態を解除
      input.removeAttribute("aria-invalid");
    }
  }

  // サーバー側で表示されたエラーを初期状態に反映
  fieldIds.forEach((id) => {

    // 入力欄を取得
    const input = document.getElementById(id);

    // エラー表示欄を取得
    const error = document.getElementById(id + "-error");

    // エラーメッセージが存在する場合
    if (input && error && error.textContent.trim()) {
      input.setAttribute("aria-invalid", "true");
    }
  });

  // 入力内容が変更されたら、その項目のエラーを解除
  fieldIds.forEach((id) => {

    // 入力欄を取得
    const input = document.getElementById(id);

    // 存在しない場合はスキップ
    if (!input) return;

    // 文字を入力した場合
    input.addEventListener("input", () => {
      setError(id, "");
    });

    // 選択内容を変更した場合
    input.addEventListener("change", () => {
      setError(id, "");
    });
  });

  // ========================================
  // 4. ご要望の文字数表示
  // ========================================

  // 文字数表示を更新する関数
  function updateRequestCount() {

    // 必要な要素が存在しなければ終了
    if (!requests || !count) return;

    // 現在の文字数を表示
    count.textContent = requests.value.length;
  }

  // ご要望欄が存在する場合
  if (requests) {

    // 入力するたびに文字数を更新
    requests.addEventListener("input", () => {

      // 文字数を更新
      updateRequestCount();

      // ご要望のエラーを取得
      const error = document.getElementById("requests-error");

      // 入力内容が修正されたらエラーを解除
      if (error) {
        error.textContent = "";
      }
    });
  }

  // ========================================
  // 5. 宿泊人数の制限
  // ========================================

  // 選択中の部屋タイプの最大定員を取得
  function getRoomCapacity() {

    // 選択中の部屋タイプを取得
    const selectedOption = roomSelect.selectedOptions[0];

    // 部屋タイプが未選択ならnullを返す
    if (!selectedOption || !roomSelect.value) {
      return null;
    }

    // HTMLのdata-capacity属性から定員を取得
    const capacity = Number(selectedOption.dataset.capacity);

    // 正しい定員なら返し、そうでなければnull
    return Number.isInteger(capacity) && capacity > 0
      ? capacity
      : null;
  }

  // 宿泊人数が部屋の定員を超えていないか確認
  function validateGuestCapacity() {

    // 選択中の部屋の最大定員を取得
    const capacity = getRoomCapacity();

    // 現在選択している宿泊人数を取得
    const guests = Number(guestSelect.value);

    // 部屋または人数が未選択なら終了
    if (!roomSelect.value || !guestSelect.value) {
      setError("number-of-guests", "");
      return true;
    }

    // 定員が取得できない場合
    if (capacity === null) {

      setError(
        "number-of-guests",
        "お部屋の定員を確認できません。"
      );

      return false;
    }

    // 最大定員を超えている場合
    if (guests > capacity) {

      // エラーメッセージを表示
      setError(
        "number-of-guests",
        `選択されたお部屋の定員は${capacity}名です。宿泊人数を変更してください。`
      );

      return false;
    }

    // 定員内であればエラーを解除
    setError("number-of-guests", "");

    return true;
  }

  // 部屋タイプに合わせて人数の状態を更新
  function updateGuestOptions() {

    // すべての人数を選択できる状態にする
    // 定員超過はエラーメッセージで知らせる
    Array.from(guestSelect.options).forEach((option) => {
      option.disabled = false;
    });

    // 現在の宿泊人数をチェック
    validateGuestCapacity();
  }

  // 部屋タイプを変更したとき
  roomSelect.addEventListener("change", () => {
    updateGuestOptions();
  });

  // 宿泊人数を変更したとき
  guestSelect.addEventListener("change", () => {
    validateGuestCapacity();
  });

  // ========================================
  // 6. 空室確認
  // ========================================

  // 空室確認の状態とメッセージを更新
  function updateAvailabilityMessage(message, confirmed = false) {

    // 空室確認の成功状態を保存
    availabilityConfirmed = confirmed;

    // メッセージを表示
    if (availabilityMessage) {
      availabilityMessage.textContent = message;
    }

    // 空室確認が終わるまで送信ボタンを無効化
    if (submitButton) {
      submitButton.disabled = false;
    }
  }

  // 空室確認を行う関数
  async function checkRoomAvailability() {

    // 今回の確認番号を発行
    const currentRequestId = ++availabilityRequestId;

    // 入力された日程を取得
    const checkIn = checkinInput.value;
    const checkOut = checkoutInput.value;

    // 前回の確認日程を無効にする
    confirmedDates = "";

    // 空室未確認の状態に戻す
    updateAvailabilityMessage(
      "宿泊日程を選択すると空室状況を確認できます。"
    );

    // 部屋タイプの選択肢を取得
    const options = Array.from(roomSelect.options)
      .filter((option) => option.value !== "");

    // 前回の満室表示を初期化
    options.forEach((option) => {

      // 選択不可を解除
      option.disabled = false;

      // 「満室」の文字を削除
      option.textContent =
        option.textContent.replace(/（満室）$/, "");
    });

	// 日程が未入力なら終了
	if (!checkIn || !checkOut) return;

	// チェックアウト日のエラーを解除
	setError("checkout", "");

	// 日付の前後関係を確認
	if (checkOut <= checkIn) {

	  // チェックアウト日の下にエラーを表示
	  setError(
	    "checkout",
	    "チェックアウト日はチェックイン日より後にしてください。"
	  );

	  // 日付が不正なので空室確認を終了
	  return;
	}

	// 日付が正しい場合は空室確認を続ける
	updateAvailabilityMessage("空室状況を確認しています...");
	

    try {

      // APIに送る日程を作成
      const params = new URLSearchParams({
        checkIn: checkIn,
        checkOut: checkOut
      });

      // Spring Bootの空室確認APIを呼び出す
      const response = await fetch(
        `/api/rooms/availability?${params.toString()}`
      );

      // 古い通信結果なら使用しない
      if (currentRequestId !== availabilityRequestId) return;

      // HTTPエラーを確認
      if (!response.ok) {
        throw new Error("空室確認APIでエラーが発生しました");
      }

      // JSONデータを取得
      const data = await response.json();

      // 古い通信結果なら使用しない
      if (currentRequestId !== availabilityRequestId) return;

      // 返却データの形式を確認
      if (!Array.isArray(data.roomTypes)) {
        throw new Error("空室確認データの形式が不正です");
      }

      // 部屋タイプIDと空室状況を対応させる
      const availabilityMap = new Map(
        data.roomTypes.map((room) => [
          String(room.roomTypeId),
          room.available
        ])
      );

      // 空室のある部屋タイプの数
      let availableCount = 0;

      // 各部屋タイプの空室状況を反映
      options.forEach((option) => {

        // この部屋タイプの空室状況を取得
        const available = availabilityMap.get(option.value);

        // 空室がある場合だけ選択可能
        option.disabled = available !== true;

        // 満室なら文字を追加
        if (available === false) {
          option.textContent += "（満室）";
        }

        // 空室がある場合はカウント
        if (available === true) {
          availableCount++;
        }
      });

      // 選択中の部屋タイプが予約可能か確認
      const selectedRoomUnavailable =
        roomSelect.value !== "" &&
        availabilityMap.get(roomSelect.value) !== true;

      // 選択中の部屋が満室などの場合
      if (selectedRoomUnavailable) {

        // 部屋の選択を解除
        roomSelect.value = "";

        // 人数制限も更新
        updateGuestOptions();
      }

      // 確認できた日程を保存
      confirmedDates = `${checkIn}|${checkOut}`;

      // 全室満室の場合
      if (availableCount === 0) {

        updateAvailabilityMessage(
          "この日程は全室満室です。別の日程をお選びください。",
          false
        );

      } else if (selectedRoomUnavailable) {

        updateAvailabilityMessage(
          "選択されたお部屋は予約できません。別のお部屋または日程をお選びください。",
          true
        );

      } else {

        updateAvailabilityMessage(
          "空室状況を確認しました。ご希望のお部屋をお選びください。",
          true
        );
      }

    } catch (error) {

      // 古い通信のエラーは無視
      if (currentRequestId !== availabilityRequestId) return;

      // 開発者向けにエラーを表示
      console.error(error);

      // 画面にエラーメッセージを表示
      updateAvailabilityMessage(
        "空室状況を確認できませんでした。日程を変更して再度お試しください。"
      );
    }
  }

  // チェックイン日を変更したら空室確認
  checkinInput.addEventListener("change", checkRoomAvailability);

  // チェックアウト日を変更したら空室確認
  checkoutInput.addEventListener("change", checkRoomAvailability);

  // ========================================
  // 7. 予約フォームの送信チェック
  // ========================================

  // 予約ボタンを押したとき
  reservationForm.addEventListener("submit", (event) => {

    // 前回のフォーム全体のエラーを消す
    feedback.textContent = "";

    // 各項目のエラーを解除
    fieldIds.forEach((id) => setError(id, ""));

    // 入力値を取得
    const name = document.getElementById("guest-name").value.trim();
    const email = document.getElementById("guest-email").value.trim();
    const phone = document.getElementById("guest-phone").value.trim();
    const address = document.getElementById("guest-address").value.trim();
    const checkin = checkinInput.value;
    const checkout = checkoutInput.value;
    const room = roomSelect.value;
    const plan = document.getElementById("plan-choice").value;
    const guests = guestSelect.value;

    // 名前の必須チェック
    if (!name) {
      setError("guest-name", "お名前を入力してください。");
    }

    // メールアドレスの形式チェック
    if (!email) {

      setError(
        "guest-email",
        "メールアドレスを入力してください。"
      );

    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {

      setError(
        "guest-email",
        "正しいメールアドレスを入力してください。"
      );
    }

    // 電話番号の形式チェック
    if (!phone) {

      setError(
        "guest-phone",
        "電話番号を入力してください。"
      );

    } else {

      // ハイフンを除去
      const digits = phone.replace(/-/g, "");

      // 電話番号の形式を確認
      if (!/^0\d{9,10}$/.test(digits) || !/^[0-9-]+$/.test(phone)) {

        setError(
          "guest-phone",
          "電話番号は数字とハイフンで10〜11桁の形式で入力してください。"
        );
      }
    }

    // 住所の必須チェック
    if (!address) {
      setError("guest-address", "住所を入力してください。");
    }

    // チェックイン日の必須チェック
    if (!checkin) {
      setError("checkin", "チェックイン日を選択してください。");
    }

    // チェックアウト日のチェック
    if (!checkout) {

      setError(
        "checkout",
        "チェックアウト日を選択してください。"
      );

    } else if (checkin && checkout <= checkin) {

      setError(
        "checkout",
        "チェックアウト日はチェックイン日より後の日付にしてください。"
      );
    }

    // 部屋タイプの必須チェック
    if (!room) {
      setError("room-choice", "お部屋を選択してください。");
    }

    // 宿泊プランの必須チェック
    if (!plan) {
      setError("plan-choice", "宿泊プランを選択してください。");
    }

    // 宿泊人数の必須チェック
    if (!guests) {
      setError("number-of-guests", "宿泊人数を選択してください。");
    }

    // 定員超過のチェック
    if (room && guests) {
      validateGuestCapacity();
    }

    // ご要望の文字数チェック
    if (requests && requests.value.length > 500) {

      feedback.textContent =
        "ご要望は500文字以内で入力してください。";
    }

    // 最初にエラーがある入力欄を取得
    const firstInvalid = fieldIds
      .map((id) => document.getElementById(id))
      .find((input) =>
        input.getAttribute("aria-invalid") === "true"
      );

    // 入力エラーがあるか確認
    const hasError =
      Boolean(firstInvalid) ||
      (requests && requests.value.length > 500);

    // 入力エラーがある場合は送信しない
    if (hasError) {

      event.preventDefault();

      // 最初のエラー項目へ移動
      if (firstInvalid) {
        firstInvalid.focus();
      }

      return;
    }

    // 現在の日程を取得
    const currentDates = `${checkin}|${checkout}`;

    // 空室確認が未完了なら送信しない
    if (!availabilityConfirmed || confirmedDates !== currentDates) {

      event.preventDefault();

      feedback.textContent =
        "空室状況を確認できていません。日程を再度選択してください。";

      return;
    }

    // 選択中の部屋タイプを取得
    const selectedRoomOption = roomSelect.selectedOptions[0];

    // 選択中の部屋が予約不可なら送信しない
    if (!selectedRoomOption || selectedRoomOption.disabled) {

      event.preventDefault();

      feedback.textContent =
        "選択されたお部屋は予約できません。";

      return;
    }

    // すべて問題なければ通常のPOST送信を実行
    // Spring BootのControllerが予約情報を受け取る
  });

  // ========================================
  // 8. 初期表示
  // ========================================

  // ご要望の文字数を初期表示
  updateRequestCount();

  // 初期状態の人数チェックを反映
  // サーバー側のエラー表示を消さないように、
  // 部屋と人数が両方選択されている場合だけ実行
  if (roomSelect.value && guestSelect.value) {
    validateGuestCapacity();
  }

  // 入力済みの日程がある場合は空室確認を実行
  // サーバー側のエラーでフォームに戻った場合にも対応
  if (checkinInput.value && checkoutInput.value) {

    checkRoomAvailability();

  } else {

    // 日程が未入力なら空室未確認にする
    updateAvailabilityMessage(
      "宿泊日程を選択すると空室状況を確認できます。"
    );
  }
  
  // ========================================
  // サーバー側のバリデーションエラー表示
  // ========================================

  // エラーメッセージが表示されている入力欄を探す
  const serverErrorField = fieldIds
    .map((id) => document.getElementById(id))
    .find((input) => {
      if (!input) return false;

      // 対応するエラーメッセージ欄を取得
      const error = document.getElementById(input.id + "-error");

      // エラーメッセージが存在するか確認
      return error && error.textContent.trim() !== "";
    });

  // サーバー側のエラーがある場合
  if (serverErrorField) {

    // エラーのある入力欄へスクロール
    serverErrorField.scrollIntoView({
      behavior: "smooth",
      block: "center"
    });

    // 入力欄をフォーカス
    serverErrorField.focus({
      preventScroll: true
    });
  }
}
