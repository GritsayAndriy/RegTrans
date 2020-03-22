package com.regtrans.model;

import com.regtrans.model.interfaces.DriversList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DriversListCollection implements DriversList {
    private ObservableList <Driver> drivers;

    public DriversListCollection() {
        drivers = FXCollections.observableArrayList();
    }

    @Override
    public void addDriver(Driver driver) {
        drivers.add(driver);
    }

    @Override
    public void removeDriver(Driver driver) {
        drivers.removeAll(driver);
    }

    @Override
    public ObservableList<Driver> getDrivers() {
        return drivers;
    }
}
