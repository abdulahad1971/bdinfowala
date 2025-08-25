package com.bd.bdinfowala.fragments.servicefragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.R;
import com.bd.bdinfowala.adapter.ServiceAdapter;
import com.bd.bdinfowala.constants.Urls;
import com.bd.bdinfowala.model.Service;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends Fragment {

    private static final String ARG_CATEGORY_ID = "CATEGORY_ID";
    private static final String ARG_CATEGORY_NAME = "CATEGORY_NAME";

    private int categoryId;
    private String categoryName;

    RecyclerView recyclerView;
    TextView tvEmpty;
    ShimmerFrameLayout shimmerLayout;
    List<Service> serviceList;
    ServiceAdapter adapter;

    public ServiceFragment() {}

    public static ServiceFragment newInstance(int categoryId, String categoryName) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        args.putString(ARG_CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
            categoryName = getArguments().getString(ARG_CATEGORY_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service, container, false);

        TextView tv = v.findViewById(R.id.tvInfo);
        tv.setText(categoryName);

        tvEmpty = v.findViewById(R.id.tvEmpty);
        shimmerLayout = v.findViewById(R.id.shimmerLayout);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        serviceList = new ArrayList<>();
        adapter = new ServiceAdapter(getContext(), serviceList);
        recyclerView.setAdapter(adapter);

        // Shimmer start
        shimmerLayout.startShimmer();
        shimmerLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);

        loadServices(categoryId);

        return v;
    }

    private void loadServices(int categoryId) {
        String url;
        if (categoryId == 0) {
            url = Urls.showServiceUrl; // All services API
        } else {
            url = Urls.getServicesByCategory + "?category_id=" + categoryId;
        }

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    serviceList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            Service service = new Service(
                                    obj.optInt("id", 0),
                                    obj.optInt("category_id", categoryId),
                                    obj.optString("category_name", ""),
                                    obj.optString("service_name", ""),
                                    obj.optString("description", ""),
                                    obj.optString("features", ""),
                                    obj.optString("price", "0"),
                                    obj.optString("days", ""),
                                    obj.optString("image_url", ""),
                                    obj.optString("requirement_json", "")
                            );

                            serviceList.add(service);
                        }

                        adapter.notifyDataSetChanged();

                        // Shimmer stop
                        shimmerLayout.stopShimmer();
                        shimmerLayout.setVisibility(View.GONE);

                        // Empty check
                        if (serviceList.isEmpty()) {
                            tvEmpty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            tvEmpty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        shimmerLayout.stopShimmer();
                        shimmerLayout.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("ServiceFragment", error.toString());
                }
        );

        queue.add(request);
    }
}
