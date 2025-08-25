package com.bd.bdinfowala.admin.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.R;
import com.bd.bdinfowala.admin.adapter.CategoryAdminAdapter;
import com.bd.bdinfowala.admin.model.CategoryAdmin;
import com.bd.bdinfowala.constants.Urls;
import com.bd.bdinfowala.databinding.ActivityAddCategoryBinding;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {

    private ActivityAddCategoryBinding binding;
    private static final String INSERT_CATEGORY_URL = Urls.insertCategoryUrl;
    private static final String SELECT_CATEGORY_URL = Urls.showCategoryUrl;

    private RecyclerView recyclerView;
    private CategoryAdminAdapter adapter;
    private List<CategoryAdmin> categoryList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        applyEdgeToEdgePadding();
        setupRecyclerView();
        setupSearchView();
        setupFloatingActionButton();
        loadCategories();


        binding.back.setOnClickListener(v -> finish());
    }

    private void applyEdgeToEdgePadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupRecyclerView() {
        recyclerView = binding.recyclerViewCategories;
        categoryList = new ArrayList<>();
        adapter = new CategoryAdminAdapter(categoryList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupSearchView() {
        searchView = binding.searchView;

        setSearchViewFont(searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setSearchViewFont(View view) {
        if (view instanceof android.widget.TextView) {
            android.widget.TextView tv = (android.widget.TextView) view;
            android.graphics.Typeface typeface = androidx.core.content.res.ResourcesCompat.getFont(this, R.font.kalpurush);
            tv.setTypeface(typeface);
        } else if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                setSearchViewFont(vg.getChildAt(i));
            }
        }
    }


    private void setupFloatingActionButton() {
        binding.floatingactionbutton.setOnClickListener(v -> showAddCategoryDialog());
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);

        TextInputEditText etCategoryName = dialogView.findViewById(R.id.etCategoryName);
        Button btnAddCategory = dialogView.findViewById(R.id.btnAddCategory);
        Button cancel = dialogView.findViewById(R.id.cancel);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAddCategory.setOnClickListener(view -> {
            String categoryName = etCategoryName.getText().toString().trim();
            if (!categoryName.isEmpty()) {
                insertCategory(categoryName, dialog);
            } else {
                etCategoryName.setError("ক্যাটাগরি নাম দিন");
            }
        });
    }

    private void insertCategory(String categoryName, AlertDialog dialog) {
        binding.progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, INSERT_CATEGORY_URL,
                response -> {
                    binding.progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject json = new JSONObject(response);
                        Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
                        if (json.getBoolean("success")) {
                            dialog.dismiss();
                            loadCategories();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("name", categoryName);
                return params;
            }
        };

        queue.add(postRequest);
    }

    public void loadCategories() {
        RequestQueue queue = Volley.newRequestQueue(this);
        binding.progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, SELECT_CATEGORY_URL,
                response -> {
                    binding.progressBar.setVisibility(View.GONE);
                    try {
                        JSONArray array = new JSONArray(response);
                        categoryList.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            int id = obj.getInt("id");
                            String name = obj.getString("name");
                            categoryList.add(new CategoryAdmin(id, name));
                        }

                        if (adapter != null) {
                            adapter.categoryListFull.clear();
                            adapter.categoryListFull.addAll(categoryList);
                            adapter.notifyDataSetChanged();
                        }

                        binding.tvCategoryCount.setText("মোট ক্যাটাগরি: " + convertToBanglaNumber(categoryList.size()));


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("dfdfgdfg",error.toString());
                });

        queue.add(request);
    }
    private String convertToBanglaNumber(int number) {
        String numStr = String.valueOf(number);
        numStr = numStr.replace('0', '০')
                .replace('1', '১')
                .replace('2', '২')
                .replace('3', '৩')
                .replace('4', '৪')
                .replace('5', '৫')
                .replace('6', '৬')
                .replace('7', '৭')
                .replace('8', '৮')
                .replace('9', '৯');
        return numStr;
    }



}
