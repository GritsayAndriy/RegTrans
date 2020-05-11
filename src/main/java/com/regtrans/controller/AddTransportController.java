package com.regtrans.controller;

import com.regtrans.model.Driver;
import com.regtrans.model.Transport;
import com.regtrans.service.TransportService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("add_transport.fxml")
public class AddTransportController {

    private TransportService transportService;

    @FXML
    private TextField fieldBrand;

    @FXML
    private TextField fieldModel;

    @FXML
    private TextField fieldNumber;

    @FXML
    private ChoiceBox<?> comboBoxTypeTransport;

    @FXML
    private ComboBox<?> comboBoxTypeFlue;

    @FXML
    private TextField fieldStaticUseFlue;

    @FXML
    private Button btnAddTransport;

    @FXML
    private Button btnCancel;

    private ObservableList<Transport> transportsObservableList;

    @Autowired
    public AddTransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    public void setTransportsObservableList(ObservableList<Transport> transportsObservableList) {
        this.transportsObservableList = transportsObservableList;
    }

    @FXML
    void cancelAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveTransport(ActionEvent event) {
        Transport transport = new Transport();
        transport.setBrand(fieldBrand.getText());
        transport.setModel(fieldModel.getText());
        transport.setNumber(fieldNumber.getText());
        transport.setTypeFlue(comboBoxTypeFlue.getSelectionModel().getSelectedIndex());
        transport.setTypeTransport(comboBoxTypeTransport.getSelectionModel().getSelectedIndex());
        transport.setStaticUseFuel(Integer.parseInt(fieldStaticUseFlue.getText()));
        transportsObservableList.add(transportService.save(transport));
        cancelAction(event);
    }

    @FXML
    void initialize() {
    }
}
