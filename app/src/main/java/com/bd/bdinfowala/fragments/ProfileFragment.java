package com.bd.bdinfowala.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.R;
import com.bd.bdinfowala.constants.Urls;
import com.bd.bdinfowala.databinding.FragmentProfileBinding;
import com.bd.bdinfowala.view.auth.LoginActivity;
import com.bd.bdinfowala.view.auth.ProfileEditActivity;
import com.bd.bdinfowala.view.auth.seassion.SessionManager;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SessionManager session;

    private String userName, userPhone, joinTime, userEmail, address, profileImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        try {
            binding = FragmentProfileBinding.inflate(inflater, container, false);
            session = new SessionManager(getContext());

            fetchProfile();


            // Edit Profile button
            binding.editProfile.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                    intent.putExtra("name", userName);
                    intent.putExtra("address", address);
                    intent.putExtra("phone", userPhone);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("profile_image", profileImage);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Cannot open Edit Profile", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "UI initialization failed", Toast.LENGTH_SHORT).show();
        }

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.clearSession();
                startActivity(new Intent(getActivity(), LoginActivity.class));

            }
        });

        return binding.getRoot();
    }

    private void fetchProfile() {
        try {
            binding.profileProgressBar.setVisibility(View.VISIBLE);



            String url = Urls.getProfileUrl;
            RequestQueue queue = Volley.newRequestQueue(getContext());

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            binding.profileProgressBar.setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");

                            if (status) {
                                // Set global variables
                                userName = jsonObject.optString("name");
                                userPhone = jsonObject.optString("phone");
                                address = jsonObject.optString("address");
                                profileImage = jsonObject.optString("profile_image");
                                joinTime = jsonObject.optString("join_date");
                                userEmail = jsonObject.optString("email");

                                // Update UI
                                binding.name.setText(userName);
                                binding.email.setText(userEmail);
                                binding.phone.setText(userPhone);
                                binding.profileAddress.setText(address);
                                binding.join.setText(joinTime);

                                String imageUrl;
                                if (profileImage != null && profileImage.startsWith("uploads/")) {
                                    imageUrl = Urls.imageUrl + profileImage;
                                } else {
                                    imageUrl = Urls.imageUrl + "uploads/profile_images/" + profileImage;
                                }

                                try {
                                    Glide.with(getContext())
                                            .load(imageUrl)
                                            .placeholder(R.drawable.man)
                                            .error(R.drawable.man)
                                            .into(binding.profileImage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Image load error", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getContext(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "JSON parse error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        try {
                            binding.profileProgressBar.setVisibility(View.GONE);
                        } catch (Exception e) { e.printStackTrace(); }
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    try {
                        params.put("token", session.getToken());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return params;
                }
            };

            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to fetch profile", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            fetchProfile(); // Refresh data after returning from ProfileEditActivity
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        try {
            super.onDestroyView();
            binding = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
