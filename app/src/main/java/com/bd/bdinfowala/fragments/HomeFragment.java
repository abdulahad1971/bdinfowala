package com.bd.bdinfowala.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bd.bdinfowala.R;
import com.bd.bdinfowala.fragments.servicefragments.ServiceFragment;
import com.bd.bdinfowala.model.Category;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ImageSlider image_slider;
    private TabLayout tabLayout;
    private List<Category> categoryList = new ArrayList<>();
    private static final String URL = "http://192.168.0.103/practice_api/test.json";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Image Slider
        image_slider = view.findViewById(R.id.image_slider);
        setupImageSlider();

        // TabLayout
        tabLayout = view.findViewById(R.id.tabLayout);

        // Load categories from API
        loadCategories();

        // Tab select listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Category c = categoryList.get(position);
                openServiceFragment(c.getId(), c.getName());

                // Tab font, color, margin
                View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(position);
                if (tabView instanceof ViewGroup) {
                    setTabFontAndMargin((ViewGroup) tabView, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(position);
                if (tabView instanceof ViewGroup) {
                    setTabFontAndMargin((ViewGroup) tabView, false);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    // Helper method to set font, color, margin
    private void setTabFontAndMargin(ViewGroup tabView, boolean selected) {
        for (int i = 0; i < tabView.getChildCount(); i++) {
            View child = tabView.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.kalpurush);
                textView.setTypeface(typeface, selected ? Typeface.BOLD : Typeface.NORMAL);
                textView.setTextColor(selected ? Color.WHITE : Color.BLACK);
            }
        }

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
        int margin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        params.setMargins(margin, 0, margin, 0);
        tabView.setLayoutParams(params);
    }

    private void setupImageSlider() {
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel("https://wallpaperaccess.com/full/4723253.jpg", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://png.pngtree.com/background/20230211/original/pngtree-21-february-banner-picture-image_2026321.jpg", ScaleTypes.CENTER_CROP));


        image_slider.setImageList(imageList);
    }

    private void loadCategories() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            categoryList.clear();
                            tabLayout.removeAllTabs();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                int id = obj.getInt("id");
                                String name = obj.getString("name");

                                Category category = new Category(id, name);
                                categoryList.add(category);

                                tabLayout.addTab(tabLayout.newTab().setText(name));
                            }

                            // Apply font/color/margin to first tab
                            if (!categoryList.isEmpty()) {
                                Category first = categoryList.get(0);
                                openServiceFragment(first.getId(), first.getName());
                                View firstTabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
                                if (firstTabView instanceof ViewGroup) {
                                    setTabFontAndMargin((ViewGroup) firstTabView, true);
                                }
                            }

                        } catch (JSONException e) {
                            Toast.makeText(requireContext(), "Parse error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (isAdded() && getContext() != null) {
                            Toast.makeText(getContext(), "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }                    }
                });

        queue.add(request);
    }

    private void openServiceFragment(int categoryId, String categoryName) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.serviceFragmentContainer, ServiceFragment.newInstance(categoryId, categoryName))
                .commit();
    }
}
