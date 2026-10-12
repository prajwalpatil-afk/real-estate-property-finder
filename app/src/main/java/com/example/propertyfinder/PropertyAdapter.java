package com.example.propertyfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter to display property items in a clean card list.
 */
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    public interface OnPropertyClickListener {
        void onPropertyClick(Property property);
        void onFavoriteClick(Property property, int position);
    }

    private final Context context;
    private List<Property> propertyList = new ArrayList<>();
    private final OnPropertyClickListener listener;
    private final NumberFormat currencyFormatter;

    public PropertyAdapter(Context context, OnPropertyClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        this.currencyFormatter.setMaximumFractionDigits(0);
    }

    public void setProperties(List<Property> properties) {
        this.propertyList = properties != null ? properties : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);

        holder.tvTitle.setText(property.getTitle());
        holder.tvLocation.setText(property.getLocation());
        holder.tvTypeBadge.setText(property.getType());

        // Price formatted with Indian Rupee symbol
        holder.tvPrice.setText(formatIndianCurrency(property.getPrice()));

        // Bedrooms configuration
        if ("Plot".equalsIgnoreCase(property.getType()) || property.getBedrooms() <= 0) {
            holder.tvBedrooms.setText("N/A");
        } else {
            holder.tvBedrooms.setText(property.getBedrooms() + " BHK");
        }

        // Area configuration
        holder.tvArea.setText(property.getAreaSqft() + " sq ft");

        // Favorite icon toggle state
        if (property.getIsFavorite() == 1) {
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

        // Entire card click opens detail activity
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPropertyClick(property);
            }
        });

        // Favorite heart icon click toggles favorite status
        holder.ivFavorite.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFavoriteClick(property, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    /**
     * Formats integer price into Indian Rupees format e.g. ₹ 95,00,000.
     */
    private String formatIndianCurrency(int amount) {
        try {
            return currencyFormatter.format(amount);
        } catch (Exception e) {
            return "₹ " + amount;
        }
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLocation, tvTypeBadge, tvBedrooms, tvArea, tvPrice;
        ImageView ivFavorite;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_property_title);
            tvLocation = itemView.findViewById(R.id.tv_property_location);
            tvTypeBadge = itemView.findViewById(R.id.tv_property_type_badge);
            tvBedrooms = itemView.findViewById(R.id.tv_property_bedrooms);
            tvArea = itemView.findViewById(R.id.tv_property_area);
            tvPrice = itemView.findViewById(R.id.tv_property_price);
            ivFavorite = itemView.findViewById(R.id.iv_property_favorite);
        }
    }
}
