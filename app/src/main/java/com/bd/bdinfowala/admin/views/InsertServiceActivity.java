package com.bd.bdinfowala.admin.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.bd.bdinfowala.R;
import com.bd.bdinfowala.constants.Urls;
import com.bd.bdinfowala.databinding.ActivityInsertServiceBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertServiceActivity extends AppCompatActivity {

    private ActivityInsertServiceBinding binding;
    private Uri selectedImageUri;

    // category id list for mapping
    private List<Integer> categoryIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityInsertServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // SystemBars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Image Picker
        binding.editIcon.setOnClickListener(v -> {
            try {
                ImagePicker.with(this)
                        .cropSquare()
                        .compress(512)
                        .maxResultSize(512, 512)
                        .start();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Cannot open image picker", Toast.LENGTH_SHORT).show();
            }
        });

        // Placeholder Spinner
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("ক্যাটাগরি নির্বাচন করুন");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categoryNames);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapter);

        // Spinner touch -> load categories dynamically
        binding.spinnerCategory.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                loadCategoriesIntoSpinner(binding.spinnerCategory);
            }
            return false;
        });

        // Add Requirement Button Click
        binding.btnAddRequirement.setOnClickListener(v -> {
            View reqRow = LayoutInflater.from(this).inflate(R.layout.requirement_field_item, null);
            binding.layoutRequirements.addView(reqRow);
        });

        // Add Service Button Click
        binding.btnAddService.setOnClickListener(v -> {
            String serviceName = binding.etServiceName.getText().toString().trim();
            String serviceDesc = binding.etServiceDescription.getText().toString().trim();
            String serviceBoististo = binding.etServiceBoististo.getText().toString().trim();
            String servicePrice = binding.servicePrice.getText().toString().trim();
            String serviceDay = binding.serviceDay.getText().toString().trim();

            int categoryPosition = binding.spinnerCategory.getSelectedItemPosition();
            int categoryId = categoryPosition > 0 ? categoryIds.get(categoryPosition - 1) : 0;
            String categoryName = binding.spinnerCategory.getSelectedItem().toString();

            if (categoryId == 0) {
                Toast.makeText(this, "Category নির্বাচন করুন", Toast.LENGTH_SHORT).show();
                return;
            }

            // Requirement JSON
            JSONArray requirementArray = new JSONArray();
            for (int i = 0; i < binding.layoutRequirements.getChildCount(); i++) {
                View child = binding.layoutRequirements.getChildAt(i);
                EditText etFieldLabel = child.findViewById(R.id.etFieldLabel);
                Spinner spinnerFieldType = child.findViewById(R.id.spinnerFieldType);
                CheckBox chkRequired = child.findViewById(R.id.chkRequired);

                String label = etFieldLabel.getText().toString().trim();
                String type = spinnerFieldType.getSelectedItem().toString();
                boolean required = chkRequired.isChecked();

                if (!label.isEmpty()) {
                    JSONObject field = new JSONObject();
                    try {
                        field.put("field", label.toLowerCase().replace(" ", "_"));
                        field.put("label", label);
                        field.put("type", type);
                        field.put("required", required);
                        requirementArray.put(field);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            String requirementJson = requirementArray.toString();
            String imageBase64 = "";

            // Convert image to Base64
            if (selectedImageUri != null) {
                try {
                    InputStream is = getContentResolver().openInputStream(selectedImageUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, read);
                    }
                    imageBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Send to API
            sendServiceToApi(serviceName, serviceDesc, serviceBoististo, servicePrice,
                    serviceDay, categoryId, categoryName, requirementJson, imageBase64);
        });

        // Cancel Button
        binding.cancel.setOnClickListener(v -> finish());
    }

    private void loadCategoriesIntoSpinner(Spinner spinner) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Urls.showCategoryUrl,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        List<String> categoryNames = new ArrayList<>();
                        categoryIds.clear();
                        categoryNames.add("ক্যাটাগরি নির্বাচন করুন");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            categoryIds.add(obj.getInt("id"));
                            categoryNames.add(obj.getString("name"));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                R.layout.spinner_item, categoryNames);
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.post(spinner::performClick);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        );
        queue.add(request);
    }

    private void sendServiceToApi(String serviceName, String description, String features,
                                  String price, String days, int categoryId, String categoryName,
                                  String requirementJson, String imageBase64) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Urls.insertServiceUrl; // আপনার PHP URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        boolean status = obj.getBoolean("status");
                        String message = obj.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                        if (status) {
                            // Clear form
                            binding.etServiceName.setText("");
                            binding.etServiceDescription.setText("");
                            binding.etServiceBoististo.setText("");
                            binding.servicePrice.setText("");
                            binding.serviceDay.setText("");
                            binding.layoutRequirements.removeAllViews();
                            binding.image.setImageResource(R.drawable.balloon);
                            selectedImageUri = null;
                            binding.spinnerCategory.setSelection(0);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Response parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("service_name", serviceName);
                params.put("description", description);
                params.put("features", features);
                params.put("price", price);
                params.put("days", days);
                params.put("category_id", String.valueOf(categoryId));
                params.put("category_name", categoryName);
                params.put("requirement_json", requirementJson);
                params.put("image_base64", imageBase64); // <-- send Base64
                return params;
            }
        };

        queue.add(stringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && data != null) {
                selectedImageUri = data.getData();
                if (binding.image != null) {
                    binding.image.setImageURI(selectedImageUri);
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Image selection error", Toast.LENGTH_SHORT).show();
        }
    }
}
