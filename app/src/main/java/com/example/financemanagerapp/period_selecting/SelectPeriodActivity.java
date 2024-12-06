package com.example.financemanagerapp.period_selecting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.financemanagerapp.R;

import java.util.Calendar;
import java.util.Date;

public class SelectPeriodActivity extends AppCompatActivity {

    private Period period;
    private PeriodViewModel periodViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_period);

        // Инициализация ViewModel
        periodViewModel = new ViewModelProvider(this).get(PeriodViewModel.class);

        // Инициализация Period с начальной датой
        period = new Period(new Date(), new Date(), PeriodTypes.ARBITRARY); // По умолчанию установим текущую дату

        Button weekButton = findViewById(R.id.week_button);
        Button monthButton = findViewById(R.id.month_button);
        Button yearButton = findViewById(R.id.year_button);
        Button allTimeButton = findViewById(R.id.all_time_button);
        Button rangeButton = findViewById(R.id.range_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        weekButton.setOnClickListener(v -> selectWeek());
        monthButton.setOnClickListener(v -> selectMonth());
        yearButton.setOnClickListener(v -> selectYear());
        allTimeButton.setOnClickListener(v -> selectAllTime());
        rangeButton.setOnClickListener(v -> openDatePicker());
        cancelButton.setOnClickListener(v -> cancel());
//        okButton.setOnClickListener(v -> applyChanges());
    }

    private void selectWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        period.setMinDate(calendar.getTime()); // Устанавливаем минимальную дату на начало недели
        calendar.add(Calendar.DAY_OF_WEEK, 6); // Устанавливаем конец недели
        period.setMaxDate(calendar.getTime());

        period.setPeriodType(PeriodTypes.THIS_WEEK);

        applyChanges();
    }

    private void selectMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Устанавливаем начало месяца
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        period.setMinDate(calendar.getTime());
        calendar.add(Calendar.MONTH, 1); // Переход к следующему месяцу
        calendar.set(Calendar.DAY_OF_MONTH, 0); // Устанавливаем конец месяца
        period.setMaxDate(calendar.getTime());

        period.setPeriodType(PeriodTypes.THIS_MONTH);

        applyChanges();
    }

    private void selectYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1); // Устанавливаем начало года
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        period.setMinDate(calendar.getTime());
        calendar.add(Calendar.YEAR, 1); // Переход к следующему году
        calendar.set(Calendar.DAY_OF_YEAR, 0); // Устанавливаем конец года
        period.setMaxDate(calendar.getTime());

        period.setPeriodType(PeriodTypes.THIS_YEAR);

        applyChanges();
    }

    private void selectAllTime() {
        period.setMinDate(period.getZeroDate()); // Установка на начало времени
        period.setMaxDate(period.getEndDate()); // Установка на конец времени

        period.setPeriodType(PeriodTypes.ALL_TIME);

        applyChanges();
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog startDatePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    // Установка минимальной даты
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.set(year, month, dayOfMonth);
                    startCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    startCalendar.set(Calendar.MINUTE, 0);
                    startCalendar.set(Calendar.SECOND, 0);
                    startCalendar.set(Calendar.MILLISECOND, 0);
                    period.setMinDate(startCalendar.getTime());

                    // Открываем второй DatePicker для максимальной даты
                    DatePickerDialog endDatePickerDialog =  new DatePickerDialog(this,
                            (view1, year1, month1, dayOfMonth1) -> {
                                // Установка максимальной даты
                                Calendar endCalendar = Calendar.getInstance();
                                endCalendar.set(year1, month1, dayOfMonth1);
                                endCalendar.set(Calendar.HOUR_OF_DAY, 0);
                                endCalendar.set(Calendar.MINUTE, 0);
                                endCalendar.set(Calendar.SECOND, 0);
                                endCalendar.set(Calendar.MILLISECOND, 0);
                                period.setMaxDate(endCalendar.getTime());

                                period.setPeriodType(PeriodTypes.ARBITRARY);

                                applyChanges();

                            }, year, month, dayOfMonth);

                    endDatePickerDialog.setTitle("Выберите конечную дату");
                    endDatePickerDialog.show();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        startDatePickerDialog.setTitle("Выберите начальную дату");
        startDatePickerDialog.show();
    }

    private void showPeriodToast() {
        Toast.makeText(this, period.getName() + "Минимальная дата: " + period.getMinDate() + "\nМаксимальная дата: " + period.getMaxDate(), Toast.LENGTH_LONG).show();
        Log.i("DB", period.getName() + "Минимальная дата: " + period.getMinDate() + "\nМаксимальная дата: " + period.getMaxDate());
    }

    private void cancel(){
        finish();
    }

    private void applyChanges(){

        Intent resultIntent = new Intent();
        resultIntent.putExtra("selected_period", period); // Передаем объект Period
        setResult(RESULT_OK, resultIntent); // Устанавливаем результат

        finish();
    }
}
