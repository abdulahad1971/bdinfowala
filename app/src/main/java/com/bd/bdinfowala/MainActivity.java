package com.bd.bdinfowala;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bd.bdinfowala.fragments.FavoritesFragment;
import com.bd.bdinfowala.fragments.HomeFragment;
import com.bd.bdinfowala.fragments.OrderFragment;
import com.bd.bdinfowala.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                try {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, v.getPaddingTop(), systemBars.right, v.getPaddingBottom());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return insets;
            });

            bottomNav = findViewById(R.id.bottomNav);

            loadFragment(new HomeFragment());

            // Bottom Navigation item select করলে ফ্র্যাগমেন্ট লোড হবে
            bottomNav.setOnItemSelectedListener(item -> {
                try {
                    Fragment fragment = null;
                    int id = item.getItemId();
                    if (id == R.id.nav_home) fragment = new HomeFragment();
                    else if (id == R.id.order) fragment = new OrderFragment();
                    else if (id == R.id.favorites) fragment = new FavoritesFragment();
                    else if (id == R.id.profile) fragment = new ProfileFragment();
                    if (fragment != null) loadFragment(fragment);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error loading fragment", Toast.LENGTH_SHORT).show();
                }
                return true;
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing MainActivity", Toast.LENGTH_LONG).show();
        }
    }

    // Fragment লোড করার মেথড
    private void loadFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading fragment", Toast.LENGTH_SHORT).show();
        }
    }

    // Custom Alert Dialog
    @Override
    public void onBackPressed() {
        try {
            ShowDialogBox();
        } catch (Exception e) {
            e.printStackTrace();
            super.onBackPressed(); // যদি কোনো error হয়, normal back behavior
        }
    }

    private void ShowDialogBox() {
        try {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.exit_app_dialog, null);
            alert.setView(mView);

            final AlertDialog alertDialog = alert.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            mView.findViewById(R.id.chancelBTN).setOnClickListener(v -> {
                try {
                    alertDialog.dismiss();
                    finishAndRemoveTask();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            mView.findViewById(R.id.okBTN).setOnClickListener(v -> {
                try {
                    alertDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error showing exit dialog", Toast.LENGTH_SHORT).show();
        }
    }
}
