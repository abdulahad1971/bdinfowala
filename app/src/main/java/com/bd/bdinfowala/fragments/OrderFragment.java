package com.bd.bdinfowala.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bd.bdinfowala.R;
import com.bd.bdinfowala.adapter.OrderPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class OrderFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private OrderPagerAdapter pagerAdapter;

    private final String[] tabTitles = new String[]{"অর্ডার", "প্রসেস", "সম্পন্ন",};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        pagerAdapter = new OrderPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Ensure context is not null
        LayoutInflater tabInflater = LayoutInflater.from(getContext());
        if (tabInflater == null) return view;

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View customTab = tabInflater.inflate(R.layout.custom_tab, null);
            TextView tabTitle = customTab.findViewById(R.id.tabTitle);

            if (tabTitle != null) {
                tabTitle.setText(tabTitles[position]);

                if (position == 0) {
                    tabTitle.setBackgroundResource(R.drawable.tab_selected_bg);
                    tabTitle.setTextColor(Color.WHITE);
                } else {
                    tabTitle.setBackgroundResource(R.drawable.tab_unselected_bg);
                    tabTitle.setTextColor(Color.BLACK);
                }

                tab.setCustomView(customTab);
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabAppearance(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabAppearance(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }

    private void updateTabAppearance(TabLayout.Tab tab, boolean isSelected) {
        View view = tab.getCustomView();
        if (view != null) {
            TextView tabTitle = view.findViewById(R.id.tabTitle);
            if (tabTitle != null) {
                if (isSelected) {
                    tabTitle.setBackgroundResource(R.drawable.tab_selected_bg);
                    tabTitle.setTextColor(Color.WHITE);
                } else {
                    tabTitle.setBackgroundResource(R.drawable.tab_unselected_bg);
                    tabTitle.setTextColor(Color.BLACK);
                }
            }
        }
    }
}
