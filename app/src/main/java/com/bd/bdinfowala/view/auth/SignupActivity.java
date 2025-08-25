package com.bd.bdinfowala.view.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.bd.bdinfowala.databinding.ActivitySignupBinding;
import com.bd.bdinfowala.view.auth.seassion.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        try {
            EdgeToEdge.enable(this);
            binding = ActivitySignupBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            binding.tvLogin.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));


            binding.btnSignUp.setOnClickListener(v -> {
                try {
                    String name = binding.etName.getText().toString().trim();
                    String phone = binding.etPhone.getText().toString().trim();
                    String address = binding.etAddress.getText().toString().trim();
                    String email = binding.etEmail.getText().toString().trim();
                    String password = binding.etPassword.getText().toString().trim();

                    if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(SignupActivity.this, "সব ফিল্ড পূরণ করুন", Toast.LENGTH_SHORT).show();
                    } else {
                        signupUser(name, phone, address, email, password);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SignupActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing UI", Toast.LENGTH_SHORT).show();
        }
    }

    private void signupUser(String name, String phone, String address, String email, String password) {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("অ্যাকাউন্ট তৈরি হচ্ছে...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            String url = Urls.signUrl;
            RequestQueue queue = Volley.newRequestQueue(this);
            SessionManager session = new SessionManager(SignupActivity.this);

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            progressDialog.dismiss();
                            Log.d("SignupResponse", response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String message = jsonObject.getString("message");

                            Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (status) {
                                String token = jsonObject.getString("token");


                                session.saveSession(token, "user", email);


                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignupActivity.this, "JSON Error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SignupActivity.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        try {
                            progressDialog.dismiss();
                            error.printStackTrace();
                            Toast.makeText(SignupActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                            Log.d("dsdfsdfsd",error.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SignupActivity.this, "Error handling network response", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("phone", phone);
                    params.put("address", address);
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SignupActivity.this, "Error sending signup request", Toast.LENGTH_SHORT).show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }


}
