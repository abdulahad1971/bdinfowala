package com.bd.bdinfowala.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bdinfowala.R;
import com.bd.bdinfowala.admin.model.AdminOption;

import java.util.List;

public class AdminOptionAdapter extends RecyclerView.Adapter<AdminOptionAdapter.MyViewHolder> {

    Context context;
    List<AdminOption> list;
    OnItemClickListener listener;

    // Constructor
    public AdminOptionAdapter(Context context, List<AdminOption> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_option, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AdminOption data = list.get(position);

        holder.txtTitle.setText(data.getTitle());
        holder.imgIcon.setImageResource(data.getIcon());

        // Handle click
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            txtTitle = itemView.findViewById(R.id.txtTitle);
        }
    }

    // Interface for click handling
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}

