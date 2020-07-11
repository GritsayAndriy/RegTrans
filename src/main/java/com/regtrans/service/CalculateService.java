package com.regtrans.service;

import com.regtrans.dao.TimeSheetDao;
import com.regtrans.model.TimeSheet;
import com.regtrans.model.TypeFuel;
import com.regtrans.model.TypeTransportsRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("calculateService")
public class CalculateService {

    private TimeSheetDao timeSheetDao;
    private TypeFuelService typeFuelService;
    private List<TypeFuel> typeFuel;
    private List<TimeSheet> timeSheets;

    @Autowired
    public CalculateService(TimeSheetDao timeSheetDao, TypeFuelService typeFuelService) {
        this.timeSheetDao = timeSheetDao;
        this.typeFuelService = typeFuelService;
        this.typeFuel = typeFuelService.getAllTypeFuel();
    }

    public void calculateFuel(String fuel, double totalFuel, String from, String to) {

        TypeFuel typeFuel = (TypeFuel) this.typeFuel
                .stream()
                .filter(typeFuel1 -> typeFuel1.getName().equals(fuel))
                .findAny()
                .get();


        this.timeSheets = timeSheetDao
                .getTimeSheetSomeMonth(from, to, typeFuel);
        clearUseFuel();
        double maxTotalFuel = calculateMax(this.timeSheets,fuel,from,to);
        if (totalFuel>maxTotalFuel){
            throw new IndexOutOfBoundsException("Значення більше максимального");
        }

        double randomForUseFuel;
        double maxFuelConsumptionRateForDay = 0;

        do {
            for (TimeSheet timeSheet : this.timeSheets) {
                if (totalFuel <= 0)
                    return;

                double fuelConsumptionRate = timeSheet
                        .getTransport()
                        .getStaticUseFuel();

                if (timeSheet.getTransport().getTypeTransport() == 0) {
                    maxFuelConsumptionRateForDay = TypeTransportsRate.TRUCK
                            .maxFuelConsumptionRateForDay(fuelConsumptionRate);
                } else
                    maxFuelConsumptionRateForDay = TypeTransportsRate.SPECIAL.
                            maxFuelConsumptionRateForDay(fuelConsumptionRate);

                double residue = Decimal.cleaningExtra(maxFuelConsumptionRateForDay - timeSheet.getUseFuel());

                if (residue != 0) {
                    if (residue <= totalFuel) {
                        randomForUseFuel = randomMaxMin(residue, 0.01);
                    } else
                        randomForUseFuel = randomMaxMin(totalFuel, 0.01);

                    totalFuel -= randomForUseFuel;

                    timeSheet.setUseFuel(timeSheet.getUseFuel() + randomForUseFuel);
                    totalFuel = Decimal.cleaningExtra(totalFuel);
                }
            }
        } while (totalFuel > 0);
    }

    public void calculateResultWork() {
        for (TimeSheet timeSheet : this.timeSheets) {
            double fuelConsumptionRate = timeSheet
                    .getTransport()
                    .getStaticUseFuel();
            double useFuelForDay = timeSheet.
                    getUseFuel();
            if (timeSheet.getTransport().getTypeTransport() == 0) {
                timeSheet.setResultWork(TypeTransportsRate.TRUCK
                        .calculateResultWork(fuelConsumptionRate, useFuelForDay));
            } else
                timeSheet.setResultWork(TypeTransportsRate.SPECIAL
                        .calculateResultWork(fuelConsumptionRate, useFuelForDay));
        }

    }

    public void saveCalculation() {
        for (TimeSheet timeSheet : this.timeSheets) {
            timeSheetDao.update(timeSheet);
        }
    }

    public void clearUseFuel() {
        for (TimeSheet timeSheet : this.timeSheets) {
            timeSheet.setResultWork(0);
            timeSheet.setUseFuel(0);
        }
    }

    public double calculateMax(List<TimeSheet> timeSheets, String fuel, String from, String to) {
        double maxFuelConsumptionRate = 0;

        TypeFuel typeFuel = (TypeFuel) this.typeFuel
                .stream()
                .filter(typeFuel1 -> typeFuel1.getName().equals(fuel))
                .findAny()
                .get();

        if (timeSheets == null) {
            this.timeSheets = timeSheetDao
                    .getTimeSheetSomeMonth(from, to, typeFuel);
        } else
            this.timeSheets = timeSheets;

        for (TimeSheet timeSheet : this.timeSheets) {
            double fuelConsumptionRate = timeSheet
                    .getTransport()
                    .getStaticUseFuel();
            if (timeSheet.getTransport().getTypeTransport() == 0) {
                maxFuelConsumptionRate += TypeTransportsRate.TRUCK
                        .maxFuelConsumptionRateForDay(fuelConsumptionRate);
            } else
                maxFuelConsumptionRate += TypeTransportsRate.SPECIAL.
                        maxFuelConsumptionRateForDay(fuelConsumptionRate);
        }
        return Decimal.cleaningExtra(maxFuelConsumptionRate);
    }

    public double calculateMax(String fuel, String from, String to) {
        return calculateMax(null, fuel, from, to);
    }

    public double randomMaxMin(double max, double min) {
        return Decimal.cleaningExtra((Math.random() * (max - min)) + min);
    }
}
