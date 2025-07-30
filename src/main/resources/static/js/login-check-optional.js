document.addEventListener("DOMContentLoaded", async function () {
    try {
        const statusRes = await fetch("/api/auth/status", {
            method: "GET",
            credentials: "include"
        });

        if (statusRes.status === 200) {
            const result = await statusRes.json();
            window.isLoggedIn = result.loggedIn;
            if (!window.isLoggedIn) throw new Error("Not authenticated");
            return;
        }

        if (statusRes.status === 401) {
            const refreshRes = await fetch("/api/auth/token/refresh", {
                method: "POST",
                credentials: "include"
            });

            if (refreshRes.ok) {
                location.reload(); // access 재발급 성공
            } else {
                throw new Error("Refresh token invalid");
            }
        }
    } catch (e) {
        window.isLoggedIn = false;

        // confirm 창 띄우고 선택
        if (confirm("로그인이 필요합니다. 로그인 페이지로 이동할까요?")) {
            window.location.href = "/login";
        }
    }
});
