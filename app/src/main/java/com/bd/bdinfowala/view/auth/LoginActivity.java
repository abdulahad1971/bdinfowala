package com.bd.bdinfowala.view.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.MainActivity;
import com.bd.bdinfowala.R;
import com.bd.bdinfowala.admin.AdminActivity;
import com.bd.bdinfowala.constants.Urls;
import com.bd.bdinfowala.databinding.ActivityLoginBinding;
import com.bd.bdinfowala.view.auth.seassion.SessionManager;
import com.bd.bdinfowala.forget_password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EdgeToEdge.enable(this);
            binding = ActivityLoginBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Signup এ যাওয়ার লিঙ্ক
            binding.tvSignUp.setOnClickListener(v ->
                    startActivity(new Intent(LoginActivity.this, SignupActivity.class))
            );

            // Forget password এ যাওয়ার লিঙ্ক
            binding.tvForgetPassword.setOnClickListener(v ->
                    startActivity(new Intent(LoginActivity.this, forget_password.class))
            );

            // Login বাটন ক্লিক
            binding.btnLogin.setOnClickListener(v -> {
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Email এবং Password পূরণ করুন", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing UI", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser(String email, String password) {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Logging in...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            String url = Urls.loginUrl;
            RequestQueue queue = Volley.newRequestQueue(this);
            SessionManager session = new SessionManager(LoginActivity.this);

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String message = jsonObject.getString("message");

                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                            if(status){
                                String role = jsonObject.getString("role");
                                String token = jsonObject.getString("token");

                                // session save
                                session.saveSession(token, role, email);

                                // role অনুযায়ী activity redirect
                                if(role.equals("admin")){
                                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                } else if(role.equals("user")){
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "JSON Error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        try {
                            progressDialog.dismiss();
                            error.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error handling network response", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            queue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "Error sending login request", Toast.LENGTH_SHORT).show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
