package com.regtrans.model;

import com.regtrans.service.Decimal;

public enum  TypeTransportsRate {
    FREIGHT, SPECIAL;

    private final long MAX_DISTANCE_FOR_DAY = 640;
    private final long MAX_WORK_TIME_MINUTES_FOR_DAY = 480;

    public double maxFuelConsumptionRateForDay(double fuelConsumptionRate){
        double value = 0;
        if (this==TypeTransportsRate.FREIGHT){
            value = fuelConsumptionRate/100*MAX_DISTANCE_FOR_DAY;
        }else
            value = fuelConsumptionRate/60*MAX_WORK_TIME_MINUTES_FOR_DAY;
        return Decimal.cleaningExtra(value);
    }

    public double calculateResultWork(double fuelConsumptionRate, double useFuel){
        double value = 0;
        if (this==TypeTransportsRate.FREIGHT){
            value = useFuel/fuelConsumptionRate*100;
        }else if (this==TypeTransportsRate.SPECIAL){
            value = (useFuel/fuelConsumptionRate);
        }
        return Decimal.cleaningExtra(value);
    }
}
