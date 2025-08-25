package com.bd.bdinfowala.admin.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.R;
import com.bd.bdinfowala.constants.Urls;
import com.bd.bdinfowala.databinding.ActivityAddCategoryBinding;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {

    private ActivityAddCategoryBinding binding;
    private static final String INSERT_CATEGORY_URL = Urls.insertCategoryUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applyEdgeToEdgePadding();
        setupFloatingActionButton();
    }

    // Apply edge-to-edge system bar padding
    private void applyEdgeToEdgePadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }

    // FloatingActionButton click listener
    private void setupFloatingActionButton() {
        binding.floatingactionbutton.setOnClickListener(v -> showAddCategoryDialog());
    }

    // Show AlertDialog to add new category
    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);

        TextInputEditText etCategoryName = dialogView.findViewById(R.id.etCategoryName);
        Button btnAddCategory = dialogView.findViewById(R.id.btnAddCategory);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnAddCategory.setOnClickListener(view -> {
            String categoryName = etCategoryName.getText().toString().trim();
            if (!categoryName.isEmpty()) {
                insertCategory(categoryName, dialog);
            } else {
                etCategoryName.setError("নাম দিন");
            }
        });
    }

    private void insertCategory(String categoryName, AlertDialog dialog) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, INSERT_CATEGORY_URL,
                response -> handleResponse(response, dialog),
                error -> handleError(error)
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", categoryName);
                return params;
            }
        };

        queue.add(postRequest);
    }

    private void handleResponse(String response, AlertDialog dialog) {
        try {
            JSONObject json = new JSONObject(response);
            boolean success = json.getBoolean("success");
            String message = json.getString("message");

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            if (success) {
                dialog.dismiss();
                // TODO: reload categories if needed
                // loadCategories();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle Volley errors
    private void handleError(VolleyError error) {
        Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
    }
}
