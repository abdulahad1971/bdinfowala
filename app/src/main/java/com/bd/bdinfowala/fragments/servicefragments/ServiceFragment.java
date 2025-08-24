package com.bd.bdinfowala.fragments.servicefragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bdinfowala.R;
import com.bd.bdinfowala.adapter.ServiceAdapter;
import com.bd.bdinfowala.model.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends Fragment {

    private static final String ARG_CATEGORY_ID = "CATEGORY_ID";
    private static final String ARG_CATEGORY_NAME = "CATEGORY_NAME";

    private int categoryId;
    private String categoryName;

    public ServiceFragment() {
        // Required empty public constructor
    }


    RecyclerView recyclerView;

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
        List<Service> serviceList = new ArrayList<>();
        serviceList.add(new Service("অনলাইনে মনোনয়নপত্র দাখিল", "অনলাইনে মনোনয়নপত্র দাখিল অনলাইনে মনোনয়নপত্র দাখিল", "https://static.vecteezy.com/system/resources/thumbnails/057/068/323/small/single-fresh-red-strawberry-on-table-green-background-food-fruit-sweet-macro-juicy-plant-image-photo.jpg", "1300"));
        serviceList.add(new Service("অফিস আদেশ/অন্যান্য নোটিশ", "অফিস আদেশ/অন্যান্য নোটিশ অফিস আদেশ/অন্যান্য নোটিশ", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Sunflower_from_Silesia2.jpg/1280px-Sunflower_from_Silesia2.jpg", "1500"));
        serviceList.add(new Service("অফিস আদেশ/অন্যান্য নোটিশ", "অফিস আদেশ/অন্যান্য নোটিশ অফিস আদেশ/অন্যান্য নোটিশ", "https://png.pngtree.com/thumb_back/fh260/background/20230519/pngtree-landscape-jpg-wallpapers-free-download-image_2573540.jpg", "1500"));
        serviceList.add(new Service("অফিস আদেশ/অন্যান্য নোটিশ", "অফিস আদেশ/অন্যান্য নোটিশ অফিস আদেশ/অন্যান্য নোটিশ", "https://png.pngtree.com/thumb_back/fh260/background/20230519/pngtree-landscape-jpg-wallpapers-free-download-image_2573540.jpg", "1500"));
        serviceList.add(new Service("অফিস আদেশ/অন্যান্য নোটিশ", "অফিস আদেশ/অন্যান্য নোটিশ অফিস আদেশ/অন্যান্য নোটিশ", "https://png.pngtree.com/thumb_back/fh260/background/20230519/pngtree-landscape-jpg-wallpapers-free-download-image_2573540.jpg", "1500"));
        serviceList.add(new Service("অফিস আদেশ/অন্যান্য নোটিশ", "অফিস আদেশ/অন্যান্য নোটিশ অফিস আদেশ/অন্যান্য নোটিশ", "https://png.pngtree.com/thumb_back/fh260/background/20230519/pngtree-landscape-jpg-wallpapers-free-download-image_2573540.jpg", "1500"));
        serviceList.add(new Service("অফিস আদেশ/অন্যান্য নোটিশ", "অফিস আদেশ/অন্যান্য নোটিশ অফিস আদেশ/অন্যান্য নোটিশ", "https://png.pngtree.com/thumb_back/fh260/background/20230519/pngtree-landscape-jpg-wallpapers-free-download-image_2573540.jpg", "1500"));

        ServiceAdapter adapter = new ServiceAdapter(getContext(), serviceList);
        recyclerView.setAdapter(adapter);

        return v;
    }
}
