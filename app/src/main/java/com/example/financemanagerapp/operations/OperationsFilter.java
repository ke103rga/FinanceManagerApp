package com.example.financemanagerapp.operations;

import java.io.Serializable;

public class OperationsFilter implements Serializable {
    String categoryName;
    int minValue;
    int maxValue;
    String defaultCategoryName = "Все категории";
    int defaultMinValue = -1;
    int defaultMaxValue = Integer.MAX_VALUE;


    public OperationsFilter(){
        categoryName = defaultCategoryName;
        minValue = defaultMinValue;
        maxValue = defaultMaxValue;
    }

    public String getCategoryName() {return categoryName; }

    public int getMinValue() {return minValue; }

    public int getMaxValue() {return maxValue; }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
