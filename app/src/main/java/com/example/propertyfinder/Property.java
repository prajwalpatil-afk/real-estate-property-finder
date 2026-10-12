package com.example.propertyfinder;

import java.io.Serializable;

/**
 * Model class representing a real estate property.
 * Implements Serializable so it can be passed via Intents if needed.
 */
public class Property implements Serializable {
    private int id;
    private String title;
    private String location;
    private String type; // Flat, House, Villa, Plot
    private int price;
    private int bedrooms;
    private int areaSqft;
    private String description;
    private String contact;
    private int isFavorite; // 0 for false, 1 for true

    // Default constructor
    public Property() {
    }

    // Constructor with all fields
    public Property(int id, String title, String location, String type, int price,
                    int bedrooms, int areaSqft, String description, String contact, int isFavorite) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.type = type;
        this.price = price;
        this.bedrooms = bedrooms;
        this.areaSqft = areaSqft;
        this.description = description;
        this.contact = contact;
        this.isFavorite = isFavorite;
    }

    // Constructor without ID (for inserting new property)
    public Property(String title, String location, String type, int price,
                    int bedrooms, int areaSqft, String description, String contact, int isFavorite) {
        this.title = title;
        this.location = location;
        this.type = type;
        this.price = price;
        this.bedrooms = bedrooms;
        this.areaSqft = areaSqft;
        this.description = description;
        this.contact = contact;
        this.isFavorite = isFavorite;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getAreaSqft() {
        return areaSqft;
    }

    public void setAreaSqft(int areaSqft) {
        this.areaSqft = areaSqft;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return isFavorite == 1;
    }
}
