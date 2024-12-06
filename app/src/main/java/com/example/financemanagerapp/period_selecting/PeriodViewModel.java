package com.example.financemanagerapp.period_selecting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;

public class PeriodViewModel extends ViewModel {
    private final MutableLiveData<Period> period = new MutableLiveData<>();

    public PeriodViewModel() {
        Period newPeriod = new Period(new Date(), new Date(), PeriodTypes.ARBITRARY);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Устанавливаем начало месяца
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        newPeriod.setMinDate(calendar.getTime());
        calendar.add(Calendar.MONTH, 1); // Переход к следующему месяцу
        calendar.set(Calendar.DAY_OF_MONTH, 0); // Устанавливаем конец месяца
        newPeriod.setMaxDate(calendar.getTime());

        newPeriod.setPeriodType(PeriodTypes.THIS_MONTH);

        period.setValue(newPeriod);
    }

    public LiveData<Period> getPeriod() {
        return period;
    }

    public void setPeriod(Period newPeriod) {
        period.setValue(newPeriod);
    }
}

