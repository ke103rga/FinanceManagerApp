package com.example.financemanagerapp.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseInitializer {

    public static void clearDatabase(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "app_database").build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            db.operationDao().deleteAllOperations();
            db.categoryDao().deleteAllCategories();
            db.operationTypeDao().deleteAllOperationTypes();
        });
    }

    public static void populateDatabase(Context context, Boolean addOperations) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "app_database").build();

        // Список категорий
        String[] incomeCategories = {"Зарплата", "Прочий доход"};
        String[] incomeCategoriesColors = {"Зарплата", "Стипендия"};
        String[] incomeCategoriesIons = {"salary_icon", "monetization_on"};
        String[] expenseCategories = {"Продукты", "Кафе", "Досуг", "Транспорт", "Здоровье", "Покупки", "Подарки"};
        String[] expenseCategoriesColors = {"#3fa2ff", "#3e50b4", "#ff4586", "#feaf4a", "#62C474", "#7a5b50", "#ff5f5d"};
        String[] expenseCategoriesIcons = {"products_icon", "cafe_icon", "leisure_icon", "transport_icon", "health_icon", "purchases_icon", "gifts_icon"};
        String[] operationTypes = {"Доход", "Расход"};
        String[] operationTypesColors = {"#249D3A", "#DF2E3D"};

        // Добавляем типы операций и сохраняем их ID
        List<Integer> operationTypeIds = new ArrayList<>();
        for (int i = 0; i < operationTypes.length; i++) {
            OperationType operationType = new OperationType();
            operationType.name = operationTypes[i];
            operationType.color = operationTypesColors[i];
            // Сохраняем ID после вставки
            long id = db.operationTypeDao().insert(operationType);
            operationTypeIds.add((int) id);
        }

        // Добавляем категории Дохода и сохраняем их ID
        List<Integer> incomeCategoryIds = new ArrayList<>();
        for (int i = 0; i < incomeCategories.length; i++) {
            Category category = new Category();
            category.name = incomeCategories[i];
            category.operationsTypeId = operationTypeIds.get(0); // Используйте ID дохоа
            category.icon = incomeCategoriesIons[i];
            category.color = incomeCategoriesColors[i];
            long id = db.categoryDao().insert(category);
            incomeCategoryIds.add((int) id);
        }

        // Добавляем категории Расхода и сохраняем их ID
        List<Integer> expenseCategoryIds = new ArrayList<>();
        for (int i = 0; i < expenseCategories.length; i++) {
            Category category = new Category();
            category.name = expenseCategories[i];
            category.operationsTypeId = operationTypeIds.get(1); // Используйте ID расходов
            category.icon = expenseCategoriesIcons[i];
            category.color = expenseCategoriesColors[i];
            long id = db.categoryDao().insert(category);
            expenseCategoryIds.add((int) id);
        }

        if(addOperations){
            // Добавляем 50 операций за последние 2 месяца
            for (int i = 0; i < 50; i++) {
                Operation operation = new Operation();
                operation.categoryId = expenseCategoryIds.get(i % expenseCategoryIds.size()); // Идём по кругу по категориям расходов
                operation.date = getDate(i); // Метод для получения даты
                operation.value = ((int) (Math.random() * 1000)); // Случайная сумма
                operation.comment = "Тестовая операция " + (i + 1);
                db.operationDao().insert(operation);
            }

            // Добавляем операции дохода
            for (int i = 0; i < 5; i++) {
                Operation operation = new Operation();
                operation.categoryId = incomeCategoryIds.get(i % incomeCategoryIds.size()); // Идём по кругу по категориям дохода
                operation.date = getDate(i); // Метод для получения даты
                operation.value = ((int) (Math.random() * 10000)); // Случайная сумма
                operation.comment = "Тестовая операция Дохода " + (i + 1);
                db.operationDao().insert(operation);
            }
        }
        Log.i("DB", "DB POPULATED");
    }


    private static Date getDate(int daysAgo) {
        // Вы можете использовать простой способ получения даты
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo);
        return calendar.getTime();
    }
}

