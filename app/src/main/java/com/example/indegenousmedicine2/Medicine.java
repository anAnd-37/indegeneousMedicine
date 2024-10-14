
package com.example.indegenousmedicine2;

public class Medicine {
    private String key;
    private String name;
    private String scientificName;

    // Constructor
    public Medicine(String key, String name, String scientificName) {
        this.key = key;
        this.name = name;
        this.scientificName = scientificName;
    }

    // Getter method for key
    public String getKey() {
        return key;
    }

    // Getter methods for other fields
    public String getName() {
        return name;
    }

    public String getScientificName() {
        return scientificName;
    }
}
