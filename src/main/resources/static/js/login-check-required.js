document.addEventListener("DOMContentLoaded", async function () {
    try {
        const statusRes = await fetch("/api/auth/status", {
            method: "GET",
            credentials: "include"
        });

        if (statusRes.status === 200) {
            const result = await statusRes.json();
            if (!result.loggedIn) throw new Error("Not authenticated");
            return;
        }

        if (statusRes.status === 401) {
            const refreshRes = await fetch("/api/auth/token/refresh", {
                method: "POST",
                credentials: "include"
            });

            if (refreshRes.ok) {
                location.reload(); // 토큰 재발급 성공 → 재요청
            } else {
                throw new Error("Refresh token invalid");
            }
        }
    } catch (e) {
        // confirm 없이 무조건 이동
        window.location.href = "/login";
    }
});
