package com.regtrans.controller.report;

import com.regtrans.model.Driver;
import com.regtrans.model.TypeTransportsRate;
import com.regtrans.service.ReportService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@FxmlView("create_report.fxml")
public class CreateReportController {

    @FXML
    private Label labelRoute;

    @FXML
    private TextField fieldRoute;

    @FXML
    private Label labelCustomer;

    @FXML
    private TextField fieldCustomer;

    @FXML
    private Label labelAddress;

    @FXML
    private TextField fieldAddress;

    @FXML
    private Label labelWhatCreation;

    @FXML
    private Label labelFullName;

    @FXML
    private Label labelCount;


    private Driver driver;
    private TypeTransportsRate typeTransport;
    private ReportService reportService;
    private String currentCount;

    @Autowired
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setLabelCount(int current, int amount){
        currentCount = (current +"/"+ amount);
    }

    @FXML
    public void initialize() {
        if (driver.getTransport().getTypeTransport()==0){
            typeTransport = TypeTransportsRate.TRUCK;
            labelWhatCreation.setText("Створення подорожніх листів");
        }else {
            typeTransport = TypeTransportsRate.SPECIAL;
            labelWhatCreation.setText("Створення рапорту");
        }
        labelFullName.setText("Водій: "+driver.getLastName()+" "+driver.getFirstName());
        labelCount.setText(currentCount);
        validation();
    }

    @FXML
    void createReport(ActionEvent event) {
        try {
            reportService.createFile();
                if (!driver.getTimeSheets().isEmpty()) {
                    String fullName = driver.getLastName()+" "+ driver.getFirstName();
                    reportService.setRoute(fieldRoute.getText());
                    reportService.setСustomer(fieldCustomer.getText());
                    reportService.setAddress(fieldAddress.getText());
                    reportService.writeReport(driver, fullName, typeTransport);
                }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.showAndWait();
        }
        closeStage(event);
    }

    private void validation(){
        if (typeTransport == TypeTransportsRate.TRUCK){
            labelAddress.setDisable(true);
            labelCustomer.setDisable(true);
            fieldCustomer.setDisable(true);
            fieldAddress.setDisable(true);
        }else {
            fieldRoute.setDisable(true);
            labelRoute.setDisable(true);
        }
    }

    @FXML
    public void closeStage(ActionEvent event){
        Node node = (Node)event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
