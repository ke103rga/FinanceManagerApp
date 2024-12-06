package com.example.financemanagerapp.operations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.financemanagerapp.R;
import com.example.financemanagerapp.database.AppDatabase;
import com.example.financemanagerapp.database.Category;
import com.example.financemanagerapp.database.CategoryDao;
import com.example.financemanagerapp.database.OperationDao;

import java.util.List;
import java.util.Objects;

public class FilterActivity extends AppCompatActivity {
    private Spinner categorySpinner;
    private EditText minValueEditText;
    private EditText maxValueEditText;
    private CategoryDao categoryDao;
    private OperationsFilter operationsFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        categorySpinner = findViewById(R.id.category_spinner);
        minValueEditText = findViewById(R.id.min_value_edit_text);
        maxValueEditText = findViewById(R.id.max_value_edit_text);
        Button applyButton = findViewById(R.id.apply_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        // Инициализация DAO
        AppDatabase db = AppDatabase.getDatabase(this);
        categoryDao = db.categoryDao();

        // Получение объекта фильтра из Intent
        operationsFilter = (OperationsFilter) getIntent().getSerializableExtra("filter");

        // Заполнение Spinner
        fillCategorySpinner();

        // Установка значений в поля
        if (operationsFilter != null) {
            if (!Objects.equals(operationsFilter.getMinValue(), operationsFilter.defaultMinValue)) {
                minValueEditText.setText(String.valueOf(operationsFilter.getMinValue()));
            }
            if (!Objects.equals(operationsFilter.getMaxValue(), operationsFilter.defaultMaxValue)) {
                maxValueEditText.setText(String.valueOf(operationsFilter.getMaxValue()));
            }
        }

        applyButton.setOnClickListener(v -> applyFilters());
        cancelButton.setOnClickListener(v -> cancel());
    }


    private int getCategoryPosition(String categoryName) {
        // Посмотрим, где находится выбранная категория
        // Это довольно простой подход для поиска позиции
        // Обновите, чтобы соответствовать вашей реализации класса Category
        for (int i = 0; i < categorySpinner.getAdapter().getCount(); i++) {
            Category category = (Category) categorySpinner.getAdapter().getItem(i);
            if (category.getName().equals(categoryName)) {
                return i;
            }
        }
        return 0; // Вернуть 0, если категория не найдена
    }


    private void fillCategorySpinner() {
        new Thread(() -> {
            List<Category> categories = categoryDao.getAllCategories(); // Получение категорий

            Category newCategory = new Category();
            newCategory.setName("Все категории");
            categories.add(0, newCategory);

            runOnUiThread(() -> {
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);

                // Установка значения категории
                if (operationsFilter != null) {
                    categorySpinner.setSelection(getCategoryPosition(operationsFilter.getCategoryName()));
                }
            });
        }).start();
    }

    private int parseMinValue(String  value) {
        if (value.isEmpty()){
            return new OperationsFilter().defaultMinValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Некорректная минимальная сумма", Toast.LENGTH_SHORT).show();
            minValueEditText.setText("");
            return 0;
        }
    }

    private int parseMaxValue(String  value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return new OperationsFilter().defaultMaxValue; // Возвращаем null или используйте значение по умолчанию
        }
    }

    private void applyFilters() {
        // Получаем введенные значения
        String categoryName = categorySpinner.getSelectedItem().toString();
        String minValueString = minValueEditText.getText().toString();
        String maxValueString = maxValueEditText.getText().toString();
        Integer minValue;
        Integer maxValue;

        if (minValueString.isEmpty()){
            minValue = new OperationsFilter().defaultMinValue;
        }
        else {
            try {
                minValue = Integer.parseInt(minValueString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Некорректная минимальная сумма", Toast.LENGTH_SHORT).show();
                minValueEditText.setText("");
                return;
            }
        }

        if (maxValueString.isEmpty()){
            maxValue = new OperationsFilter().defaultMaxValue;
        }
        else {
            try {
                maxValue = Integer.parseInt(maxValueString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Некорректная максимальная сумма", Toast.LENGTH_SHORT).show();
                maxValueEditText.setText("");
                return;
            }
        }


        // Создаем объект фильтра
        OperationsFilter filter = new OperationsFilter();
        filter.setCategoryName(categoryName);
        filter.setMinValue(minValue);
        filter.setMaxValue(maxValue);

        // Создаем Intent для возврата результата
        Intent resultIntent = new Intent();
        resultIntent.putExtra("filter", filter); // Передаем объект фильтра
        setResult(RESULT_OK, resultIntent); // Устанавливаем результат
        finish(); // Закрываем активность
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish(); // Закрытие активности
    }
}

