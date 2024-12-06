package com.example.financemanagerapp.period_selecting;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Period  implements Serializable {
    private Date minDate;
    private Date maxDate;
    private Date zeroDate;
    private Date endDate;
    private String name;
    private PeriodTypes periodType;

    private Map<PeriodTypes, String> defaultPeriodsNames = Map.of(
            PeriodTypes.THIS_WEEK, "Эта неделя",
            PeriodTypes.THIS_MONTH, "Этот месяц",
            PeriodTypes.THIS_YEAR, "Этот год",
            PeriodTypes.ALL_TIME, "Все время"
    );

    public Period(Date minDate, Date maxDate, PeriodTypes periodType){
        this.maxDate = maxDate;
        this.minDate = minDate;

        createName(minDate, maxDate, periodType);

        setZeroAndEndTime();
    }

    public void createName(Date minDate, Date maxDate, PeriodTypes periodType){
        if (defaultPeriodsNames.containsKey(periodType)) {
            this.name = defaultPeriodsNames.get(periodType);
        }
        else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String startDate = dateFormat.format(minDate);
            String endDate = dateFormat.format(maxDate);

            this.name =  String.format("С %s по %s", startDate, endDate);
        }
    }

    private void setZeroAndEndTime(){
        Calendar calendar = Calendar.getInstance();

        // Устанавливаем начало времени - 1 января 1970 года, 00:00:00
        calendar.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.zeroDate = calendar.getTime();

        // Устанавливаем конец времени - 31 декабря 3000 года, 23:59:59
        calendar.set(3000, Calendar.DECEMBER, 31, 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        this.endDate = calendar.getTime();
    }

    public Date getMinDate(){
        return minDate;
    }

    public Date getMaxDate(){
        return maxDate;
    }

    public Date getEndDate(){
        return endDate;
    }

    public Date getZeroDate(){
        return zeroDate;
    }

    public String getName() {return name; }

    public PeriodTypes getPeriodType() {return periodType; }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public void setPeriodType(PeriodTypes periodType) {
        this.periodType = periodType;
        createName(this.minDate, this.maxDate, periodType);
    }
}
