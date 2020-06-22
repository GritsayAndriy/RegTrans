package com.regtrans.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Decimal {
    public static double cleaningExtra(double number){
        return BigDecimal.valueOf(number)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
