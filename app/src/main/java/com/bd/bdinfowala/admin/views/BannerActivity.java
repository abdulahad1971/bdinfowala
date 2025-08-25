package com.bd.bdinfowala.admin.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bdinfowala.R;
import com.bd.bdinfowala.databinding.ActivityBannerBinding;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class BannerActivity extends AppCompatActivity {

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hashMap = new HashMap<>();

    MyAdapter myAdapter;

    ActivityBannerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityBannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.add.setOnClickListener(v -> {
            AddBnanerDialog(this);
        });

        // Banner লোড করা
        loadBanerItem();

        // Adapter সেট করা
        myAdapter=new MyAdapter(arrayList);
        binding.recyclerView.setAdapter(myAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // =================== Adapter ===================
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {

        ArrayList<HashMap<String,String>> arrayList;

        public MyAdapter(ArrayList<HashMap<String,String>> arrayList){
            this.arrayList = arrayList;
        }

        public class myViewHolder extends RecyclerView.ViewHolder{
            RoundedImageView bannerImage;
            ImageView edit_icon;

            public myViewHolder(@NonNull View itemView) {
                super(itemView);
                bannerImage = itemView.findViewById(R.id.bannerImage);
                edit_icon = itemView.findViewById(R.id.edit_icon);
            }
        }

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item,parent,false);
            return new myViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
            hashMap = arrayList.get(position);

            String edit_icon_str = hashMap.get("edit_icon");
            int iconResId = Integer.parseInt(edit_icon_str);
            holder.edit_icon.setOnClickListener(v->{
                AddBnanerDialog(BannerActivity.this);
            });
            holder.bannerImage.setImageResource(iconResId);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    // =================== Load Banner ===================
    private void loadBanerItem() {
        hashMap = new HashMap<>();
        hashMap.put("edit_icon", String.valueOf(R.drawable.man));
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("edit_icon", String.valueOf(R.drawable.undraw_secure_login_m11a));
        arrayList.add(hashMap);
    }

    // =================== Add Banner Dialog ===================
    public void AddBnanerDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.add_banner, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
        ImageView edit_icon = dialogView.findViewById(R.id.edit_icon);

        edit_icon.setOnClickListener(v -> {
            Toast.makeText(this, "Edit Banner", Toast.LENGTH_SHORT).show();
        });

        btnUpdate.setOnClickListener(v -> {
            Toast.makeText(this, "Add Banner", Toast.LENGTH_SHORT).show();
        });



        dialog.show();
    }
}