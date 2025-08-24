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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, v.getPaddingTop(), systemBars.right, v.getPaddingBottom());
            return insets;
        });


        bottomNav = findViewById(R.id.bottomNav);


        loadFragment(new HomeFragment());

        // Bottom Navigation item select করলে ফ্র্যাগমেন্ট লোড হবে
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();
            if (id == R.id.nav_home) fragment = new HomeFragment();
            else if (id == R.id.order) fragment = new OrderFragment();
            else if (id == R.id.favorites) fragment = new FavoritesFragment();
            else if (id == R.id.profile) fragment = new ProfileFragment();
            if (fragment != null) loadFragment(fragment);
            return true;
        });








    }

    // Fragment লোড করার মেথড
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }



//    @Override
//    protected void onResume() {
//        super.onResume();
////        Toast.makeText(this, "onResume called", Toast.LENGTH_SHORT).show();
//        if (!NetworkUtil.isInternetAvailable(this)) {
//            Toast.makeText(this, "No internet!", Toast.LENGTH_SHORT).show();
//            NetworkUtil.showNoInternetDialog(this);
//        }
//
//
//
//
//
//    }





    //Custom Alert Dialog
    @Override
    public void onBackPressed() {
        ShowDialogBox();

    }

    private void ShowDialogBox (){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.exit_app_dialog, null);
        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Transparent background
        alertDialog.show();

        mView.findViewById(R.id.chancelBTN).setOnClickListener(v -> {
            alertDialog.dismiss();
            finishAndRemoveTask();
        });

        mView.findViewById(R.id.okBTN).setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();


    }

    //Custom Alert Dialog
}