package com.example.financemanagerapp.database;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "operations",
        foreignKeys = {
                @ForeignKey(entity = Category.class,
                        parentColumns = "id",
                        childColumns = "categoryId",
                        onDelete = ForeignKey.CASCADE)
        })
public class Operation {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int categoryId;   // FK к Category

    public Date date;
    public int value;
    public String comment;

    // Конструкторы, геттеры и сеттеры

    // Геттеры
    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Date getDate() {
        return date;
    }

    public int getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }

    // Сеттеры
    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDateFromString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            this.date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace(); // Обработка ошибки парсинга
        }
    }
}


