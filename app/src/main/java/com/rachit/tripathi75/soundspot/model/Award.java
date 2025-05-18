package com.rachit.tripathi75.soundspot.model;

// Award.java

public class Award {
    private String name;
    private String category;
    private int year;

    public Award(String name, String category, int year) {
        this.name = name;
        this.category = category;
        this.year = year;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // Helper method
    public String getFormattedAward() {
        return name + " (" + year + ")";
    }
}