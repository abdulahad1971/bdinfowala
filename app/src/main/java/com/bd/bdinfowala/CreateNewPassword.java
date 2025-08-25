package com.bd.bdinfowala;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.databinding.FragmentCreateNewPasswordBinding;
import com.bd.bdinfowala.view.auth.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class CreateNewPassword extends Fragment {

    private String email;
    FragmentCreateNewPasswordBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateNewPasswordBinding.inflate(inflater, container, false);
        if (container != null) container.removeAllViews();

        if (getArguments() != null) {
            email = getArguments().getString("email");
        }

        binding.textNotice.setText("আপনার নতুন পাসওয়ার্ডটি অবশ্যই পূর্বে ব্যবহৃত পাসওয়ার্ড থেকে ভিন্ন হতে হবে।");

        binding.resetButton.setOnClickListener(v -> {
            String newPass = binding.password.getText().toString();
            String confrimPass = binding.confrimPass.getText().toString();

            if (newPass.isEmpty() || confrimPass.isEmpty()) {
                Toast.makeText(getContext(), "আপনার পাসওয়ার্ড লিখুন", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confrimPass)) {
                Toast.makeText(getContext(), "পাসওয়ার্ড দুটো মিলছে না", Toast.LENGTH_SHORT).show();
                return;
            }

            resetPassword(email, newPass);
        });

        return binding.getRoot();
    }//==============oncreate end here================================================
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void resetPassword(String email, String newPassword) {
        String url = "https://buffel.xyz/blood_village/reset_password.php";

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.resetButton.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.resetButton.setEnabled(true);

                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("myApp", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("email");
                    editor.apply();

                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                },
                error -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.resetButton.setEnabled(true);
                    Toast.makeText(getContext(), "নেটওয়ার্ক সমস্যা হয়েছে। আবার চেষ্টা করুন।", Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("new_password", newPassword);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}//=====================public class end here===========================