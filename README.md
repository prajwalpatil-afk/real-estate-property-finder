# Real Estate Property Finder (Mobile Computing Mini Project)

A clean, lightweight, beginner-friendly Android application designed for a college mini project viva. Built purely with native Android Java and built-in SQLite, following standard Android architectural practices.

---

## 📌 Project Overview
- **App Name:** Property Finder
- **Package Name:** `com.example.propertyfinder`
- **Language:** Java (Android SDK 34)
- **Database:** Local SQLite (`SQLiteOpenHelper`)
- **UI Framework:** Material Components & XML layouts with `RecyclerView`
- **Architecture:** Standard Model-View-Adapter with local SQLite database layer

---

## ✨ Features
1. **Browse Property Listings:**
   - Displays real estate cards with Title, Location, Configuration (BHK), Area (sq ft), Formatted Price in INR (₹), and a Favorite bookmark icon.
   - Pre-seeded with 9 realistic properties from Indian cities (Mumbai, Pune, Thane, Navi Mumbai).

2. **Search & Multi-criteria Filtering:**
   - Text search matching city/area or title (e.g., "Pune", "Bandra").
   - Filter by property type: **All, Flat, House, Villa, Plot**.
   - Maximum budget (₹) filter.
   - Minimum BHK configuration filter.
   - One-tap **Reset** button to restore full listing.

3. **Favorites Management:**
   - Toggle property favorites directly from the listing heart icon or from the detail screen.
   - Top action bar toggle to filter and view only Favorite properties.

4. **Detailed Property View:**
   - High-level property badge and full specifications.
   - **Call Owner:** Direct dialer action using `Intent.ACTION_DIAL` (safe, requires zero runtime permissions).
   - **Favorite / Unfavorite** button.
   - **Edit** button to modify information.
   - **Delete** button with confirmation alert dialog.

5. **Add & Edit Property:**
   - Unified form with validation checks (checks required fields and positive numeric values).
   - Immediate SQLite update and automatic list refresh upon return.

---

## 🗄️ Database Schema

- **Database Name:** `property_finder.db` (Version 1)
- **Table:** `properties`

| Column Name | Data Type | Constraints | Description |
|---|---|---|---|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique property ID |
| `title` | TEXT | NOT NULL | Property name/headline |
| `location` | TEXT | NOT NULL | City or neighborhood area |
| `type` | TEXT | NOT NULL | Flat, House, Villa, or Plot |
| `price` | INTEGER | NOT NULL | Price in INR (₹) |
| `bedrooms` | INTEGER | DEFAULT 0 | Number of bedrooms (BHK) |
| `area_sqft` | INTEGER | DEFAULT 0 | Built-up area in square feet |
| `description` | TEXT | | Detailed property description |
| `contact` | TEXT | NOT NULL | Owner/Agent phone number |
| `is_favorite` | INTEGER | DEFAULT 0 | 0 = Normal, 1 = Favorite |

---

## 📱 Screen List (Activities)

1. **`MainActivity` (`activity_main.xml`):**
   - Location search input, property type dropdown, price/BHK filters, search and reset buttons.
   - `RecyclerView` showing list of property cards.
   - Empty state message displayed when no properties match criteria.
   - Floating Action Button (FAB) to add a new property.
   - Top App Bar menu item to switch between All Properties and Favorites.

2. **`PropertyDetailActivity` (`activity_property_detail.xml`):**
   - Displays all property fields with clear visual hierarchy.
   - Actions: Call Owner (launches phone dialer), Favorite/Unfavorite, Edit, and Delete (with confirmation).

3. **`AddEditPropertyActivity` (`activity_add_edit_property.xml`):**
   - Input form with validation for title, location, type, price, bedrooms, area, phone, and description.
   - Handles both adding new records and editing existing records.

---

## 🛠️ Project Structure
```
RealEstatePropertyFinder/
├── app/
│   ├── build.gradle
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/propertyfinder/
│       │   ├── AddEditPropertyActivity.java
│       │   ├── DatabaseHelper.java
│       │   ├── MainActivity.java
│       │   ├── Property.java
│       │   ├── PropertyAdapter.java
│       │   └── PropertyDetailActivity.java
│       └── res/
│           ├── drawable/
│           │   ├── ic_add.xml
│           │   ├── ic_bed.xml
│           │   ├── ic_call.xml
│           │   ├── ic_delete.xml
│           │   ├── ic_edit.xml
│           │   ├── ic_favorite_border.xml
│           │   ├── ic_favorite_filled.xml
│           │   ├── ic_location.xml
│           │   ├── ic_property_placeholder.xml
│           │   ├── ic_search.xml
│           │   └── ic_square_foot.xml
│           ├── layout/
│           │   ├── activity_add_edit_property.xml
│           │   ├── activity_main.xml
│           │   ├── activity_property_detail.xml
│           │   └── item_property.xml
│           ├── menu/
│           │   └── menu_main.xml
│           └── values/
│               ├── colors.xml
│               ├── strings.xml
│               └── themes.xml
├── build.gradle
├── gradle.properties
├── settings.gradle
├── local.properties
└── README.md
```

---

## 💻 How to Open and Run in Android Studio

1. Open **Android Studio**.
2. Click **File -> Open...** and select the folder:
   `c:\Users\PRAJWAL PATIL\OneDrive\Desktop\RealEstatePropertyFinder`
3. Allow Android Studio to sync the Gradle project automatically.
   *(Android Studio includes its own bundled JDK 17/21 and Android SDK manager).*
4. Connect an Android phone (via USB with USB Debugging enabled) or start an Android Emulator.
5. Click the green **Run (▶)** button or press `Shift + F10` to build and run the app.

---

## 📲 How to Install the APK on Your Android Phone

1. Build the APK inside Android Studio:
   - Click **Build -> Build Bundle(s) / APK(s) -> Build APK(s)**.
   - Or open terminal in project directory and run: `gradlew assembleDebug`.
2. The generated APK will be located at:
   `app/build/outputs/apk/debug/app-debug.apk` (or project root `PropertyFinder-debug.apk`).
3. **Transfer to your phone:**
   - Copy the `.apk` file to your phone using a USB cable, WhatsApp, Google Drive, or Bluetooth.
4. **Enable Installation:**
   - On your phone, open the APK file from File Manager or Downloads.
   - When prompted with *"For your security, your phone is not allowed to install unknown apps from this source"*, tap **Settings** and turn ON **"Allow from this source"**.
5. Tap **Install** and then **Open**.
