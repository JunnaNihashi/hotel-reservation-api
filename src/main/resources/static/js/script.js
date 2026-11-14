
```javascript
// モバイル用ナビゲーションの開閉
const menuToggle = document.querySelector(".menu-toggle");
const globalNav = document.querySelector(".global-nav");

if (menuToggle && globalNav) {
  menuToggle.addEventListener("click", () => {
    const isExpanded =
      menuToggle.getAttribute("aria-expanded") === "true";

    menuToggle.setAttribute("aria-expanded", String(!isExpanded));
    menuToggle.setAttribute(
      "aria-label",
      isExpanded ? "メニューを開く" : "メニューを閉じる"
    );

    globalNav.classList.toggle("is-open", !isExpanded);
  });

  globalNav.querySelectorAll("a").forEach((link) => {
    link.addEventListener("click", () => {
      menuToggle.setAttribute("aria-expanded", "false");
      menuToggle.setAttribute("aria-label", "メニューを開く");
      globalNav.classList.remove("is-open");
    });
  });
}

// 予約フォームの入力チェック
const reservationForm = document.querySelector("#reservation-form");

if (reservationForm) {
	const fieldIds = [
	  "guest-name",
	  "guest-email",
	  "guest-phone",
	  "guest-address",
	  "checkin",
	  "checkout",
	  "room-choice",
	  "plan-choice"
	];

  const requests = document.querySelector("#requests");
  const count = document.querySelector("#requests-count");
  const feedback = document.querySelector("#form-feedback");

  // ご要望の文字数表示
  if (requests && count) {
    requests.addEventListener("input", () => {
      count.textContent = requests.value.length;
    });
  }

  // エラーメッセージの表示
  function setError(id, message) {
    const input = document.getElementById(id);
    const error = document.getElementById(id + "-error");

    if (!input || !error) return;

    error.textContent = message;

    if (message) {
      input.setAttribute("aria-invalid", "true");
    } else {
      input.removeAttribute("aria-invalid");
    }
  }

  // 入力内容が変更されたらエラーを解除
  fieldIds.forEach((id) => {
    const input = document.getElementById(id);

    if (!input) return;

    input.addEventListener("input", () => setError(id, ""));
    input.addEventListener("change", () => setError(id, ""));
  });

  // 予約フォーム送信時の処理
  reservationForm.addEventListener("submit", (event) => {
    feedback.textContent = "";
    fieldIds.forEach((id) => setError(id, ""));

    const name = document.getElementById("guest-name").value.trim();
    const email = document.getElementById("guest-email").value.trim();
    const phone = document.getElementById("guest-phone").value.trim();
	const address = document.getElementById("guest-address").value.trim();
    const checkin = document.getElementById("checkin").value;
    const checkout = document.getElementById("checkout").value;
    const room = document.getElementById("room-choice").value;
    const plan = document.getElementById("plan-choice").value;

    if (!name) {
      setError("guest-name", "お名前を入力してください。");
    }

    if (!email) {
      setError("guest-email", "メールアドレスを入力してください。");
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      setError("guest-email", "正しいメールアドレスを入力してください。");
    }

    if (!phone) {
      setError("guest-phone", "電話番号を入力してください。");
    } else {
      const digits = phone.replace(/-/g, "");

      if (!/^0\d{9,10}$/.test(digits) ||
          !/^[0-9-]+$/.test(phone)) {
        setError(
          "guest-phone",
          "電話番号は数字とハイフンで10〜11桁の形式で入力してください。"
        );
      }
    }
	
	// 住所の入力チェック
	if (!address) {

	  setError("guest-address", "住所を入力してください。");

	}

    if (!checkin) {
      setError("checkin", "チェックイン日を選択してください。");
    }

    if (!checkout) {
      setError("checkout", "チェックアウト日を選択してください。");
    } else if (checkin && checkout <= checkin) {
      setError(
        "checkout",
        "チェックアウト日はチェックイン日より後の日付にしてください。"
      );
    }

    if (!room) {
      setError("room-choice", "お部屋を選択してください。");
    }

    if (!plan) {
      setError("plan-choice", "宿泊プランを選択してください。");
    }

    if (requests.value.length > 500) {
      feedback.textContent =
        "ご要望は500文字以内で入力してください。";
    }

    const firstInvalid = fieldIds
      .map((id) => document.getElementById(id))
      .find((input) =>
        input.getAttribute("aria-invalid") === "true"
      );

    const hasError =
      Boolean(firstInvalid) || requests.value.length > 500;

    if (hasError) {
      // エラーがあれば送信しない
      event.preventDefault();

      if (firstInvalid) {
        firstInvalid.focus();
      }
      return;
    }

    // 入力OKならHTMLの通常のフォーム送信を実行
    // Spring BootのControllerが受け取る
  });
}
```
