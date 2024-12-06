package com.example.financemanagerapp.database;

import androidx.room.Ignore;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//public class OperationWithDetails implements Serializable {
//    public Date operationDate;
//    public double operationValue;
//    public String categoryName;
//    public String operationTypeName;
//    public String operationTypeColor;
//    public String operationComment;
//
//    public OperationWithDetails(Date operationDate, double operationValue,
//                                String categoryName,
//                                String operationTypeColor, String operationComment) {
//        this.operationDate = operationDate;
//        this.operationValue = operationValue;
//        this.categoryName = categoryName;
//        this.operationTypeColor = operationTypeColor;
//        this.operationComment = operationComment;
//    }
//}



public class OperationWithDetails implements Serializable {
    private int operationId;
    private Date operationDate;
    private int operationValue;
    private int categoryId;
    private String categoryName;
    private String operationTypeName;
    private String operationTypeColor;
    private String operationComment;


    public OperationWithDetails(int operationId, Date operationDate, int operationValue, int categoryId,
                                String categoryName, String operationTypeName,
                                String operationTypeColor, String operationComment) {
        this.operationId = operationId;
        this.operationDate = operationDate;
        this.operationValue = operationValue;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.operationTypeName = operationTypeName;
        this.operationTypeColor = operationTypeColor;
        this.operationComment = operationComment;
    }

    @Ignore
    public OperationWithDetails(int operationValue,
                                String categoryName,
                                String operationComment) {
        this.operationValue = operationValue;
        this.categoryName = categoryName;
        this.operationComment = operationComment;
    }


    // Геттеры
    public int getOperationID() {return operationId; }

    public Date getOperationDate() {
        return operationDate;
    }

    public int getOperationValue() {
        return operationValue;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getOperationTypeName() {
        return operationTypeName;
    }

    public String getOperationTypeColor() {
        return operationTypeColor;
    }

    public String getOperationComment() {
        return operationComment;
    }

    public int getCategoryId () {return categoryId; }

    // Сеттеры
    public void setOperationID(int id) {this.operationId = id; }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public void setOperationValue(int operationValue) {
        this.operationValue = operationValue;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setOperationTypeName(String operationTypeName) {
        this.operationTypeName = operationTypeName;
    }

    public void setOperationTypeColor(String operationTypeColor) {
        this.operationTypeColor = operationTypeColor;
    }

    public void setOperationComment(String operationComment) {
        this.operationComment = operationComment;
    }

    // Метод для установки даты из строки
    public void setDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            this.operationDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace(); // Здесь можно добавить обработку ошибки
        }
    }

    public String getStringDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return dateFormat.format(operationDate);
    }
}
