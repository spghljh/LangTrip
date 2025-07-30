document.addEventListener("DOMContentLoaded", async function () {
    console.log("✅ 로그인 상태 JS 실행됨");

    try {
        const res = await fetch("/api/auth/status", {
            method: "GET",
            credentials: "include",
            cache: "no-store",
            headers: {
                "Cache-Control": "no-cache",
                "Pragma": "no-cache"
            }
        });

        const result = await res.json();
        console.log("✅ 로그인 상태 응답:", result);

        if (result.loggedIn) {
            document.body.classList.add("logged-in");
            document.body.classList.remove("logged-out");

            const userNameEl = document.getElementById("user-name");
            if (userNameEl) {
                userNameEl.textContent = result.nickname || "사용자";
            }
        } else {
            document.body.classList.add("logged-out");
            document.body.classList.remove("logged-in");
        }
    } catch (e) {
        console.error("❌ 로그인 상태 확인 실패:", e);
        // fallback 처리: 로그아웃 상태로 보이도록
        document.body.classList.add("logged-out");
        document.body.classList.remove("logged-in");
    }
});
