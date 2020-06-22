package com.regtrans.model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "time_sheet")
@NamedQueries({
        @NamedQuery(name = "TimeSheet.getTimeSheetSomeMonth",
        query = "select distinct ts from TimeSheet ts "
                + "left join fetch ts.transport t "
                + "where ts.dayDate>=:fromDay "
                + "and ts.dayDate< :toDay "
                + "and t.typeFuel = :type "),
        @NamedQuery(name = "TimeSheet.getTimeSheetSomeMonthAndDriver",
        query = "select distinct ts from TimeSheet ts "
                + "where ts.dayDate>=:fromDay "
                + "and ts.dayDate< :toDay "
                + "and ts.driver = :driver")
})
public class TimeSheet {

    private int id;
    private String dayDate;
    private double useFuel;
    private double resultWork;

    private Driver driver;
    private Transport transport;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "day_date")
    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    @Column(name = "use_fuel")
    public double getUseFuel() {
        return useFuel;
    }

    public void setUseFuel(double useFuel) {
        this.useFuel = useFuel;
    }

    @Column(name = "result_work")
    public double getResultWork() {
        return resultWork;
    }

    public void setResultWork(double resultWork) {
        this.resultWork = resultWork;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver")
    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport")
    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return "TimeSheet{" +
                "id=" + id +
                ", dayDate=" + dayDate +
                ", useFuel=" + useFuel +
                ", resultWork=" + resultWork +
                ", driver=" + driver +
                ", transport=" + transport +
                '}';
    }
}
