package com.regtrans.controller;

import com.regtrans.model.Driver;
import com.regtrans.model.DriversListCollection;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class Controller {
    @FXML
    TableView table;

    @FXML
    TableColumn column;

    @FXML
    TextField textField;


    DriversListCollection collection = new DriversListCollection();


    public void initialize() {
        table.setItems(collection.getDrivers());
        column.setCellValueFactory(new PropertyValueFactory<>("name"));

    }

    public void addDriver(MouseEvent mouseEvent) {
        if (textField.getText().length() > 1) {
            collection.addDriver(new Driver(textField.getText()));
            textField.setText("");
        } else {
            System.out.println("Enter driver name");
        }
    }

    public void removeDriver(MouseEvent mouseEvent) {
        collection.removeDriver((Driver)table.getSelectionModel().getSelectedItem());
    }
}
