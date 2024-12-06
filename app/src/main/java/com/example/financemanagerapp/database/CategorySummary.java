package com.example.financemanagerapp.database;

public class CategorySummary {
    public String categoryName;
    public double catTotalOperations;
    public double catTotalValue;
    public double percentage;
    public String categoryColor;
    public String categoryIcon;

    public CategorySummary(String categoryName, String categoryColor, String categoryIcon, double catTotalOperations, double catTotalValue, double percentage) {
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.categoryIcon = categoryIcon;
        this.catTotalOperations = catTotalOperations;
        this.catTotalValue = catTotalValue;
        this.percentage = percentage;
    }

    // Геттеры
    public String getCategoryName() {
        return categoryName;
    }

    public double getCatTotalOperations() {
        return catTotalOperations;
    }

    public double getCatTotalValue() {
        return catTotalValue;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    // Сеттеры
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCatTotalOperations(double catTotalOperations) {
        this.catTotalOperations = catTotalOperations;
    }

    public void setCatTotalValue(double catTotalValue) {
        this.catTotalValue = catTotalValue;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryColor = categoryIcon;
    }
}