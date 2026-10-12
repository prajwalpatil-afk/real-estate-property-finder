package com.example.propertyfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Main Activity displaying property search filters and the list of properties.
 */
public class MainActivity extends AppCompatActivity implements PropertyAdapter.OnPropertyClickListener {

    private DatabaseHelper dbHelper;
    private PropertyAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout layoutEmptyState;
    private TextView tvFilterStatus;

    // Filter UI elements
    private EditText etSearchLocation;
    private Spinner spinnerType;
    private EditText etMaxPrice;
    private EditText etMinBedrooms;
    private MaterialButton btnSearch;
    private MaterialButton btnReset;
    private FloatingActionButton fabAddProperty;

    // State
    private boolean isFavoritesOnly = false;
    private final String[] propertyTypes = {"All", "Flat", "House", "Villa", "Plot"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SQLite Database Helper
        dbHelper = new DatabaseHelper(this);

        initViews();
        setupSpinner();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_properties);
        layoutEmptyState = findViewById(R.id.layout_empty_state);
        tvFilterStatus = findViewById(R.id.tv_filter_status);

        etSearchLocation = findViewById(R.id.et_search_location);
        spinnerType = findViewById(R.id.spinner_type);
        etMaxPrice = findViewById(R.id.et_max_price);
        etMinBedrooms = findViewById(R.id.et_min_bedrooms);
        btnSearch = findViewById(R.id.btn_search);
        btnReset = findViewById(R.id.btn_reset);
        fabAddProperty = findViewById(R.id.fab_add_property);
    }

    private void setupSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                propertyTypes
        );
        spinnerType.setAdapter(spinnerAdapter);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PropertyAdapter(this, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        // Search button click
        btnSearch.setOnClickListener(v -> performSearch());

        // Reset button click
        btnReset.setOnClickListener(v -> resetFilters());

        // FAB to add new property
        fabAddProperty.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditPropertyActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh property list when returning from Add/Edit/Detail screens
        if (isFavoritesOnly) {
            loadFavorites();
        } else {
            performSearch();
        }
    }

    /**
     * Executes the search and filter query against SQLite.
     */
    private void performSearch() {
        String location = etSearchLocation.getText().toString().trim();
        String type = spinnerType.getSelectedItem() != null ? spinnerType.getSelectedItem().toString() : "All";

        int maxPrice = 0;
        String maxPriceStr = etMaxPrice.getText().toString().trim();
        if (!maxPriceStr.isEmpty()) {
            try {
                maxPrice = Integer.parseInt(maxPriceStr);
            } catch (NumberFormatException ignored) {}
        }

        int minBedrooms = 0;
        String minBhkStr = etMinBedrooms.getText().toString().trim();
        if (!minBhkStr.isEmpty()) {
            try {
                minBedrooms = Integer.parseInt(minBhkStr);
            } catch (NumberFormatException ignored) {}
        }

        List<Property> properties;
        if (isFavoritesOnly) {
            properties = dbHelper.getFavorites();
        } else {
            properties = dbHelper.searchProperties(location, type, maxPrice, minBedrooms);
        }

        updateListUI(properties);
    }

    /**
     * Resets all search filters and reloads all properties.
     */
    private void resetFilters() {
        etSearchLocation.setText("");
        spinnerType.setSelection(0);
        etMaxPrice.setText("");
        etMinBedrooms.setText("");
        isFavoritesOnly = false;
        tvFilterStatus.setVisibility(View.GONE);
        invalidateOptionsMenu();

        List<Property> properties = dbHelper.getAllProperties();
        updateListUI(properties);
    }

    /**
     * Loads only favorite properties.
     */
    private void loadFavorites() {
        List<Property> favorites = dbHelper.getFavorites();
        updateListUI(favorites);
    }

    private void updateListUI(List<Property> properties) {
        if (properties == null || properties.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
            adapter.setProperties(properties);
        }
    }

    // Options Menu for Favorites filter toggle
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem favItem = menu.findItem(R.id.action_favorites);
        if (favItem != null) {
            favItem.setTitle(isFavoritesOnly ? R.string.all_properties : R.string.favorites_only);
            favItem.setIcon(isFavoritesOnly ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorites) {
            isFavoritesOnly = !isFavoritesOnly;
            invalidateOptionsMenu();

            if (isFavoritesOnly) {
                tvFilterStatus.setText("Showing: Favorites Only");
                tvFilterStatus.setVisibility(View.VISIBLE);
                loadFavorites();
                Toast.makeText(this, "Showing favorite properties", Toast.LENGTH_SHORT).show();
            } else {
                tvFilterStatus.setVisibility(View.GONE);
                performSearch();
                Toast.makeText(this, "Showing all properties", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Property item clicked -> open PropertyDetailActivity
    @Override
    public void onPropertyClick(Property property) {
        Intent intent = new Intent(MainActivity.this, PropertyDetailActivity.class);
        intent.putExtra("property_id", property.getId());
        startActivity(intent);
    }

    // Favorite heart clicked on item card -> toggle favorite in DB
    @Override
    public void onFavoriteClick(Property property, int position) {
        int newState = dbHelper.toggleFavorite(property.getId());
        property.setIsFavorite(newState);

        if (isFavoritesOnly) {
            loadFavorites();
        } else {
            adapter.notifyItemChanged(position);
        }

        String msg = (newState == 1) ? "Added to favorites" : "Removed from favorites";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
