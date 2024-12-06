package com.example.financemanagerapp.database;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity(tableName = "operation_types")
public class OperationType {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String color;

    public void setName(String name){ this.name = name; }

    public void setColor(String color){ this.name = color; }
}


