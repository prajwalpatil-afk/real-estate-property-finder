package com.example.propertyfinder;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Activity for both adding a new property and editing an existing one.
 */
public class AddEditPropertyActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int propertyId = -1;
    private boolean isEditMode = false;
    private Property existingProperty;

    // UI elements
    private TextInputLayout layoutTitle, layoutLocation, layoutPrice, layoutBedrooms, layoutArea, layoutContact, layoutDescription;
    private EditText etTitle, etLocation, etPrice, etBedrooms, etArea, etContact, etDescription;
    private Spinner spinnerType;
    private MaterialButton btnSave;

    private final String[] typeOptions = {"Flat", "House", "Villa", "Plot"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_property);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupSpinner();

        // Check if intent contains property_id for editing
        propertyId = getIntent().getIntExtra("property_id", -1);
        isEditMode = (propertyId != -1);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? R.string.edit_property : R.string.add_property);
        }

        if (isEditMode) {
            loadExistingProperty();
        }

        btnSave.setOnClickListener(v -> saveProperty());
    }

    private void initViews() {
        layoutTitle = findViewById(R.id.layout_input_title);
        layoutLocation = findViewById(R.id.layout_input_location);
        layoutPrice = findViewById(R.id.layout_input_price);
        layoutBedrooms = findViewById(R.id.layout_input_bedrooms);
        layoutArea = findViewById(R.id.layout_input_area);
        layoutContact = findViewById(R.id.layout_input_contact);
        layoutDescription = findViewById(R.id.layout_input_description);

        etTitle = findViewById(R.id.et_input_title);
        etLocation = findViewById(R.id.et_input_location);
        etPrice = findViewById(R.id.et_input_price);
        etBedrooms = findViewById(R.id.et_input_bedrooms);
        etArea = findViewById(R.id.et_input_area);
        etContact = findViewById(R.id.et_input_contact);
        etDescription = findViewById(R.id.et_input_description);

        spinnerType = findViewById(R.id.spinner_input_type);
        btnSave = findViewById(R.id.btn_save_property);
    }

    private void setupSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                typeOptions
        );
        spinnerType.setAdapter(spinnerAdapter);
    }

    /**
     * If editing, populate inputs with the current property data.
     */
    private void loadExistingProperty() {
        existingProperty = dbHelper.getPropertyById(propertyId);
        if (existingProperty == null) {
            Toast.makeText(this, "Error loading property for edit", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etTitle.setText(existingProperty.getTitle());
        etLocation.setText(existingProperty.getLocation());
        etPrice.setText(String.valueOf(existingProperty.getPrice()));

        if (existingProperty.getBedrooms() > 0) {
            etBedrooms.setText(String.valueOf(existingProperty.getBedrooms()));
        }

        if (existingProperty.getAreaSqft() > 0) {
            etArea.setText(String.valueOf(existingProperty.getAreaSqft()));
        }

        etContact.setText(existingProperty.getContact());
        etDescription.setText(existingProperty.getDescription());

        // Set spinner selection
        for (int i = 0; i < typeOptions.length; i++) {
            if (typeOptions[i].equalsIgnoreCase(existingProperty.getType())) {
                spinnerType.setSelection(i);
                break;
            }
        }

        btnSave.setText("Update Property");
    }

    /**
     * Validates user inputs and writes to SQLite database.
     */
    private void saveProperty() {
        // Reset previous errors
        layoutTitle.setError(null);
        layoutLocation.setError(null);
        layoutPrice.setError(null);
        layoutContact.setError(null);

        String title = etTitle.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String bedroomsStr = etBedrooms.getText().toString().trim();
        String areaStr = etArea.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();

        // Validation
        boolean hasError = false;

        if (title.isEmpty()) {
            layoutTitle.setError("Title is required");
            hasError = true;
        }

        if (location.isEmpty()) {
            layoutLocation.setError("Location is required");
            hasError = true;
        }

        if (priceStr.isEmpty()) {
            layoutPrice.setError("Price is required");
            hasError = true;
        }

        if (contact.isEmpty()) {
            layoutContact.setError("Contact phone is required");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
            if (price <= 0) {
                layoutPrice.setError("Price must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            layoutPrice.setError("Invalid price number");
            return;
        }

        int bedrooms = 0;
        if (!bedroomsStr.isEmpty()) {
            try {
                bedrooms = Integer.parseInt(bedroomsStr);
            } catch (NumberFormatException e) {
                layoutBedrooms.setError("Invalid number");
                return;
            }
        }

        int areaSqft = 0;
        if (!areaStr.isEmpty()) {
            try {
                areaSqft = Integer.parseInt(areaStr);
            } catch (NumberFormatException e) {
                layoutArea.setError("Invalid area number");
                return;
            }
        }

        if (isEditMode) {
            // Update existing property
            existingProperty.setTitle(title);
            existingProperty.setLocation(location);
            existingProperty.setType(type);
            existingProperty.setPrice(price);
            existingProperty.setBedrooms(bedrooms);
            existingProperty.setAreaSqft(areaSqft);
            existingProperty.setContact(contact);
            existingProperty.setDescription(description);

            dbHelper.updateProperty(existingProperty);
            Toast.makeText(this, "Property updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Add new property
            Property newProperty = new Property(title, location, type, price, bedrooms,
                    areaSqft, description, contact, 0);
            dbHelper.addProperty(newProperty);
            Toast.makeText(this, "Property added successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
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
