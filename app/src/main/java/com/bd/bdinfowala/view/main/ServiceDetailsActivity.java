package com.bd.bdinfowala.view.main;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bd.bdinfowala.R;
import com.bd.bdinfowala.constants.Urls;
import com.bd.bdinfowala.databinding.ActivityServiceDetailsBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceDetailsActivity extends AppCompatActivity {

    private ActivityServiceDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // ✅ ViewBinding init
        binding = ActivityServiceDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Edge to edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ✅ Intent থেকে ডাটা রিসিভ করা
        Bundle extras = getIntent().getExtras();
        if (extras != null) {


            binding.tvCategoryName.setText("ক্যাটাগরি নাম: " + extras.getString("category_name", "N/A"));
            binding.tvServiceName.setText(extras.getString("service_name", "N/A"));
            binding.titleName.setText(extras.getString("service_name", "N/A"));

            String price = extras.getString("price", "0.00");

            if (price.contains(".")) {
                price = price.substring(0, price.indexOf(".")); // শুধু . এর আগের অংশ নিলাম
            }

            price = convertToBanglaNumber(price);

            binding.tvPrice.setText("" + price + " টাকা");

            String days = extras.getString("days", "N/A");
            if (!days.equals("N/A")) {
                days = convertToBanglaNumber(days);
            }
            binding.tvDays.setText("কার্যদিবস: " + days + " দিন");

            binding.tvDescription.setText("বর্ণনা: " + extras.getString("description", ""));
            binding.tvFeatures.setText("বৈশিষ্ট্য: " + extras.getString("features", ""));


            // ✅ Glide দিয়ে ছবি লোড
            String imageUrl = extras.getString("image_url");
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(Urls.imageUrl + "uploads/images/" + imageUrl)
                        .placeholder(R.drawable.man)
                        .error(R.drawable.man)
                        .into(binding.ivService);
            }

            // ✅ Requirement JSON
            String requirementJson = extras.getString("requirement_json", "[]");
            try {
                JSONArray array = new JSONArray(requirementJson);
                binding.requirementsContainer.removeAllViews();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String label = obj.optString("label", "Requirement");

                    // Label TextView
                    TextView labelView = new TextView(this);
                    labelView.setTypeface(ResourcesCompat.getFont(this, R.font.kalpurush));

                    LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    labelParams.setMargins(0, 16, 0, 8);
                    labelView.setLayoutParams(labelParams);
                    labelView.setText(label);
                    labelView.setTextSize(14f);
                    labelView.setTextColor(getResources().getColor(R.color.black));

                    // TextInputLayout
                    TextInputLayout til = new TextInputLayout(this, null,
                            com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
                    LinearLayout.LayoutParams tilParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    tilParams.setMargins(0, 0, 0, 16);
                    til.setLayoutParams(tilParams);
                    til.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                    til.setBoxCornerRadii(16, 16, 16, 16);
                    til.setHint("Enter " + label);
                    til.setHintTextAppearance(R.style.TextInputHint);
                    til.setBoxStrokeColor(getResources().getColor(R.color.main_color));
                    til.setBoxStrokeWidthFocused(3);
                    til.setStartIconTintList(getResources().getColorStateList(R.color.main_color));

                    // ✅ TextInputEditText with extra padding inside
                    TextInputEditText editText = new TextInputEditText(til.getContext());
                    editText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    // Padding in dp → pixels
                    int horizontalPadding = (int) (16 * getResources().getDisplayMetrics().density);
                    int verticalPadding = (int) (20 * getResources().getDisplayMetrics().density);
                    editText.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);

                    editText.setTextSize(16f);
                    editText.setTextColor(getResources().getColor(R.color.main_color));
                    editText.setTypeface(ResourcesCompat.getFont(this, R.font.kalpurush)); // custom font
                    editText.setBackground(null); // remove underline

                    til.addView(editText);

                    // Add views to container
                    binding.requirementsContainer.addView(labelView);
                    binding.requirementsContainer.addView(til);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "No service data found!", Toast.LENGTH_SHORT).show();
        }

        // ✅ Back button
        binding.back.setOnClickListener(v -> finish());
    }

    private String convertToBanglaNumber(String value) {
        return value.replace("0", "০")
                .replace("1", "১")
                .replace("2", "২")
                .replace("3", "৩")
                .replace("4", "৪")
                .replace("5", "৫")
                .replace("6", "৬")
                .replace("7", "৭")
                .replace("8", "৮")
                .replace("9", "৯");
    }

}
