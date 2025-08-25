package com.bd.bdinfowala.admin.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.R;
import com.bd.bdinfowala.admin.model.CategoryAdmin;
import com.bd.bdinfowala.admin.views.AddCategoryActivity;
import com.bd.bdinfowala.constants.Urls;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAdminAdapter extends RecyclerView.Adapter<CategoryAdminAdapter.CategoryViewHolder> implements Filterable {

    private final List<CategoryAdmin> categoryList;
    public List<CategoryAdmin> categoryListFull;
    private final Context context;

    public CategoryAdminAdapter(List<CategoryAdmin> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
        categoryListFull = new ArrayList<>(categoryList);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryAdmin category = categoryList.get(position);
        holder.tvName.setText(category.getName());

        holder.ivEdit.setOnClickListener(v -> showEditDialog(category));
        holder.ivDelete.setOnClickListener(v -> showDeleteDialog(category, position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivEdit, ivDelete;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

    // Filterable
    @Override
    public Filter getFilter() {
        return categoryFilter;
    }

    private Filter categoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CategoryAdmin> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(categoryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CategoryAdmin item : categoryListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            categoryList.clear();
            categoryList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    // Update full list for search/filter
    public void updateFullList(List<CategoryAdmin> newList) {
        categoryList.clear();
        categoryList.addAll(newList);

        categoryListFull.clear();
        categoryListFull.addAll(newList);

        notifyDataSetChanged();
    }

    private void showEditDialog(CategoryAdmin category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);

        TextInputEditText etCategoryName = dialogView.findViewById(R.id.etCategoryName);
        etCategoryName.setText(category.getName());
        Button btnSave = dialogView.findViewById(R.id.btnAddCategory);
        btnSave.setText("আপডেট");
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        tvDialogTitle.setText("ক্যাটাগরি আপডেট করুন");

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btnSave.setOnClickListener(v -> {
            String newName = etCategoryName.getText().toString().trim();
            if (!newName.isEmpty()) {
                updateCategory(category.getId(), newName, dialog);
            } else {
                etCategoryName.setError("নাম দিন");
            }
        });
    }

    private void showDeleteDialog(CategoryAdmin category, int position) {
        new AlertDialog.Builder(context)
                .setTitle("ক্যাটাগরি ডিলিট")
                .setMessage(category.getName() + " কি আপনি সত্যিই ডিলিট করতে চান?")
                .setPositiveButton("হ্যাঁ", (dialog, which) -> deleteCategory(category.getId(), position))
                .setNegativeButton("না", null)
                .show();
    }

    private void updateCategory(int id, String name, AlertDialog dialog) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, Urls.updateCategoryUrl,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        boolean success = json.optBoolean("success", false);
                        String message = json.optString("message", "Unknown error");

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        if (success && context instanceof AddCategoryActivity) {
                            dialog.dismiss();
                            ((AddCategoryActivity) context).loadCategories();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("name", name);
                return params;
            }
        };
        queue.add(request);
    }

    private void deleteCategory(int id, int position) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, Urls.deleteCategoryUrl,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        boolean success = json.optBoolean("success", false);
                        String message = json.optString("message", "Unknown error");

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        if (success && context instanceof AddCategoryActivity) {
                            ((AddCategoryActivity) context).loadCategories();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                return params;
            }
        };
        queue.add(request);
    }
}
