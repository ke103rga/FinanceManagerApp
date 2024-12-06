package com.example.financemanagerapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface OperationDao {
    @Insert
    long insert(Operation operation);

    @Update
    void update(Operation operation);

    @Query("DELETE FROM operations WHERE id = :operationId")
    void deleteById(int operationId);

    @Query("DELETE FROM operations")
    void deleteAllOperations();

    @Query("SELECT * FROM operations")
    List<Operation> getAllOperations();

    @Query("SELECT " +
            "   o.id as operationId, " +
            "   o.date as operationDate, " +
            "   o.value as operationValue, " +
            "   c.id as categoryId, c.name as categoryName, " +
            "   ot.color as operationTypeColor, " +
            "   o.comment as operationComment " +
            "FROM operations o " +
            "   JOIN category c ON o.categoryId = c.id " +
            "   JOIN operation_types ot ON c.operationsTypeId = ot.id " +
            "WHERE 1 = 1" +
            "   and o.date >= :minDate AND o.date <= :maxDate " +
            "   and (:categoryName = 'Все категории' or c.name = :categoryName) " +
            "   and o.value >= :minValue " +
            "   and o.value <= :maxValue " +
            "order by o.date desc ")
    List<OperationWithDetails> getAllOperationsWithDetails(
            Date minDate, Date maxDate, String categoryName,
            int minValue, int maxValue);

    @Query("SELECT coalesce(SUM(value), 0) " +
            "FROM operations o " +
            "   Inner JOIN category c ON o.categoryId = c.id " +
            "   Inner JOIN operation_types ot ON c.operationsTypeId = ot.id " +
            "WHERE ot.name = 'Расход' AND o.date >= :minDate AND o.date <= :maxDate")
    Double getTotalExpense(Date minDate, Date maxDate);

    @Query("SELECT coalesce(SUM(value), 0) " +
            "FROM operations o " +
            "   Inner JOIN category c ON o.categoryId = c.id " +
            "   Inner JOIN operation_types ot ON c.operationsTypeId = ot.id " +
            "WHERE ot.name = 'Доход' AND o.date >= :minDate AND o.date <= :maxDate")
    Double getTotalIncome(Date minDate, Date maxDate);

    @Query("with period_values as (" +
            "   select " +
            "       o.id, " +
            "       o.value, " +
            "       case " +
            "           when :period = 'date' then Date(DATETIME(date / 1000, 'unixepoch')) " +
            "           when :period = 'week' then strftime('%W', DATETIME(date / 1000, 'unixepoch')) " +
            "           when :period = 'month' then strftime('%Y-%m', DATETIME(date / 1000, 'unixepoch')) " +
            "           else date " +
            "       end as period_value " +
            "FROM operations o " +
            "   JOIN category c ON o.categoryId = c.id " +
            "   JOIN operation_types ot ON c.operationsTypeId = ot.id " +
            "WHERE ot.name = 'Расход' AND o.date >= :minDate AND o.date <= :maxDate" +
            ") " +
            "select coalesce(avg(total_period_value), 0) " +
            "from (" +
            "   select period_value, SUM(value) as total_period_value" +
            "   from period_values " +
            "   group by period_value" +
            ")")
    Double getAverageExpense(Date minDate, Date maxDate, String period);
}
