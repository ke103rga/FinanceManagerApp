package com.example.financemanagerapp.operations;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financemanagerapp.R;
import com.example.financemanagerapp.database.AppDatabase;
import com.example.financemanagerapp.database.Category;
import com.example.financemanagerapp.database.CategoryDao;
import com.example.financemanagerapp.database.Operation;
import com.example.financemanagerapp.database.OperationDao;
import com.example.financemanagerapp.database.OperationWithDetails;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditOperationActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private Button dateButton;
    private EditText valueEditText;
    private EditText commentEditText;

    private CategoryDao categoryDao;
    private OperationDao operationDao;

    String activityGoal;
    OperationWithDetails operationWithDetails;

    String selectedDate;
    ArrayAdapter<Category> categoryArrayAdapter;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_operation);

        // Инициализация DAO
        AppDatabase db = AppDatabase.getDatabase(this);
        categoryDao = db.categoryDao();
        operationDao = db.operationDao();

        TextView activityTitleView = findViewById(R.id.activity_title);
        categorySpinner = findViewById(R.id.category_spinner);
        dateButton = findViewById(R.id.date_button);
        valueEditText = findViewById(R.id.value_edit);
        commentEditText = findViewById(R.id.comment_edit);
        Button saveButton = findViewById(R.id.save_operation_button);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button deleteButton = findViewById(R.id.delete_button);

        // Выясняем цель открытия активности (редактирование или добавление)
        activityGoal = getIntent().getStringExtra("activityGoal");
        // Если целью было редактирование то устанавливаем существующие значения полей
        if (activityGoal.equals("EDIT")){
            activityTitleView.setText("Редактирование операции");
            operationWithDetails = (OperationWithDetails) getIntent().getSerializableExtra("operation");
            dateButton.setText(operationWithDetails.getStringDate());
            valueEditText.setText(String.valueOf(operationWithDetails.getOperationValue()));
            commentEditText.setText(operationWithDetails.getOperationComment());
            fillCategorySpinner(true);
        }
        // Иначе все поля остаются пустыми, заполняется только список категорий
        else if (activityGoal.equals("ADD")){
            deleteButton.setVisibility(View.GONE);
            activityTitleView.setText("Добавление операции");
            fillCategorySpinner(false);
        }

        dateButton.setOnClickListener(v -> openDatePicker());

        saveButton.setOnClickListener(this::saveOperation);
        deleteButton.setOnClickListener(this::deleteOperation);
        cancelButton.setOnClickListener(v -> finish()); // Закрытие активности
    }

    private void saveOperation(View v) {
        // Получение введенных данных
        String date = dateButton.getText().toString();
        String valueString = valueEditText.getText().toString();
        String comment = commentEditText.getText().toString();

        // Проверка корректности ввода данных
        if (date.isEmpty() | date.equals("Выбрать дату")) {
            Toast.makeText(this, "Введите корректную дату", Toast.LENGTH_SHORT).show();
            return;
        }

        if (valueString.isEmpty()) {
            Toast.makeText(this, "Введите сумму", Toast.LENGTH_SHORT).show();
            return;
        }

        int value;
        try {
            value = Integer.parseInt(valueString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Некорректная сумма", Toast.LENGTH_SHORT).show();
            valueEditText.setText("");
            return;
        }


        // Получение введенных данных и сохранение в базу данных
//        String date = dateButton.getText().toString();
//        int value =  Integer.parseInt(valueEditText.getText().toString());
//        String comment = commentEditText.getText().toString();
        Category selectedCategory = (Category) categorySpinner.getAdapter()
                .getItem(categorySpinner.getSelectedItemPosition());

        Operation editedOperation = new Operation();
        editedOperation.setCategoryId(selectedCategory.id);
        editedOperation.setValue(value);
        editedOperation.setDateFromString(date);
        editedOperation.setComment(comment);

        if (activityGoal.equals("EDIT")){
            editedOperation.setId(operationWithDetails.getOperationID());
            executorService.execute(() -> {
                operationDao.update(editedOperation);
            });

        }
        else if (activityGoal.equals("ADD")){
            executorService.execute(() -> {
                operationDao.insert(editedOperation);
            });
        }

        finish();
    }

    private void deleteOperation(View v){
        executorService.execute(() -> {
            operationDao.deleteById(operationWithDetails.getOperationID());
        });
        finish();
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = String.format("%02d.%02d.%d", selectedDay, selectedMonth + 1, selectedYear);
                    dateButton.setText(selectedDate); // Устанавливаем текст кнопки
                }, year, month, day);
        datePickerDialog.show();
    }

    private void fillCategorySpinner(boolean setSelection) {
        new Thread(() -> {
            List<Category> categories = categoryDao.getAllCategories(); // Получение категорий

            runOnUiThread(() -> {
                categoryArrayAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, categories);
                categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categoryArrayAdapter);

                if (setSelection) {
                    categorySpinner.setSelection(getCategoryPosition(operationWithDetails.getCategoryId()));
                }
            });
        }).start();
    }

    private int getCategoryPosition(int categoryId) {
        for (int i = 0; i < categorySpinner.getAdapter().getCount(); i++) {
            Category category = (Category) categorySpinner.getAdapter().getItem(i);
            if (category.getId() == categoryId) {
                return i;
            }
        }
        return 0;
    }

}

