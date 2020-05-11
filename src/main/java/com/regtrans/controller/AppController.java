package com.regtrans.controller;

import com.regtrans.controller.timesheet.TimeSheetCell;
import com.regtrans.model.Driver;
import com.regtrans.model.TimeSheet;
import com.regtrans.service.DriverService;
import com.regtrans.service.TimeSheetService;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.*;

@Component
@FxmlView("app.fxml")
public class AppController {

    private final FxWeaver fxWeaver;
    private DriverService driverService;
    private TimeSheetService timeSheetService;

    @FXML
    private DatePicker datePickerTimeSheet;

    @FXML
    private TableView<Driver> tableTimeSheet;

    @FXML
    private TableColumn<Driver, String> columnDrivers;

    @FXML
    private MenuItem btnMenuEditDrivers;

    @FXML
    private Button addDriver;

//    private ObservableList<Driver> driverObservableList = FXCollections.observableArrayList();
    private ObservableList<Driver> driverObservableList = FXCollections.observableArrayList();
    Map<LocalDate,TableColumn<Driver,TimeSheet>> listDaysColumn;

    @Autowired
    public AppController(DriverService driverService, TimeSheetService timeSheetService, FxWeaver fxWeaver) {
        this.driverService = driverService;
        this.timeSheetService = timeSheetService;
        this.fxWeaver = fxWeaver;

    }

    @FXML
    void openAddDriverWindow(ActionEvent event) {
        AddDriverController dialogController = fxWeaver.getBean(AddDriverController.class);
        dialogController.setAppMainDriverObservableList(driverObservableList);
        Parent parent = fxWeaver.loadView(dialogController.getClass());
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        tableTimeSheet.refresh();
    }

    @FXML
    public void initialize() {
        initializeListTimeSheet();
    }


    private void initializeListTimeSheet(){
        List<LocalDate> days = getListDate("2020-04-01", "2020-05-01");
        listDaysColumn = new LinkedHashMap<>();
        for (LocalDate day: days){
            listDaysColumn.put(day, new TableColumn<>(day.toString()));
        }
        tableTimeSheet.getColumns().addAll(listDaysColumn.values());
        tableTimeSheet.setEditable(true);

        columnDrivers.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Driver, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Driver, String> param) {
                Driver drivers = param.getValue();
                SimpleStringProperty driver = new SimpleStringProperty(drivers.getFullName());
                return driver;
            }
        });
        for (Map.Entry<LocalDate,TableColumn<Driver,TimeSheet>> tableColumnDay: listDaysColumn.entrySet()) {
            tableColumnDay.getValue().setCellValueFactory(driverTimeSheetCellDataFeatures -> {
                for (TimeSheet timeSheet: driverTimeSheetCellDataFeatures.getValue().getTimeSheets()){
                    if(tableColumnDay.getKey().toString().equals(timeSheet.getDayDate())) {
                        return new SimpleObjectProperty<TimeSheet>(timeSheet);
//                    }else {
//                        return new SimpleObjectProperty<TimeSheet>(driverTimeSheetCellDataFeatures.getValue());
                    }
                }
                return null;
            });
            tableColumnDay.getValue().setCellFactory(new Callback<TableColumn<Driver, TimeSheet>, TableCell<Driver, TimeSheet>>() {
                @Override
                public TableCell<Driver, TimeSheet> call(TableColumn<Driver, TimeSheet> driverTimeSheetTableColumn) {
                    return new TimeSheetCell<>(tableColumnDay.getKey(), timeSheetService);
//                    return new TimeSheetCellEdit<>(tableColumnDay.getKey(),driverTimeSheetTableColumn);
                }
            });
        }

        driverObservableList.setAll(driverService.getAllDrivers());
        tableTimeSheet.setItems(driverObservableList);
    }


    public List<LocalDate> getListDate(String from, String to){
        LocalDate start = LocalDate.parse(from);
        LocalDate end = LocalDate.parse(to);
        List<LocalDate> totalDates = new ArrayList<>();
        while (!start.isEqual(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }
        return totalDates;
    }

    public void createDataPicker(){
//        datePickerTimeSheet.
    }
}


