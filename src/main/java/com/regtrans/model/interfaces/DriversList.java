package com.regtrans.model.interfaces;

import com.regtrans.model.Driver;
import javafx.collections.ObservableList;

public interface DriversList {
    public void addDriver (Driver driver);
    public void removeDriver (Driver driver);
    public ObservableList<Driver> getDrivers();
}
