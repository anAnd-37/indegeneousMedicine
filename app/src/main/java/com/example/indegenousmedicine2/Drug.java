package com.example.indegenousmedicine2;

import java.util.ArrayList;

public class Drug {
    private String name;
    private String scientificName;
    private String howToApply;
    private String medicinalPlants;
    private String modeOfPreparation;
    private boolean isViable;
    private int yearsUsedSince;
    private int livingAreaSince;
    private ArrayList<String> imageUrls; // List of image URLs

    // Constructors, getters, and setters
    // Constructor
    public Drug(String name, String scientificName, String howToApply, String medicinalPlants, String modeOfPreparation, boolean isViable, int yearsUsedSince, int livingAreaSince, ArrayList<String> imageUrls) {
        this.name = name;
        this.scientificName = scientificName;
        this.howToApply = howToApply;
        this.medicinalPlants = medicinalPlants;
        this.modeOfPreparation = modeOfPreparation;
        this.isViable = isViable;
        this.yearsUsedSince = yearsUsedSince;
        this.livingAreaSince = livingAreaSince;
        this.imageUrls = imageUrls;
    }

    // Getters and setters
    // For image URLs
    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}

//public class Drug {
//    private String id;
//    private String scientificName;
//    private String howToApply;
//    private String livingSince;
//    // Other fields as needed
//
//    // Constructor, getters, and setters
//    public Drug() {
//        // Default constructor required for Firebase
//    }
//
//    public Drug(String id, String scientificName, String howToApply, String livingSince) {
//        this.id = id;
//        this.scientificName = scientificName;
//        this.howToApply = howToApply;
//        this.livingSince = livingSince;
//        // Initialize other fields
//    }
//
//    // Getters and setters
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getScientificName() {
//        return scientificName;
//    }
//
//    public void setScientificName(String scientificName) {
//        this.scientificName = scientificName;
//    }
//
//    public String getHowToApply() {
//        return howToApply;
//    }
//
//    public void setHowToApply(String howToApply) {
//        this.howToApply = howToApply;
//    }
//
//    public String getLivingSince() {
//        return livingSince;
//    }
//
//    public void setLivingSince(String livingSince) {
//        this.livingSince = livingSince;
//    }
//}
