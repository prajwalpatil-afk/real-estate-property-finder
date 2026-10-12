package com.example.propertyfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Activity displaying comprehensive details of a selected property,
 * with actions to call owner, toggle favorite, edit, and delete.
 */
public class PropertyDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int propertyId = -1;
    private Property currentProperty;

    // UI elements
    private TextView tvTypeBadge;
    private TextView tvPrice;
    private TextView tvTitle;
    private TextView tvLocation;
    private TextView tvBedrooms;
    private TextView tvArea;
    private TextView tvContact;
    private TextView tvDescription;

    private MaterialButton btnCallOwner;
    private MaterialButton btnFavoriteToggle;
    private MaterialButton btnEditProperty;
    private MaterialButton btnDeleteProperty;

    private NumberFormat currencyFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);

        // Enable Back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.property_details);
        }

        dbHelper = new DatabaseHelper(this);
        currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        currencyFormatter.setMaximumFractionDigits(0);

        propertyId = getIntent().getIntExtra("property_id", -1);
        if (propertyId == -1) {
            Toast.makeText(this, "Invalid property ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPropertyData();
    }

    private void initViews() {
        tvTypeBadge = findViewById(R.id.tv_detail_type_badge);
        tvPrice = findViewById(R.id.tv_detail_price);
        tvTitle = findViewById(R.id.tv_detail_title);
        tvLocation = findViewById(R.id.tv_detail_location);
        tvBedrooms = findViewById(R.id.tv_detail_bedrooms);
        tvArea = findViewById(R.id.tv_detail_area);
        tvContact = findViewById(R.id.tv_detail_contact);
        tvDescription = findViewById(R.id.tv_detail_description);

        btnCallOwner = findViewById(R.id.btn_call_owner);
        btnFavoriteToggle = findViewById(R.id.btn_favorite_toggle);
        btnEditProperty = findViewById(R.id.btn_edit_property);
        btnDeleteProperty = findViewById(R.id.btn_delete_property);
    }

    private void setupListeners() {
        // Call Owner button: opens dialer with phone number (ACTION_DIAL requires NO special permissions)
        btnCallOwner.setOnClickListener(v -> {
            if (currentProperty != null && currentProperty.getContact() != null) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + currentProperty.getContact().trim()));
                startActivity(dialIntent);
            }
        });

        // Favorite Toggle button
        btnFavoriteToggle.setOnClickListener(v -> {
            if (currentProperty != null) {
                int newState = dbHelper.toggleFavorite(currentProperty.getId());
                currentProperty.setIsFavorite(newState);
                updateFavoriteButtonState(newState == 1);
                String msg = (newState == 1) ? "Marked as Favorite" : "Removed from Favorites";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Edit button: opens AddEditPropertyActivity with property ID
        btnEditProperty.setOnClickListener(v -> {
            Intent intent = new Intent(PropertyDetailActivity.this, AddEditPropertyActivity.class);
            intent.putExtra("property_id", propertyId);
            startActivity(intent);
        });

        // Delete button: prompts confirmation dialog before deleting
        btnDeleteProperty.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    /**
     * Loads property details from the database and binds them to UI widgets.
     */
    private void loadPropertyData() {
        currentProperty = dbHelper.getPropertyById(propertyId);
        if (currentProperty == null) {
            Toast.makeText(this, "Property not found or deleted", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitle.setText(currentProperty.getTitle());
        tvLocation.setText(currentProperty.getLocation());
        tvTypeBadge.setText(currentProperty.getType());

        try {
            tvPrice.setText(currencyFormatter.format(currentProperty.getPrice()));
        } catch (Exception e) {
            tvPrice.setText("₹ " + currentProperty.getPrice());
        }

        if ("Plot".equalsIgnoreCase(currentProperty.getType()) || currentProperty.getBedrooms() <= 0) {
            tvBedrooms.setText("N/A");
        } else {
            tvBedrooms.setText(currentProperty.getBedrooms() + " BHK");
        }

        tvArea.setText(currentProperty.getAreaSqft() + " sq ft");
        tvContact.setText(currentProperty.getContact());

        if (currentProperty.getDescription() != null && !currentProperty.getDescription().trim().isEmpty()) {
            tvDescription.setText(currentProperty.getDescription());
        } else {
            tvDescription.setText("No additional description provided.");
        }

        updateFavoriteButtonState(currentProperty.getIsFavorite() == 1);
    }

    private void updateFavoriteButtonState(boolean isFavorite) {
        if (isFavorite) {
            btnFavoriteToggle.setText(R.string.unfavorite);
            btnFavoriteToggle.setIconResource(R.drawable.ic_favorite_filled);
            btnFavoriteToggle.setIconTintResource(R.color.favorite_color);
        } else {
            btnFavoriteToggle.setText(R.string.favorite);
            btnFavoriteToggle.setIconResource(R.drawable.ic_favorite_border);
            btnFavoriteToggle.setIconTintResource(R.color.text_secondary);
        }
    }

    /**
     * Shows a confirmation dialog before deleting property.
     */
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_msg)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    dbHelper.deleteProperty(propertyId);
                    Toast.makeText(this, "Property deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
