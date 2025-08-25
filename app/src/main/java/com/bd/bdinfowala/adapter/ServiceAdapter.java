package com.bd.bdinfowala.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bdinfowala.R;
import com.bd.bdinfowala.model.Service;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Service> serviceList;
    private Context context;

    public ServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);

        // Bind data from new Service model
        holder.tvName.setText(service.getServiceName());
        holder.description.setText(service.getDescription());
        holder.tvDiscountPrice.setText("৳ " + service.getPrice());

        // Optionally show features or days
        // holder.tvFeatures.setText(service.getFeatures());
        // holder.tvDays.setText(service.getDays());



        Glide.with(context)
                .load(service.getImageUrl())
                .placeholder(R.drawable.download) // placeholder image
                .into(holder.imgProduct);

        // Example favorite click
        holder.ivFavorite.setOnClickListener(v ->
                Toast.makeText(context, "Favorite clicked for " + service.getServiceName(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgProduct;
        TextView tvName, description, tvDiscountPrice;
        ImageView ivFavorite;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvName = itemView.findViewById(R.id.tv_name);
            description = itemView.findViewById(R.id.description);
            tvDiscountPrice = itemView.findViewById(R.id.tv_discount_price);
            ivFavorite = itemView.findViewById(R.id.iv_favorite);
        }
    }
}
