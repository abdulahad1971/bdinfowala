package com.bd.bdinfowala.view.auth.seassion;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "MyAppSession";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ROLE = "role";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Save session
    public void saveSession(String token, String role, String email) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Check if logged in
    public boolean isLoggedIn() {
        return prefs.getString(KEY_TOKEN, null) != null;
    }

    // Getters
    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public String getRole() {
        return prefs.getString(KEY_ROLE, null);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    // Logout / Clear session
    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}

