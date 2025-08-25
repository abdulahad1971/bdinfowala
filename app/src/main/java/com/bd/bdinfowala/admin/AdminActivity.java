package com.bd.bdinfowala.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bdinfowala.R;
import com.bd.bdinfowala.admin.adapter.AdminOptionAdapter;
import com.bd.bdinfowala.admin.model.AdminOption;
import com.bd.bdinfowala.admin.views.AddCategoryActivity;
import com.bd.bdinfowala.databinding.ActivityAdminBinding;
import com.bd.bdinfowala.view.auth.LoginActivity;
import com.bd.bdinfowala.view.auth.seassion.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding binding;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = new SessionManager(this);

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.clearSession();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 column

        List<AdminOption> options = new ArrayList<>();
        options.add(new AdminOption("Order", R.drawable.order));
        options.add(new AdminOption("Add Products", R.drawable.order));
        options.add(new AdminOption("Categories", R.drawable.categories));
        options.add(new AdminOption("Banner", R.drawable.slider));
        options.add(new AdminOption("Service", R.drawable.service));
        options.add(new AdminOption("User", R.drawable.user));


        AdminOptionAdapter adapter = new AdminOptionAdapter(this, options, position -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(this, AddCategoryActivity.class));
                    break;
                case 1:
//                    startActivity(new Intent(this, AddProductsActivity.class));
                    break;
                case 2:
//                    startActivity(new Intent(this, ViewCategoryActivity.class));
                    break;

                case 3:
//                    startActivity(new Intent(this, ImageSliderActivity.class));
                    break;

                case 4:
//                    startActivity(new Intent(this, UserListActivity.class));
                    break;

                case 5:
//                    startActivity(new Intent(this, OrderListActivity.class));
                    break;
                case 6:
//                    new SessionManager(this).logout();
                    startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                    break;
            }
        });

        recyclerView.setAdapter(adapter);




    }
}