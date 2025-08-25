package com.bd.bdinfowala.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bdinfowala.R;
import com.bd.bdinfowala.admin.model.AdminService;
import com.bd.bdinfowala.constants.Urls;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class AdminServiceAdapter extends RecyclerView.Adapter<AdminServiceAdapter.ServiceViewHolder> {

    private Context context;
    private List<AdminService> serviceList;

    public AdminServiceAdapter(Context context, List<AdminService> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        AdminService service = serviceList.get(position);

        holder.tvServiceName.setText(service.getServiceName());
        holder.tvCategory.setText("Category: " + service.getCategoryName());
        holder.tvPrice.setText("Price: " + service.getPrice());
        holder.tvDays.setText("Days: " + service.getDays());

        // Parse requirement JSON
        try {
            JSONArray reqArray = new JSONArray(service.getRequirementJson());
            StringBuilder reqBuilder = new StringBuilder();
            for (int i = 0; i < reqArray.length(); i++) {
                JSONObject obj = reqArray.getJSONObject(i);
                String label = obj.getString("label");
                boolean required = obj.getBoolean("required");
                reqBuilder.append(label);
                if (required) reqBuilder.append(" (required)");
                if (i != reqArray.length() - 1) reqBuilder.append(", ");
            }
            holder.tvRequirements.setText("Requirements: " + reqBuilder.toString());
        } catch (Exception e) {
            holder.tvRequirements.setText("Requirements: N/A");
        }

        // Load Image
        if (service.getImageUrl() != null && !service.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(Urls.imageUrl +"uploads/images/"+ service.getImageUrl())
                    .placeholder(R.drawable.balloon)
                    .into(holder.ivServiceImage);
        } else {
            holder.ivServiceImage.setImageResource(R.drawable.balloon);
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvCategory, tvPrice, tvDays, tvRequirements;
        RoundedImageView ivServiceImage;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDays = itemView.findViewById(R.id.tvDays);
            tvRequirements = itemView.findViewById(R.id.tvRequirements);
            ivServiceImage = itemView.findViewById(R.id.ivServiceImage);
        }
    }

    // Update list for search/filter
    public void updateList(List<AdminService> filteredList) {
        serviceList = filteredList;
        notifyDataSetChanged();
    }
}
