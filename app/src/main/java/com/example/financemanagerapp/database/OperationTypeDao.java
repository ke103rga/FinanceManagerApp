package com.example.financemanagerapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OperationTypeDao {
    @Insert
    long insert(OperationType operationType);

    @Query("DELETE FROM operation_types")
    void deleteAllOperationTypes();

    @Query("SELECT * FROM operation_types")
    List<OperationType> getAllOperationTypes();
}
