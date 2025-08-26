package com.bd.bdinfowala;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.databinding.FragmentSendOtpBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class send_otp extends Fragment {

    FragmentSendOtpBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentSendOtpBinding.inflate(inflater,container,false);

        binding.sendOtp.setOnClickListener(v -> {

            String email=binding.email.getText().toString();
            if (email.isEmpty()){
                Toast.makeText(getContext(),"দয়াকরে ইমেইল দিন?",Toast.LENGTH_SHORT).show();
            }else {
                sendOtpRequest(email);
            }

        });

        return binding.getRoot();
    }





    private void sendOtpRequest(final String email) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String OTP_URL = "https://bdinfowala.com/bdinfowala/api/send_mail/send_otp.php";
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.sendOtp.setEnabled(false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, OTP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.sendOtp.setEnabled(true);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("success")) {
                                // ইমেইল bundle এ পাঠানো
                                Bundle bundle = new Bundle();
                                bundle.putString("email", email);

                                OtpVerify otpFragment = new OtpVerify();
                                otpFragment.setArguments(bundle);

                                FragmentTransaction transaction = requireActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction();
                                transaction.replace(R.id.frame_layout, otpFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();

                                Toast.makeText(getContext(), "OTP সফলভাবে পাঠানো হয়েছে।", Toast.LENGTH_LONG).show();
                            } else {
                                // ভুল ইমেইল বা অন্য সমস্যা হলে Toast দেখাবে
                                Toast.makeText(getContext(), "কিছু একটা সমস্যা হয়েছে, আবার চেষ্টা করুন।", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.sendOtp.setEnabled(true);
                        Toast.makeText(getContext(), "Network Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        queue.add(postRequest);
    }

}