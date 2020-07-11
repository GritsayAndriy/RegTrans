package com.regtrans.model.date;

import java.util.Arrays;
import java.util.List;

public enum Month {
    UA;
    final List<String> MONTHS_UA = Arrays.asList(
            "Січень", "Лютий", "Березень", "Квітень",
            "Травень", "Червень", "Липень", "Серпень",
            "Вересень", "Жовтень", "Листопад", "Грудень");
    List<String> month;

    public String getMonth(int numberMonth){
        if (this == Month.UA){
            this.month = MONTHS_UA;
        }
        try{
            String month = this.month.get(numberMonth);
            return month;
        }catch (IndexOutOfBoundsException e){
            return "";
        }
    }
}
