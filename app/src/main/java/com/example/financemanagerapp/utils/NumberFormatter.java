package com.example.financemanagerapp.utils;

import android.health.connect.datatypes.units.Length;

import java.text.DecimalFormat;

public class NumberFormatter {

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    // Метод для форматирования числа в строку с разделением разрядов тысяч
    public static String formatKPI(int number) {
        // Используем DecimalFormat для формата
        DecimalFormat formatter = new DecimalFormat("#,##0");
        // Заменяем запятую на пробел
        return formatter.format(number).replace(",", " ");
    }

    public static String roundNumber(int number) {
        String strNumber = String.valueOf(number);
        int lenNumber = strNumber.length();

        if(lenNumber <= 4){
            return strNumber;
        }

        else if( lenNumber < 7){
            return String.valueOf(round(number * 1.0 / 1000, 1)) + " K";
        }

        else {
            return String.valueOf(round(number * 1.0 / 1000000, 1)) + " M";
        }
    }
}
