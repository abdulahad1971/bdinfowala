package com.bd.bdinfowala.admin.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.admin.adapter.AdminServiceAdapter;
import com.bd.bdinfowala.admin.model.AdminService;
import com.bd.bdinfowala.constants.Urls;
import com.bd.bdinfowala.databinding.ActivityAddServiceBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddServiceActivity extends AppCompatActivity {

    private ActivityAddServiceBinding binding;
    private AdminServiceAdapter adapter;
    private List<AdminService> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // RecyclerView setup
        binding.recyclerViewService.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminServiceAdapter(this, serviceList);
        binding.recyclerViewService.setAdapter(adapter);

        // FloatingActionButton click -> open InsertServiceActivity
        binding.floatingactionbutton.setOnClickListener(v ->
                startActivity(new Intent(AddServiceActivity.this, InsertServiceActivity.class))
        );

        // Load service list from API
        loadServiceList();

        // Search filter
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterServices(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterServices(newText);
                return false;
            }
        });
    }

    private void loadServiceList() {
        binding.progressBar.setVisibility(android.view.View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Urls.showServiceUrl,
                response -> {
                    binding.progressBar.setVisibility(android.view.View.GONE);
                    try {
                        JSONArray array = new JSONArray(response);
                        serviceList.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            AdminService service = new AdminService(
                                    obj.getInt("id"),
                                    obj.getInt("category_id"),
                                    obj.getString("category_name"),
                                    obj.getString("service_name"),
                                    obj.getString("description"),
                                    obj.getString("features"),
                                    obj.getString("price"),
                                    obj.getString("days"),
                                    obj.getString("image_url"),
                                    obj.getString("requirement_json")
                            );
                            serviceList.add(service);
                        }

                        adapter.updateList(serviceList);

                        binding.tvCategoryCount.setText("মোট ক্যাটাগরি: " + convertToBanglaNumber(serviceList.size()));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
                        Log.d("Parsedfffffff",e.toString());
                    }
                },
                error -> {
                    binding.progressBar.setVisibility(android.view.View.GONE);
                    error.printStackTrace();
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    private void filterServices(String query) {
        List<AdminService> filteredList = new ArrayList<>();
        for (AdminService service : serviceList) {
            if (service.getServiceName().toLowerCase().contains(query.toLowerCase())
                    || service.getCategoryName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(service);
            }
        }
        adapter.updateList(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh list after returning from InsertServiceActivity
        loadServiceList();
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
