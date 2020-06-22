package com.regtrans.controller;

import com.regtrans.model.Driver;
import com.regtrans.model.Transport;
import com.regtrans.service.DriverService;
import com.regtrans.service.TransportService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sqlite.SQLiteException;

@Component
@FxmlView("add_driver.fxml")
public class EditDriverController {

    private TransportService transportService;
    private DriverService driverService;
    private final FxWeaver fxWeaver;

    @FXML
    private ComboBox<Transport> transportComboBox;

    @FXML
    private ListView<Driver> listDrivers;

    @FXML
    private TextField fieldFatherName;

    @FXML
    private TextField fieldLastName;

    @FXML
    private TextField fieldFirstName;

    @FXML
    private Button btnEditTransport;

    @FXML
    private Button btnAddDriver;

    @FXML
    private Button btnDeleteDriver;

    @FXML
    private Button btnUpdateDriver;

    @FXML
    private Button btnCancel;

    private ObservableList<Transport> transportsObservableList = FXCollections.observableArrayList();
    private ObservableList<Driver> driversObservableList = FXCollections.observableArrayList();

    private boolean fieldsIsEmpty = true;


    @Autowired
    public EditDriverController(TransportService transportService, DriverService driverService, FxWeaver fxWeaver) {
        this.transportService = transportService;
        this.driverService = driverService;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    void openEditTransport(ActionEvent event) {
        EditTransportController dialogController = fxWeaver.getBean(EditTransportController.class);
        Parent parent = fxWeaver.loadView(dialogController.getClass());
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    void addDriver(ActionEvent event) throws SQLiteException {
        Driver driver = new Driver();
        driver.setLastName(fieldLastName.getText());
        driver.setFirstName(fieldFirstName.getText());
        driver.setFatherName(fieldFatherName.getText());
        driver.setTransport(transportComboBox.getSelectionModel().getSelectedItem());
        driversObservableList.add(driverService.saveDriver(driver));
        clearField();
        transportComboBox.getSelectionModel().clearSelection();

    }

    @FXML
    void cancelAddDriver(ActionEvent event) {
//        transportsObservableList.clear();
//        driversObservableList.clear();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        transportsObservableList.setAll(transportService.getAllTransports());
        transportComboBox.setItems(transportsObservableList);
        transportComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            public void updateItem(Transport item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getBrand() == null) {
                    setText(null);
                } else {
                    setText(item.getBrand() + "  " + item.getModel());
                }
            }
        });

        driversObservableList.setAll(driverService.getAllDrivers());
        listDrivers.setItems(driversObservableList);
        listDrivers.setCellFactory(driverListView -> new ListCell<>() {
            @Override
            protected void updateItem(Driver item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getLastName() == null) {
                    setText(null);
                } else {
                    setText(item.getLastName()+" "+item.getFirstName()+" "+ item.getFatherName() + "\n     "
                            + item.getTransport().getBrand() + " "
                            + item.getTransport().getModel());
                }
            }
        });

        listDrivers.getSelectionModel().selectedItemProperty().addListener((observableValue, driver, t1) -> {
            if (t1 != null) {
                fieldLastName.setText(t1.getLastName());
                fieldFirstName.setText(t1.getFirstName());
                fieldFatherName.setText(t1.getFatherName());
                transportComboBox.setValue(t1.getTransport());
                btnUpdateDriver.setDisable(false);
                btnDeleteDriver.setDisable(false);
                btnAddDriver.setDisable(true);
            }
        });

        fieldLastName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.isEmpty() && checkFields()) {
                    btnAddDriver.setDisable(false);
                } else
                    btnAddDriver.setDisable(true);
            }
        });

        fieldFirstName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.isEmpty() && checkFields()) {
                    btnAddDriver.setDisable(false);
                } else
                    btnAddDriver.setDisable(true);
            }
        });

        fieldFatherName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.isEmpty() && checkFields()) {
                    btnAddDriver.setDisable(false);
                } else
                    btnAddDriver.setDisable(true);
            }
        });

        transportComboBox.setOnAction(actionEvent -> {
            if (transportComboBox.getValue() != null && checkFields()) {
                btnAddDriver.setDisable(false);
            } else
                btnAddDriver.setDisable(true);
        });
    }

    @FXML
    void deleteDriver(ActionEvent event) {
        Driver driver = listDrivers.getFocusModel().getFocusedItem();
        driversObservableList.remove(driverService.deleteDriver(driver));
        listDrivers.refresh();
        listDrivers.getSelectionModel().clearSelection();
        clearField();
        transportComboBox.getSelectionModel().clearSelection();
        btnUpdateDriver.setDisable(true);
        btnDeleteDriver.setDisable(true);
        btnAddDriver.setDisable(false);
    }

    @FXML
    void updateDriver(ActionEvent event) {
        Driver driver = listDrivers.getFocusModel().getFocusedItem();
        int index = driversObservableList.indexOf(driver);
        driver.setLastName(fieldLastName.getText());
        driver.setFirstName(fieldFirstName.getText());
        driver.setFatherName(fieldFatherName.getText());
        driver.setTransport(transportComboBox.getSelectionModel().getSelectedItem());
        driversObservableList.set(index, driverService.updateDriver(driver));
        listDrivers.refresh();
        listDrivers.getSelectionModel().clearSelection();
        clearField();
        transportComboBox.getSelectionModel().clearSelection();
        btnUpdateDriver.setDisable(true);
        btnDeleteDriver.setDisable(true);
        btnAddDriver.setDisable(false);
    }

    @FXML
    void clearSelection(MouseEvent event) {
        listDrivers.getSelectionModel().clearSelection();
        btnUpdateDriver.setDisable(true);
        btnDeleteDriver.setDisable(true);
    }

    private void clearField() {
        fieldLastName.clear();
        fieldFirstName.clear();
        fieldFatherName.clear();
    }

    private boolean checkFields() {
        if (fieldLastName.getText().isEmpty()
                || fieldFirstName.getText().isEmpty()
                || fieldFatherName.getText().isEmpty()
                || transportComboBox.getSelectionModel().isEmpty()) {
            fieldsIsEmpty = true;
            return false;
        } else {
            fieldsIsEmpty = false;
            return true;
        }
    }
}