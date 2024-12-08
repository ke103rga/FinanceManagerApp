package com.example.financemanagerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.financemanagerapp.database.AppDatabase;
import com.example.financemanagerapp.database.DatabaseInitializer;
import com.example.financemanagerapp.operations.OperationsFragment;
import com.example.financemanagerapp.period_selecting.Period;
import com.example.financemanagerapp.period_selecting.PeriodViewModel;
import com.example.financemanagerapp.period_selecting.SelectPeriodActivity;
import com.example.financemanagerapp.review.ReviewFragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String FIRST_RUN_KEY = "isFirstRun";

    // Создание пула потоков
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static final int REQUEST_CODE_SELECT_PERIOD = 1; // Константа для кода запроса

    private PeriodViewModel periodViewModel;
    Button selectPeriodButton;
    Button overviewButton;
    Button operationsButton;
    ImageView reviewImageView;
    ImageView operationsImageView;

    // Переменная, которая будет указывать на то, какой раздел сейчас открыт
    private static int currentSectionButtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // При первом запуске МП БД наполняется информацией о стандартных категориях
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean(FIRST_RUN_KEY, true);
        if (isFirstRun) {
            // Код, который должен выполняться только при первом запуске
            executorService.execute(() -> {
                Context context = getApplicationContext();
                context.deleteDatabase("app_database");
                Log.i("DB", "First time filling");

                //            DatabaseInitializer.clearDatabase(MainActivity.this);
                DatabaseInitializer.populateDatabase(MainActivity.this, true);
            });

            // Обновление значения в SharedPreferences
            prefs.edit().putBoolean(FIRST_RUN_KEY, false).apply();
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_view, OperationsFragment.class, null)
                    .commit();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        overviewButton = findViewById(R.id.overview_button);
        operationsButton = findViewById(R.id.operations_button);
        reviewImageView = findViewById(R.id.review_image_view);
        operationsImageView = findViewById(R.id.operations_image_view);

        overviewButton.setOnClickListener(v -> switchFragment(new ReviewFragment(), overviewButton));
        operationsButton.setOnClickListener(v -> switchFragment(new OperationsFragment(), operationsButton));
        // Нажатие на иконку раздела приравнивается к нажатию на название раздела
        reviewImageView.setOnClickListener(v -> switchFragment(new ReviewFragment(), overviewButton));
        operationsImageView.setOnClickListener(v -> switchFragment(new OperationsFragment(), operationsButton));

        // Запустите фрагмент "Обзор" по умолчанию
        switchFragment(new ReviewFragment(), overviewButton);
        currentSectionButtonId = overviewButton.getId();

        // Инициализация ViewModel
        periodViewModel = new ViewModelProvider(this).get(PeriodViewModel.class);

        selectPeriodButton = findViewById(R.id.select_period_button);
        selectPeriodButton.setText(periodViewModel.getPeriod().getValue().getName());
        selectPeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectPeriodActivity.class);
                // Запускаем новую активность для получения результата
                startActivityForResult(intent, REQUEST_CODE_SELECT_PERIOD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_PERIOD && resultCode == RESULT_OK) {
            if (data != null) {
                Period selectedPeriod = (Period) data.getSerializableExtra("selected_period"); // Получение объекта Period
                periodViewModel.setPeriod(selectedPeriod);
                // Обновление текста кнопки
                selectPeriodButton.setText(periodViewModel.getPeriod().getValue().getName());
            }
        }
    }

    private void switchFragment(Fragment fragment, Button selectedButton) {
        //Ничего не происходит если надали на уже активную кнопку
        if (selectedButton.getId() == currentSectionButtonId){ return; }

        // Заменяем текущий фрагмент
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment);
        transaction.commit();
        // Обновляем информацию об активной кнопке
        currentSectionButtonId = selectedButton.getId();

        // Установка стиля кнопок
        updateButtonTextStyle(selectedButton);
    }

    private void updateButtonTextStyle(Button selectedButton) {
        overviewButton.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault);
        operationsButton.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault);

        // Задаем жирный стиль для нажатой кнопки
        selectedButton.setTextAppearance(this, android.R.style.TextAppearance_Medium);

        // Изменяем жирный шрифт для нажатой кнопки
        selectedButton.setTypeface(null, android.graphics.Typeface.BOLD);
    }

}