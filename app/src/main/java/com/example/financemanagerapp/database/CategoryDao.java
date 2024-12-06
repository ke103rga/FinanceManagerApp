package com.example.financemanagerapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    long insert(Category category);

    @Query("DELETE FROM category")
    void deleteAllCategories();

    @Query("SELECT * FROM category")
    List<Category> getAllCategories();

    @Query("With cats_totals as (" +
            "SELECT " +
            "   c.name AS categoryName, " +
            "   c.color as categoryColor, " +
            "   c.icon as categoryIcon, " +
            "   coalesce(SUM(o.value), 0) AS catTotalValue, " +
            "   coalesce(COUNT(o.value), 0) AS catTotalOperations " +
            "FROM category c " +
            "   JOIN operation_types ot on  c.operationsTypeId = ot.id and ot.name = 'Расход' " +
            "   LEFT JOIN (select * from operations where date >= :minDate AND date <= :maxDate) o ON c.id = o.categoryId " +
            "GROUP BY c.id" +
            ") " +
            "select categoryName, categoryColor, categoryIcon, catTotalValue, catTotalOperations,  " +
            "   (catTotalValue * 1.0) / (totalValue * 1.0) as percentage " +
            "from cats_totals ct" +
            "   join (select sum(catTotalValue) as totalValue from cats_totals) total " +
            "where  catTotalValue / totalValue >= :minPercent " +
            "order by (catTotalValue * 1.0) / (totalValue * 1.0) desc")
    List<CategorySummary> getCategorySummaries(Date minDate, Date maxDate, double minPercent);
}
