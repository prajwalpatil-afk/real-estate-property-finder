package com.example.propertyfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Database helper for managing property records using SQLite.
 * Provides basic CRUD operations and search filtering.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database configuration
    private static final String DATABASE_NAME = "property_finder.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column constants
    public static final String TABLE_PROPERTIES = "properties";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_BEDROOMS = "bedrooms";
    public static final String COLUMN_AREA_SQFT = "area_sqft";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CONTACT = "contact";
    public static final String COLUMN_IS_FAVORITE = "is_favorite";

    // SQL query to create properties table
    private static final String CREATE_TABLE_PROPERTIES = "CREATE TABLE " + TABLE_PROPERTIES + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_LOCATION + " TEXT, "
            + COLUMN_TYPE + " TEXT, "
            + COLUMN_PRICE + " INTEGER, "
            + COLUMN_BEDROOMS + " INTEGER, "
            + COLUMN_AREA_SQFT + " INTEGER, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_CONTACT + " TEXT, "
            + COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table
        db.execSQL(CREATE_TABLE_PROPERTIES);

        // Seed realistic sample properties (Mumbai, Pune, Thane, Navi Mumbai)
        seedSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROPERTIES);
        onCreate(db);
    }

    /**
     * Seeds initial 9 sample properties into the database.
     */
    private void seedSampleData(SQLiteDatabase db) {
        insertSeedProperty(db, "3 BHK Luxury Flat in Baner", "Baner, Pune", "Flat", 9500000, 3, 1450,
                "Spacious 3 BHK apartment with modular kitchen, premium fittings, and panoramic views of Baner hills.", "+91 98201 11223", 0);

        insertSeedProperty(db, "2 BHK Sea-Facing Apartment", "Bandra West, Mumbai", "Flat", 28000000, 2, 980,
                "Prime Bandra location with direct sea view, marble flooring, and 24/7 security near Carter Road.", "+91 98192 33445", 1);

        insertSeedProperty(db, "Independent 4 BHK Villa", "Wakad, Pune", "Villa", 16500000, 4, 2800,
                "Modern gated villa with private lawn, solar heating, and covered parking for 2 cars.", "+91 98670 44556", 0);

        insertSeedProperty(db, "2 BHK Budget Home in Thane", "Ghodbunder Road, Thane", "House", 6500000, 2, 850,
                "Ready-to-move row house in quiet neighborhood, close to Hypercity and express highway.", "+91 97654 55667", 0);

        insertSeedProperty(db, "Residential Corner Plot", "Kharghar, Navi Mumbai", "Plot", 4500000, 0, 1800,
                "Clear title CIDCO transfer plot with approved permissions for G+2 residential construction.", "+91 98223 66778", 0);

        insertSeedProperty(db, "1 BHK Compact Studio Flat", "Kothrud, Pune", "Flat", 4200000, 1, 550,
                "Ideal for students and working professionals, walking distance from metro station and market.", "+91 99210 77889", 0);

        insertSeedProperty(db, "4 BHK Royal Penthouse", "Worli, Mumbai", "Flat", 55000000, 4, 3200,
                "Ultra-luxury high rise penthouse with personal terrace pool, gym access, and 3 parking bays.", "+91 98200 88990", 1);

        insertSeedProperty(db, "3 BHK Duplex House", "Vashi, Navi Mumbai", "House", 14000000, 3, 1950,
                "Well maintained duplex house with private terrace garden, serene surroundings and water purifier.", "+91 98334 99001", 0);

        insertSeedProperty(db, "Commercial & Residential Plot", "Hinjawadi, Pune", "Plot", 7500000, 0, 2400,
                "Strategic plot near IT Phase 1, ideal for rental income building or serviced apartments.", "+91 98901 12345", 0);
    }

    private void insertSeedProperty(SQLiteDatabase db, String title, String location, String type,
                                    int price, int bedrooms, int areaSqft, String description,
                                    String contact, int isFavorite) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_BEDROOMS, bedrooms);
        values.put(COLUMN_AREA_SQFT, areaSqft);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CONTACT, contact);
        values.put(COLUMN_IS_FAVORITE, isFavorite);
        db.insert(TABLE_PROPERTIES, null, values);
    }

    /**
     * Inserts a new property record into the database.
     */
    public long addProperty(Property property) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, property.getTitle());
        values.put(COLUMN_LOCATION, property.getLocation());
        values.put(COLUMN_TYPE, property.getType());
        values.put(COLUMN_PRICE, property.getPrice());
        values.put(COLUMN_BEDROOMS, property.getBedrooms());
        values.put(COLUMN_AREA_SQFT, property.getAreaSqft());
        values.put(COLUMN_DESCRIPTION, property.getDescription());
        values.put(COLUMN_CONTACT, property.getContact());
        values.put(COLUMN_IS_FAVORITE, property.getIsFavorite());

        long id = db.insert(TABLE_PROPERTIES, null, values);
        db.close();
        return id;
    }

    /**
     * Updates an existing property record.
     */
    public int updateProperty(Property property) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, property.getTitle());
        values.put(COLUMN_LOCATION, property.getLocation());
        values.put(COLUMN_TYPE, property.getType());
        values.put(COLUMN_PRICE, property.getPrice());
        values.put(COLUMN_BEDROOMS, property.getBedrooms());
        values.put(COLUMN_AREA_SQFT, property.getAreaSqft());
        values.put(COLUMN_DESCRIPTION, property.getDescription());
        values.put(COLUMN_CONTACT, property.getContact());
        values.put(COLUMN_IS_FAVORITE, property.getIsFavorite());

        int rows = db.update(TABLE_PROPERTIES, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(property.getId())});
        db.close();
        return rows;
    }

    /**
     * Deletes a property record by ID.
     */
    public void deleteProperty(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROPERTIES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * Retrieves all properties ordered by ID descending (newest first).
     */
    public List<Property> getAllProperties() {
        List<Property> propertyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROPERTIES, null, null, null,
                null, null, COLUMN_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                propertyList.add(cursorToProperty(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return propertyList;
    }

    /**
     * Retrieves a single property by its ID.
     */
    public Property getPropertyById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Property property = null;

        Cursor cursor = db.query(TABLE_PROPERTIES, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                property = cursorToProperty(cursor);
            }
            cursor.close();
        }
        db.close();
        return property;
    }

    /**
     * Toggles the favorite status of a property (0 <-> 1).
     * Returns the new favorite state (1 if favorited, 0 if unfavorited).
     */
    public int toggleFavorite(int id) {
        Property property = getPropertyById(id);
        if (property == null) {
            return 0;
        }

        int newFavoriteState = property.getIsFavorite() == 1 ? 0 : 1;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, newFavoriteState);
        db.update(TABLE_PROPERTIES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        return newFavoriteState;
    }

    /**
     * Retrieves all favorite properties.
     */
    public List<Property> getFavorites() {
        List<Property> propertyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROPERTIES, null, COLUMN_IS_FAVORITE + " = 1",
                null, null, null, COLUMN_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                propertyList.add(cursorToProperty(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return propertyList;
    }

    /**
     * Searches and filters properties based on criteria.
     *
     * @param location    search query matched against location or title (optional)
     * @param type        property type filter (e.g. Flat, House, Villa, Plot) or null/"All"
     * @param maxPrice    maximum price filter (0 for no limit)
     * @param minBedrooms minimum bedrooms filter (0 for no limit)
     */
    public List<Property> searchProperties(String location, String type, int maxPrice, int minBedrooms) {
        List<Property> propertyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        // Location text filter
        if (location != null && !location.trim().isEmpty()) {
            selection.append("(").append(COLUMN_LOCATION).append(" LIKE ? OR ")
                    .append(COLUMN_TITLE).append(" LIKE ?)");
            String queryPattern = "%" + location.trim() + "%";
            selectionArgs.add(queryPattern);
            selectionArgs.add(queryPattern);
        }

        // Type filter
        if (type != null && !type.trim().isEmpty() && !type.equalsIgnoreCase("All")) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append(COLUMN_TYPE).append(" = ?");
            selectionArgs.add(type.trim());
        }

        // Max price filter
        if (maxPrice > 0) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append(COLUMN_PRICE).append(" <= ?");
            selectionArgs.add(String.valueOf(maxPrice));
        }

        // Min bedrooms filter
        if (minBedrooms > 0) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append(COLUMN_BEDROOMS).append(" >= ?");
            selectionArgs.add(String.valueOf(minBedrooms));
        }

        String whereClause = selection.length() > 0 ? selection.toString() : null;
        String[] argsArray = selectionArgs.isEmpty() ? null : selectionArgs.toArray(new String[0]);

        Cursor cursor = db.query(TABLE_PROPERTIES, null, whereClause, argsArray,
                null, null, COLUMN_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                propertyList.add(cursorToProperty(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return propertyList;
    }

    /**
     * Helper method to map a Cursor row into a Property object.
     */
    private Property cursorToProperty(Cursor cursor) {
        Property property = new Property();
        property.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        property.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
        property.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
        property.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
        property.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
        property.setBedrooms(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BEDROOMS)));
        property.setAreaSqft(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AREA_SQFT)));
        property.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        property.setContact(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT)));
        property.setIsFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)));
        return property;
    }
}
