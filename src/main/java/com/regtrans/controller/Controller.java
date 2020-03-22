package com.regtrans.controller;

import com.regtrans.model.Driver;
import com.regtrans.model.DriversListCollection;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {
    @FXML
    TableView table;

    @FXML
    TableColumn column;


    DriversListCollection collection = new DriversListCollection();


    public void initialize() {
        collection.addDriver(new Driver("Vasya"));
        collection.addDriver(new Driver("Petya"));

        table.setItems(collection.getDrivers());
        column.setCellValueFactory(new PropertyValueFactory<>("name"));

    }
}
