package com.example.indegenousmedicine2;

import com.google.firebase.database.PropertyName;

public class DrugDetail {
    private String scientificName;
    private String vernacularName;
    private String photograph;
    private String medicinalUse;

    // Default constructor required for calls to DataSnapshot.getValue(DrugDetail.class)
    public DrugDetail() {
    }

    public DrugDetail(String scientificName, String vernacularName, String photograph, String medicinalUse) {
        this.scientificName = scientificName;
        this.vernacularName = vernacularName;
        this.photograph = photograph;
        this.medicinalUse = medicinalUse;
    }

    @PropertyName("Scientific name")
    public String getScientificName() {
        return scientificName;
    }

    @PropertyName("Scientific name")
    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    @PropertyName("Vernacular name")
    public String getVernacularName() {
        return vernacularName;
    }

    @PropertyName("Vernacular name")
    public void setVernacularName(String vernacularName) {
        this.vernacularName = vernacularName;
    }

    @PropertyName("Photograph")
    public String getPhotograph() {
        return photograph;
    }

    @PropertyName("Photograph")
    public void setPhotograph(String photograph) {
        this.photograph = photograph;
    }

    @PropertyName("Medicinal use")
    public String getMedicinalUse() {
        return medicinalUse;
    }

    @PropertyName("Medicinal use")
    public void setMedicinalUse(String medicinalUse) {
        this.medicinalUse = medicinalUse;
    }
}
