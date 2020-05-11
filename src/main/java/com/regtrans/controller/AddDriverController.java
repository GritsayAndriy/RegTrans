package com.regtrans.controller;

import com.regtrans.model.Driver;
import com.regtrans.model.Transport;
import com.regtrans.service.DriverService;
import com.regtrans.service.TransportService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("add_driver.fxml")
public class AddDriverController {

    private TransportService transportService;
    private DriverService driverService;
    private final FxWeaver fxWeaver;

    @FXML
    private ListView<Transport> listTransport;

    @FXML
    private TextField fieldDriverName;

    @FXML
    private Button btnEditTransport;

    @FXML
    private Button btnAddDriver;

    @FXML
    private Button btnCancel;

    ObservableList<Transport> observableList = FXCollections.observableArrayList();
    private ObservableList<Driver> appMainDriverObservableList;

    @Autowired
    public AddDriverController(TransportService transportService, DriverService driverService, FxWeaver fxWeaver) {
        this.transportService = transportService;
        this.driverService = driverService;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    void openEditTransport(ActionEvent event) {
        AddTransportController dialogController = fxWeaver.getBean(AddTransportController.class);
        dialogController.setTransportsObservableList(observableList);
        Parent parent = fxWeaver.loadView(dialogController.getClass());
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    void addDriver(ActionEvent event) {
        Driver driver = new Driver();
        driver.setFullName(fieldDriverName.getText());
        driver.setTransport(listTransport.getFocusModel().getFocusedItem());
        appMainDriverObservableList.add(driverService.saveDriver(driver));
        cancelAddDriver(event);
    }

    @FXML
    void cancelAddDriver(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setAppMainDriverObservableList(ObservableList<Driver> appMainDriverObservableList) {
        this.appMainDriverObservableList = appMainDriverObservableList;
    }

    @FXML
    void initialize() {
        observableList.setAll(transportService.getAllTransports());
        listTransport.setItems(observableList);
        listTransport.setCellFactory(param -> new ListCell<Transport>() {
            @Override
            protected void updateItem(Transport item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getBrand() == null) {
                    setText(null);
                } else {
                    setText(item.getBrand() + item.getModel());
                }
            }
        });
    }
}