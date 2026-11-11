
/* =========================================
   MotorSure - Frontend JavaScript
   Login, Registration and API Integration
========================================= */


/* 1. BACKEND API CONFIGURATION */

const API_BASE_URL = "http://localhost:8080/api/auth";

const TOKEN_KEY = "motorsure_token";


/* 2. GET HTML ELEMENTS */

const loginForm = document.getElementById("login-form");
const registerForm = document.getElementById("register-form");

const loginTab = document.getElementById("login-tab");
const registerTab = document.getElementById("register-tab");

const title = document.getElementById("form-title");
const subtitle = document.getElementById("form-subtitle");
const eyebrow = document.getElementById("form-eyebrow");
const notice = document.getElementById("notice");


/* 3. DISPLAY MESSAGE */

function showNotice(message, type = "info") {
    notice.className = `alert alert-${type}`;
    notice.textContent = message;
}


/* 4. CLEAR MESSAGE */

function clearNotice() {
    notice.className = "alert d-none";
    notice.textContent = "";
}


/* 5. SWITCH BETWEEN LOGIN AND REGISTRATION */

function switchForm(mode) {
    clearNotice();

    const isLogin = mode === "login";

    loginForm.classList.toggle("d-none", !isLogin);
    registerForm.classList.toggle("d-none", isLogin);

    loginTab.classList.toggle("active", isLogin);
    registerTab.classList.toggle("active", !isLogin);

    loginTab.setAttribute("aria-selected", String(isLogin));
    registerTab.setAttribute("aria-selected", String(!isLogin));

    eyebrow.textContent = isLogin ? "WELCOME BACK" : "GET STARTED";

    title.textContent = isLogin
        ? "Sign in to your account"
        : "Create your account";

    subtitle.textContent = isLogin
        ? "Enter your details to continue to your dashboard."
        : "Create an account to manage your motor insurance claims.";
}


/* 6. TAB BUTTON EVENTS */

loginTab.addEventListener("click", function () {
    switchForm("login");
});

registerTab.addEventListener("click", function () {
    switchForm("register");
});

document.getElementById("go-register")
    .addEventListener("click", function () {
        switchForm("register");
    });

document.getElementById("go-login")
    .addEventListener("click", function () {
        switchForm("login");
    });


/* 7. FORM VALIDATION */

function validateForm(form) {
    form.classList.add("was-validated");
    return form.checkValidity();
}


/* 8. BUTTON LOADING STATE */

function setFormLoading(form, isLoading, loadingText) {
    const button = form.querySelector('button[type="submit"]');

    if (!button) {
        return;
    }

    if (isLoading) {
        button.dataset.originalText = button.textContent.trim();
        button.disabled = true;
        button.textContent = loadingText;
    } else {
        button.disabled = false;

        if (button.dataset.originalText) {
            button.textContent = button.dataset.originalText;
        }
    }
}


/* 9. READ API RESPONSE SAFELY */

async function readResponse(response) {
    const responseText = await response.text();

    if (!responseText) {
        return {};
    }

    try {
        return JSON.parse(responseText);
    } catch {
        return { message: responseText };
    }
}


/* 10. GET A USEFUL ERROR MESSAGE */

function getErrorMessage(data, fallbackMessage) {
    if (typeof data.message === "string" && data.message.trim()) {
        return data.message;
    }

    if (typeof data.error === "string" && data.error.trim()) {
        return data.error;
    }

    if (data.errors && typeof data.errors === "object") {
        return Object.values(data.errors).join(" ");
    }

    return fallbackMessage;
}


/* 11. LOGIN FORM SUBMISSION */

loginForm.addEventListener("submit", async function (event) {
    event.preventDefault();
    clearNotice();

    if (!validateForm(loginForm)) {
        return;
    }

    const email = document.getElementById("login-email").value.trim();
    const password = document.getElementById("login-password").value;

    setFormLoading(loginForm, true, "Signing in...");

    try {
        const response = await fetch(`${API_BASE_URL}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        const data = await readResponse(response);

        if (!response.ok) {
            throw new Error(
                getErrorMessage(data, "Login failed. Check your credentials.")
            );
        }

        // Support common token property names.
        const token =
            data.token ||
            data.accessToken ||
            data.access_token ||
            data.jwt;

        if (typeof token !== "string" || !token.trim()) {
            throw new Error(
                "Login response did not contain a JWT token. Check the backend response format."
            );
        }

        // Store the token for authenticated API requests.
        localStorage.setItem(TOKEN_KEY, token);

        showNotice("Login successful. Opening dashboard...", "success");

// Fetch logged-in user's profile
const profileResponse = await fetch(
    "http://localhost:8080/api/users/me",
    {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }
);

const profileData = await readResponse(profileResponse);

if (!profileResponse.ok) {
    throw new Error("Unable to fetch user profile.");
}

// Redirect according to user role
if (profileData.role === "INVESTIGATOR") {
    window.location.href = "investigator_dashboard.html";

} else if (profileData.role === "CLAIMANT") {
    window.location.href = "dashboard.html";

}
else if (profileData.role === "ADMIN") {
    window.location.href = "admin_dashboard.html";
}
 else {
    throw new Error("Unknown user role. Please contact administrator.");
}

    } catch (error) {
        console.error("Login error:", error);

        showNotice(
            error instanceof TypeError
                ? "Could not connect to the backend. Check that Spring Boot is running on port 8080 and CORS is configured."
                : error.message,
            "danger"
        );

    } finally {
        setFormLoading(loginForm, false);
    }
});


/* 12. REGISTRATION FORM SUBMISSION */

registerForm.addEventListener("submit", async function (event) {
    event.preventDefault();
    clearNotice();

    if (!validateForm(registerForm)) {
        return;
    }

    const name = document.getElementById("register-name").value.trim();
    const email = document.getElementById("register-email").value.trim();
    const password = document.getElementById("register-password").value;

    setFormLoading(registerForm, true, "Creating account...");

    try {
        const response = await fetch(`${API_BASE_URL}/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                fullName: name,
                email: email,
                password: password
            })
        });

        const data = await readResponse(response);
        console.log("Login response:", data); //temporary added 

        if (!response.ok) {
            throw new Error(
                getErrorMessage(data, "Registration failed. Please try again.")
            );
        }

        showNotice(
            "Registration successful. You can now log in with your new account.",
            "success"
        );

        // Switch to login without reloading the page.
        document.getElementById("login-email").value = email;
        document.getElementById("login-password").value = "";

        registerForm.reset();
        registerForm.classList.remove("was-validated");

        switchForm("login");

        showNotice(
            "Registration successful. Please log in with your new account.",
            "success"
        );

    } catch (error) {
        console.error("Registration error:", error);

        showNotice(
            error instanceof TypeError
                ? "Could not connect to the backend. Check that Spring Boot is running on port 8080 and CORS is configured."
                : error.message,
            "danger"
        );

    } finally {
        setFormLoading(registerForm, false);
    }
});
