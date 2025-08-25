package com.bd.bdinfowala.view.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.R;
import com.bd.bdinfowala.constants.Urls;
import com.bd.bdinfowala.databinding.ActivityProfileEditBinding;
import com.bd.bdinfowala.view.auth.seassion.SessionManager;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {

    private ActivityProfileEditBinding binding;
    private Uri selectedImageUri;
    private ProgressDialog progressDialog;
    private String userToken;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            EdgeToEdge.enable(this);
            binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            sessionManager = new SessionManager(this);
            userToken = sessionManager.getToken();

            binding.back.setOnClickListener(v -> {
                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error closing activity", Toast.LENGTH_SHORT).show();
                }
            });

            // Load data from intent
            Intent intent = getIntent();
            if (intent != null) {
                try {
                    binding.etName.setText(intent.getStringExtra("name"));
                    binding.etPhone.setText(intent.getStringExtra("phone"));
                    binding.etEmail.setText(intent.getStringExtra("email"));
                    binding.etAddress.setText(intent.getStringExtra("address"));
                    String image = intent.getStringExtra("profile_image");


                    String imageUrl = null;

                    if (image != null && !image.isEmpty()) {
                        if (image.startsWith("uploads/")) {
                            imageUrl = Urls.imageUrl + image;  // Already relative path
                        } else {
                            imageUrl = Urls.imageUrl + "uploads/profile_images/" + image;
                        }
                    }

                    if (imageUrl != null) {
                        Glide.with(this)
                                .load(imageUrl)
                                .placeholder(R.drawable.man)
                                .error(R.drawable.man)
                                .into(binding.profileImage);
                    }





                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Select Image
            binding.editIcon.setOnClickListener(v -> {
                try {
                    ImagePicker.with(ProfileEditActivity.this)
                            .cropSquare()
                            .compress(512)
                            .maxResultSize(512, 512)
                            .start();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Cannot open image picker", Toast.LENGTH_SHORT).show();
                }
            });

            binding.btnUpdate.setOnClickListener(v -> updateProfile());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Initialization error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && data != null) {
                selectedImageUri = data.getData();
                binding.profileImage.setImageURI(selectedImageUri); // Preview
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Image selection error", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {
        try {
            String name = binding.etName.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "সব তথ্য পূরণ করুন", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Updating...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // Convert selected image to Base64
            String imageBase64 = null;
            if (selectedImageUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        baos.write(buffer, 0, read);
                    }
                    imageBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Image conversion failed", Toast.LENGTH_SHORT).show();
                }
            }

            String finalImageBase64 = imageBase64;

            StringRequest request = new StringRequest(Request.Method.POST, Urls.editProfileUrl,
                    response -> {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            finish(); // close activity
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Update success but UI error", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception e) { e.printStackTrace(); }
                        Log.e("UpdateProfile", error.toString());
                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    try {
                        params.put("token", userToken);
                        params.put("name", name);
                        params.put("phone", phone);
                        params.put("email", email);
                        if (finalImageBase64 != null) params.put("profile_image", finalImageBase64);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return params;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    15000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            Volley.newRequestQueue(this).add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
