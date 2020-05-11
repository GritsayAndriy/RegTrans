package com.regtrans.model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "time_sheet")
public class TimeSheet {

    private int id;
    private String dayDate;
    private int useFuel;
    private int resultWork;

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
    public int getUseFuel() {
        return useFuel;
    }

    public void setUseFuel(int useFuel) {
        this.useFuel = useFuel;
    }

    @Column(name = "result_work")
    public int getResultWork() {
        return resultWork;
    }

    public void setResultWork(int resultWork) {
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
