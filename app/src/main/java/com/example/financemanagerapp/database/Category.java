package com.example.financemanagerapp.database;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity(tableName = "category",
        foreignKeys = {
                @ForeignKey(entity = OperationType.class,
                        parentColumns = "id",
                        childColumns = "operationsTypeId",
                        onDelete = ForeignKey.CASCADE)
        })
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int operationsTypeId;

    public String name;
    public String desc;
    public String icon;
    public String color;

    public void setName(String name){ this.name = name; }

    public void setDesc(String desc){ this.desc = desc; }

    public void setIcon(String icon){ this.icon = icon; }

    public void setColor(String color){ this.name = color; }

    public String getName() {return name; }

    public int getId() {return id; }

    @Override
    public String toString(){
        return name;
    }





    // Конструкторы, геттеры и сеттеры
}

