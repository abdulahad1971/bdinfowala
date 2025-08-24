package com.bd.bdinfowala.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bd.bdinfowala.fragments.orders.CompleteFragment;
import com.bd.bdinfowala.fragments.orders.OrderListFragment;
import com.bd.bdinfowala.fragments.orders.ProcessingFragment;

public class OrderPagerAdapter extends FragmentStateAdapter {

    public OrderPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OrderListFragment();       // সব অর্ডার দেখাবে
            case 1:
                return new ProcessingFragment();      // Processing অর্ডার
            case 2:
                return new CompleteFragment();        // Delivery অর্ডার
            default:
                return new OrderListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

