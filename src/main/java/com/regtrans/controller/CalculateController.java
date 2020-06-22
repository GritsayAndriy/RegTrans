package com.regtrans.controller;

import com.regtrans.service.CalculateService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("calculate.fxml")
public class CalculateController {

    private CalculateService calculateService;
    private String dateFrom;
    private String dateTo;

    @FXML
    private TextField fieldA95;

    @FXML
    private Button btnCalculateA95;

    @FXML
    private TextField fieldA92;

    @FXML
    private Button btnCalculateA92;

    @FXML
    private TextField fieldA80;

    @FXML
    private Button btnCalculateA80;

    @FXML
    private TextField fieldDP;

    @FXML
    private Button btnCalculateDP;

    @FXML
    private TextField fieldGAZ;

    @FXML
    private Button btnCalculateGAZ;

    private ProgressIndicatorAlert proIndAlert = null;

    @Autowired
    public void setCalculateService(CalculateService calculateService) {
        this.calculateService = calculateService;
    }

    @FXML
    void calculateA80(ActionEvent event) {
        showProgressing(event,
                ()->calculate("A-80", Double.parseDouble(fieldA80.getText())));
    }

    @FXML
    void calculateA92(ActionEvent event) {
        showProgressing(event,
                ()->calculate("A-92", Double.parseDouble(fieldA92.getText())));
    }

    @FXML
    void calculateA95(ActionEvent event) {
        showProgressing(event,
                ()-> calculate("A-95", Double.parseDouble(fieldA95.getText())));
    }

    @FXML
    void calculateDP(ActionEvent event) {
        showProgressing(event,
                ()-> calculate("ДП", Double.parseDouble(fieldDP.getText())));
    }

    @FXML
    void calculateGAZ(ActionEvent event) {
        showProgressing(event,
                ()-> calculate("ГАЗ", Double.parseDouble(fieldGAZ.getText())));
    }

    @FXML
    public void initialize() {
        fieldA95.setText(String.valueOf(calculateService.calculateMax("A-95", dateFrom, dateTo)));
        fieldA92.setText(String.valueOf(calculateService.calculateMax("A-92", dateFrom, dateTo)));
        fieldA80.setText(String.valueOf(calculateService.calculateMax("A-80", dateFrom, dateTo)));
        fieldDP.setText(String.valueOf(calculateService.calculateMax("ДП", dateFrom, dateTo)));
        fieldGAZ.setText(String.valueOf(calculateService.calculateMax("ГАЗ", dateFrom, dateTo)));
    }

    public void setMonth(String dateFrom, String dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Помилка");
        alert.setHeaderText(message);
        alert.setContentText("Введіть менше значення, або додайте ще робичих днів");
        alert.showAndWait();
    }

    public void calculate(String fuel, double totalFuel) {
        try {
            calculateService.calculateFuel(fuel, totalFuel, dateFrom, dateTo);
            calculateService.calculateResultWork();
            calculateService.saveCalculation();
        } catch (IndexOutOfBoundsException e) {
            Platform.runLater(() -> showAlert(e.getMessage()));
        }
    }

    public void showProgressing(ActionEvent event, Runnable calculation) {
        Node node = (Node) event.getSource();
        proIndAlert = new ProgressIndicatorAlert(node.getScene().getWindow(), "Зачекайте, йде завантаження даних...");
        proIndAlert.start(calculation);
    }
}
