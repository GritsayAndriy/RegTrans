package com.regtrans.controller;

import com.regtrans.controller.validation.Validation;
import com.regtrans.controller.widgets.MaskField;
import com.regtrans.model.Transport;
import com.regtrans.model.TypeFuel;
import com.regtrans.service.TransportService;
import com.regtrans.service.TypeFuelService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@FxmlView("add_transport.fxml")
public class EditTransportController {

    private TransportService transportService;
    private TypeFuelService typeFuelService;

    @FXML
    private TextField fieldBrand;

    @FXML
    private TextField fieldModel;

    @FXML
    private MaskField fieldNumber;

    @FXML
    private ComboBox<String> comboBoxTypeTransport;

    @FXML
    private ComboBox<TypeFuel> comboBoxTypeFlue;

    @FXML
    private TextField fieldStaticUseFlue;

    @FXML
    private Button btnAddTransport;

    @FXML
    private Button btnCancel;

    @FXML
    private ListView<Transport> transportsListView;

    @FXML
    private Button btnDeleteTransport;

    @FXML
    private Button btnUpdateTransport;

    private ObservableList<Transport> transportsObservableList = FXCollections.observableArrayList();
    private ObservableList<TypeFuel> typeFuelObservableList = FXCollections.observableArrayList();
    private ObservableList<String> transportTypes = FXCollections.observableArrayList();

    @Autowired
    public EditTransportController(TransportService transportService, TypeFuelService typeFuelService) {
        this.transportService = transportService;
        this.typeFuelService = typeFuelService;
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
        transport.setTypeFuel(comboBoxTypeFlue.getSelectionModel().getSelectedItem());
        transport.setTypeTransport(comboBoxTypeTransport.getSelectionModel().getSelectedIndex());
        transport.setStaticUseFuel(Double.parseDouble(fieldStaticUseFlue.getText()));
        try {
            transportsObservableList.add(transportService.save(transport));
        } catch (HibernateException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Помилка");
            alert.setHeaderText("Помилка зберігання транспорту");
            alert.setContentText("Можливо вже існує трансопорт з таким державним номерем");
            alert.showAndWait();
        }
    }

    @FXML
    void initialize() {
        transportsObservableList.setAll(transportService.getAllTransports());
        initializeComboBoxTypeFuel();
        initTransportTypes();

        transportsListView.setItems(transportsObservableList);
        transportsListView.setCellFactory(driverListView -> new ListCell<>() {
            @Override
            protected void updateItem(Transport item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getBrand() + "\n     "
                            + item.getModel());
                }
            }
        });


        transportsListView.getSelectionModel().selectedItemProperty().addListener((observableValue, driver, t1) -> {
            if (t1 != null) {
                fieldBrand.setText(t1.getBrand());
                fieldModel.setText(t1.getModel());
                fieldNumber.setText(t1.getNumber());
                comboBoxTypeTransport.getSelectionModel().select(t1.getTypeTransport());
                comboBoxTypeFlue.getSelectionModel().select(t1.getTypeFuel());
                fieldStaticUseFlue.setText(String.valueOf(t1.getStaticUseFuel()));
                btnUpdateTransport.setDisable(false);
                btnDeleteTransport.setDisable(false);
                btnAddTransport.setDisable(true);
            }
        });

        ChangeListener<String> changeListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                validationFields(s,t1);
                if (isEmpty()) {
                    btnAddTransport.setDisable(true);
                } else
                    btnAddTransport.setDisable(false);
            }
        };
        fieldStaticUseFlue.textProperty().addListener(changeListener);
        fieldBrand.textProperty().addListener(changeListener);
        fieldModel.textProperty().addListener(changeListener);
        fieldNumber.textProperty().addListener(changeListener);

        comboBoxTypeTransport.setOnAction(actionEvent -> {
            if (isEmpty()) {
                btnAddTransport.setDisable(true);
            } else
                btnAddTransport.setDisable(false);
        });
        comboBoxTypeFlue.setOnAction(actionEvent -> {
            if (isEmpty()) {
                btnAddTransport.setDisable(true);
            } else
                btnAddTransport.setDisable(false);
        });
    }

    @FXML
    void updateTransport(ActionEvent event) {
        Transport transport = transportsListView.getFocusModel().getFocusedItem();
        int index = transportsObservableList.indexOf(transport);
        transport.setBrand(fieldBrand.getText());
        transport.setModel(fieldModel.getText());
        transport.setNumber(fieldNumber.getText());
        transport.setTypeFuel(comboBoxTypeFlue.getSelectionModel().getSelectedItem());
        transport.setTypeTransport(comboBoxTypeTransport.getSelectionModel().getSelectedIndex());
        transport.setStaticUseFuel(Double.parseDouble(fieldStaticUseFlue.getText()));
        transportsObservableList.set(index, transportService.updateTransport(transport));
        resetForm();
    }


    @FXML
    void deleteTransport(ActionEvent event) {
        Transport transport = transportsListView.getFocusModel().getFocusedItem();
        transportsObservableList.remove(transportService.deleteTransport(transport));
        resetForm();
    }

    public boolean isEmpty() {
        boolean empty;
        if (fieldBrand.getText().isEmpty()
                || fieldModel.getText().isEmpty()
                || fieldNumber.getText().isEmpty()
                || fieldStaticUseFlue.getText().isEmpty()
                || comboBoxTypeTransport.getValue().isEmpty()
                || comboBoxTypeFlue.getValue() == null) {
            empty = true;
        } else {
            empty = false;
        }
        return empty;
    }


    private void initializeComboBoxTypeFuel() {
        typeFuelObservableList.setAll(typeFuelService.getAllTypeFuel());
        comboBoxTypeFlue.setItems(typeFuelObservableList);
        comboBoxTypeFlue.setCellFactory(typeFuelListView -> new ListCell<>() {
            @Override
            protected void updateItem(TypeFuel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        comboBoxTypeFlue.getSelectionModel().select(0);

    }

    private void initTransportTypes(){
        transportTypes.setAll("Грузоперевозка", "Спецтехніка");
        comboBoxTypeTransport.setItems(transportTypes);
        comboBoxTypeTransport.getSelectionModel().select(0);
    }

    private void validationFields(String oldValue, String newValue) {
        if (fieldBrand.getText().equals(newValue)) {
            String text = fieldBrand.getText();
            if (Validation.isLetters(newValue) && Validation.validateLength(text)) {
                text = text.toUpperCase();
                fieldBrand.setText(text);
            } else {
                fieldBrand.setText(oldValue);
            }
        } else if (fieldModel.getText().equals(newValue)) {
            String text = fieldModel.getText();
            if (Validation.isModel(text) && Validation.validateLength(text)) {
                text = text.toUpperCase();
                text = Validation.oneSpace(text);
                fieldModel.setText(text);
            } else {
                fieldModel.setText(oldValue);
            }
        } else if (fieldNumber.getText().equals(newValue)) {
            String text = fieldNumber.getText();
            text = text.toUpperCase();
            fieldNumber.setText(text);

        } else if (fieldStaticUseFlue.getText().equals(newValue)) {
            String text = fieldStaticUseFlue.getText();
            if (Validation.isNumerical(newValue)) {
                fieldStaticUseFlue.setText(text);
            } else {
                fieldStaticUseFlue.setText(oldValue);
            }
        }
    }

    public void resetForm(){
        transportsListView.refresh();
        transportsListView.getSelectionModel().clearSelection();
        fieldBrand.clear();
        fieldModel.clear();
        fieldNumber.setPlaceholder("XX0000YY");
        comboBoxTypeTransport.getSelectionModel().select(0);
        comboBoxTypeFlue.getSelectionModel().select(0);
        fieldStaticUseFlue.clear();
        btnUpdateTransport.setDisable(true);
        btnDeleteTransport.setDisable(true);
        btnAddTransport.setDisable(true);
    }


}
