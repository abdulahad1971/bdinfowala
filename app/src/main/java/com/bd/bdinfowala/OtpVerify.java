package com.bd.bdinfowala;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.databinding.OtpVerifyBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpVerify extends Fragment {

    private String email;
    OtpVerifyBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=OtpVerifyBinding.inflate(inflater,container,false);
        if (container!=null) container.removeAllViews();


        EditText[] otpFields = {
                binding.otp1,
                binding.otp2,
                binding.otp3,
                binding.otp4,
                binding.otp5,
                binding.otp6
        };

        // TextWatcher: Auto move between fields
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;

            otpFields[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        otpFields[index - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        // Bundle থেকে ইমেইল নেওয়া
        if (getArguments() != null) {
            email = getArguments().getString("email");
        }

        // OTP verify বাটনে ক্লিক
        binding.verifyOtp.setOnClickListener(v -> {
            String otp = "";
            for (EditText field : otpFields) {
                otp += field.getText().toString().trim();
            }
            verifyOtp(email, otp);
        });

        // resend underline effect
        binding.resendText.post(() -> {
            int width = binding.resendText.getWidth();
            ViewGroup.LayoutParams params = binding.underlineView.getLayoutParams();
            params.width = width;
            binding.underlineView.setLayoutParams(params);
            binding.underlineView.setBackgroundColor(Color.parseColor("#FF8A80"));
            binding.resendText.setTextColor(Color.parseColor("#FF8A80"));
        });

        //======resend code==================
        binding.textNotice.setText("অনুগ্রহ করে "+email+" -এ পাঠানো ৬ ডিজিটের কোডটি দিন।");
        binding.resentCode.setOnClickListener(v -> {
            resendOtp(email);
            for (EditText field : otpFields) {
                field.setText("");
            }
            // ফোকাস প্রথম ঘরে দাও
            binding.otp1.requestFocus();
        });


        return binding.getRoot();
    }//=======================oncreate end here======================

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void verifyOtp(String email, String otp) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String VERIFY_URL = "https://bdinfowala.com/bdinfowala/api/send_mail/verify_otp.php";

        // ProgressBar দেখানো এবং বাটন নিষ্ক্রিয় করা
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.verifyOtp.setEnabled(false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, VERIFY_URL,
                response -> {
                    // ProgressBar লুকানো এবং বাটন সক্রিয় করা
                    binding.progressBar.setVisibility(View.GONE);
                    binding.verifyOtp.setEnabled(true);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(getContext(), "OTP যাচাই সফল হয়েছে।", Toast.LENGTH_LONG).show();

                            // ইমেইল bundle এ পাঠানো
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            CreateNewPassword newPasswordFragment = new CreateNewPassword();
                            newPasswordFragment.setArguments(bundle);

                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, newPasswordFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "সার্ভার থেকে প্রক্রিয়াকরণের সময় একটি সমস্যা হয়েছে।", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Error হলে progress bar লুকানো এবং বাটন সক্রিয় করা
                    binding.progressBar.setVisibility(View.GONE);
                    binding.verifyOtp.setEnabled(true);
                    Toast.makeText(getContext(), "নেটওয়ার্ক সমস্যা হয়েছে। অনুগ্রহ করে পুনরায় চেষ্টা করুন।", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("otp", otp);
                return params;
            }
        };

        queue.add(postRequest);
    }


    private void resendOtp(String email) {
        String RESEND_URL = "https://bdinfowala.com/bdinfowala/api/send_mail/resend_otp.php";

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, RESEND_URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        String status = json.getString("status");
                        String message = json.getString("message");

                        Toast.makeText(getContext(),"OTP পুনরায় পাঠানো হয়েছে।", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Response পার্সিং সমস্যা", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "OTP পাঠাতে সমস্যা হয়েছে", Toast.LENGTH_SHORT).show()
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


}//=====================public class end here==================================