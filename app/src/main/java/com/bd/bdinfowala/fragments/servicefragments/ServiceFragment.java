package com.bd.bdinfowala.fragments.servicefragments;

import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends Fragment {

    private static final String ARG_CATEGORY_ID = "CATEGORY_ID";
    private static final String ARG_CATEGORY_NAME = "CATEGORY_NAME";

    private int categoryId;
    private String categoryName;

    RecyclerView recyclerView;
    List<Service> serviceList;
    ServiceAdapter adapter;

    public ServiceFragment() {
        // Required empty public constructor
    }

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

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

        serviceList = new ArrayList<>();
        adapter = new ServiceAdapter(getContext(), serviceList);
        recyclerView.setAdapter(adapter);

        loadServices(categoryId);

        return v;
    }

    private void loadServices(int categoryId) {
        String url = Urls.getServicesByCategory + "?category_id=" + categoryId;

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    serviceList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            int id = obj.optInt("id", 0);
                            int catId = obj.optInt("category_id", categoryId);
                            String catName = obj.optString("category_name", "");
                            String serviceName = obj.optString("service_name", "");
                            String description = obj.optString("description", "");
                            String features = obj.optString("features", "");
                            String price = obj.optString("price", "0");
                            String days = obj.optString("days", "");
                            String imageUrl = obj.optString("image_url", "");
                            String requirementJson = obj.optString("requirement_json", "");

                            Service service = new Service(
                                    id,
                                    catId,
                                    catName,
                                    serviceName,
                                    description,
                                    features,
                                    price,
                                    days,
                                    imageUrl,
                                    requirementJson
                            );

                            serviceList.add(service);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        queue.add(request);
    }

}
