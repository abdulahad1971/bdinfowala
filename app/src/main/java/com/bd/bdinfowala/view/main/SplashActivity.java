package com.bd.bdinfowala.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bd.bdinfowala.MainActivity;
import com.bd.bdinfowala.R;
import com.bd.bdinfowala.admin.AdminActivity;
import com.bd.bdinfowala.view.auth.LoginActivity;
import com.bd.bdinfowala.view.auth.seassion.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2 সেকেন্ডের পর session চেক করবে
        new Handler(Looper.getMainLooper()).postDelayed(() -> checkSession(), 2000);
    }

    private void checkSession() {
        SessionManager session = new SessionManager(this);

        if(session.isLoggedIn()){
            String role = session.getRole();
            if(role != null){
                if(role.equals("admin")){
                    startActivity(new Intent(this, AdminActivity.class));
                } else if(role.equals("user")){
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
                return;
            }
        }

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
