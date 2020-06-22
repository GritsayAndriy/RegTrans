package com.regtrans.controller;

import com.regtrans.controller.timesheet.TimeSheetCell;
import com.regtrans.model.Driver;
import com.regtrans.model.TimeSheet;
import com.regtrans.model.TypeTransportsRate;
import com.regtrans.service.DriverService;
import com.regtrans.service.ReportService;
import com.regtrans.service.TimeSheetService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@FxmlView("app.fxml")
public class AppController {

    private final FxWeaver fxWeaver;
    private DriverService driverService;
    private TimeSheetService timeSheetService;
    private ReportService reportService;

    @FXML
    private Spinner<Integer> spinnerYear;

    @FXML
    private Spinner<String> monthSpinner;

    @FXML
    private TableView<Driver> tableViewDrivers;

    @FXML
    private TableView<Driver> tableTimeSheet;

    @FXML
    private TableView<Driver> tableResult;

    @FXML
    private TableColumn<Driver, Driver> columnDrivers;

    @FXML
    private TableColumn<Driver, Driver> columnResult;

    @FXML
    private MenuItem btnMenuEditDrivers;

    @FXML
    private MenuItem btnMenuEditTransport;

    @FXML
    private Button btnCalculate;

    @FXML
    private Button btnCreateReport;

    @FXML
    private Button btnUpdateTableCalendar;

    @FXML
    private ScrollPane scrollTimeSheet;

    @FXML
    private ScrollPane scrollTableDrivers;

    @FXML
    private ScrollPane scrollResult;

    @FXML
    private AnchorPane drivePane;

    @FXML
    private AnchorPane timeSheetPane;

    @FXML
    private AnchorPane resultPane;

    @FXML
    private Label labelA95;

    @FXML
    private Label labelA92;

    @FXML
    private Label labelA80;

    @FXML
    private Label labelDP;

    @FXML
    private Label labelGAZ;

    private static final int PREF_HEIGHT_TABLE = 620;

    private ObservableList<Driver> driverObservableList = FXCollections.observableArrayList();
    private List<Driver> driverList = new LinkedList<>();

    Map<LocalDate, TableColumn<Driver, TimeSheet>> listDaysColumn;
    ObservableList<String> months = FXCollections.observableArrayList(
            "Січень", "Лютий", "Березень", "Квітень",
            "Травень", "Червень", "Липень", "Серпень",
            "Вересень", "Жовтень", "Листопад", "Грудень");


    @Autowired
    public AppController(DriverService driverService, TimeSheetService timeSheetService,
                         ReportService reportService, FxWeaver fxWeaver) {
        this.driverService = driverService;
        this.timeSheetService = timeSheetService;
        this.fxWeaver = fxWeaver;
        this.reportService = reportService;
        this.reportService.setFormat("doc");
    }

    @FXML
    void openAddDriverWindow(ActionEvent event) {
        EditDriverController dialogController = fxWeaver.getBean(EditDriverController.class);
        Parent parent = fxWeaver.loadView(dialogController.getClass());
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        updateTableCalendar(event);
    }

    @FXML
    void updateTableCalendar(ActionEvent event) {
        driverList.clear();
        driverObservableList.clear();
        tableTimeSheet.getColumns().clear();
        tableViewDrivers.getColumns().clear();
        tableResult.getColumns().clear();

        driverList.addAll(driverService
                .getAllDrivers(dateFrom(), dateTo()));
        driverObservableList.setAll(driverList);

        initializeTableDrivers();
        initializeTableTimeSheet();
        initializeResultTable();
        insertPrefHeightTables();
        insertPrefWidthTables();
        showUsedFuelPreMonth();
    }

    @FXML
    public void initialize() {
        initializeYearMonth();
        driverList.addAll(driverService
                .getAllDrivers(dateFrom(), dateTo()));
        driverObservableList.setAll(driverList);
        initializeTableDrivers();
        initializeTableTimeSheet();
        initializeResultTable();
        initializeScrollPane();
        insertPrefHeightTables();
        insertPrefWidthTables();
        showUsedFuelPreMonth();
    }

    private void insertPrefWidthTables() {
        double width = 10 + listDaysColumn.values()
                .stream()
                .mapToDouble(TableColumnBase::getMaxWidth)
                .sum();
        tableTimeSheet.setPrefWidth(width);
    }

    private void insertPrefHeightTables() {
        double height = tableTimeSheet.getItems()
                .stream()
                .mapToDouble(driver -> {
                    return driver.getTimeSheets().size() != 0 ? 90 : 25;
                }).sum();
        if (PREF_HEIGHT_TABLE<=height) {
            tableTimeSheet.setPrefHeight(height + 25);
            tableViewDrivers.setPrefHeight(height + 25);
            tableResult.setPrefHeight(height + 25);
        }else {
            tableTimeSheet.setPrefHeight(PREF_HEIGHT_TABLE);
            tableViewDrivers.setPrefHeight(PREF_HEIGHT_TABLE);
            tableResult.setPrefHeight(PREF_HEIGHT_TABLE);
        }
    }


    /**
     *
     */
    private void initializeTableTimeSheet() {
        tableTimeSheet.setItems(driverObservableList);

        List<LocalDate> days = getListDate();
        listDaysColumn = new LinkedHashMap<>();
        for (LocalDate day : days) {
            TableColumn<Driver, TimeSheet> column = new TableColumn<>(day.toString());
            column.setMaxWidth(100);
            column.setPrefWidth(100);
            listDaysColumn.put(day, column);
        }
        tableTimeSheet.getColumns().addAll(listDaysColumn.values());

        for (Map.Entry<LocalDate, TableColumn<Driver, TimeSheet>> tableColumnDay : listDaysColumn.entrySet()) {
            tableColumnDay.getValue().setCellValueFactory(driverTimeSheetCellDataFeatures -> {
                for (TimeSheet timeSheet : driverTimeSheetCellDataFeatures.getValue().getTimeSheets()) {
                    if (tableColumnDay.getKey().toString().equals(timeSheet.getDayDate())) {
                        return new SimpleObjectProperty<TimeSheet>(timeSheet);
                    }
                }
                return null;
            });
            tableColumnDay.getValue().setCellFactory(new Callback<TableColumn<Driver, TimeSheet>, TableCell<Driver, TimeSheet>>() {
                @Override
                public TableCell<Driver, TimeSheet> call(TableColumn<Driver, TimeSheet> driverTimeSheetTableColumn) {
                    TimeSheetCell timeSheetCell = new TimeSheetCell<>(tableColumnDay.getKey(),
                            timeSheetService,
                            () -> {
                                insertPrefHeightTables();
                                tableViewDrivers.refresh();
                                tableResult.refresh();
                            });
                    timeSheetCell.setTableDriver(tableViewDrivers);
                    return timeSheetCell;
                }
            });
        }

    }

    /**
     * Initialize table with list drivers.
     * Including a list of drivers, create a column.
     * Setting a value in the column and create a custom cell of the column.
     */
    public void initializeTableDrivers() {
        tableViewDrivers.setItems(driverObservableList);

        columnDrivers = new TableColumn<>("Водії");
        tableViewDrivers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableViewDrivers.getColumns().add(columnDrivers);

        columnDrivers.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Driver, Driver>, ObservableValue<Driver>>() {
            @Override
            public ObservableValue<Driver> call(TableColumn.CellDataFeatures<Driver, Driver> param) {
                Driver drivers = param.getValue();
                return new SimpleObjectProperty(drivers);
            }
        });

        columnDrivers.setCellFactory(new Callback<TableColumn<Driver, Driver>, TableCell<Driver, Driver>>() {
            @Override
            public TableCell<Driver, Driver> call(TableColumn<Driver, Driver> tableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Driver item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getLastName()+" "+item.getFirstName()+" "+ item.getFatherName());
                            if (item.getTimeSheets().size() != 0) {
                                setPrefHeight(86);
                            }
                        }
                    }
                };
            }
        });
    }


    /**
     * @return String date which begin month
     */
    public String dateFrom() {
        int month = months.indexOf(monthSpinner.getValue()) + 1;
        int year = spinnerYear.getValue();
        String from;
        if (month < 10) {
            from = String.valueOf(year)
                    + "-0"
                    + String.valueOf(month)
                    + "-01";
        } else {
            from = String.valueOf(year)
                    + "-"
                    + String.valueOf(month)
                    + "-01";
        }
        return from;
    }


    /**
     * @return String date which end moth;
     */
    public String dateTo() {
        int month = months.indexOf(monthSpinner.getValue()) + 1;
        int year = spinnerYear.getValue();
        String to;
        if (month != 12) {
            if (month < 9) {
                to = String.valueOf(year)
                        + "-0"
                        + String.valueOf(month + 1)
                        + "-01";
            } else
                to = String.valueOf(year)
                        + "-"
                        + String.valueOf(month + 1)
                        + "-01";
        } else {
            to = String.valueOf(year + 1)
                    + "-01"
                    + "-01";
        }
        return to;
    }


    /**
     * @return
     */
    public List<LocalDate> getListDate() {
        LocalDate start = LocalDate.parse(dateFrom());
        LocalDate end = LocalDate.parse(dateTo());
        List<LocalDate> totalDates = new ArrayList<>();

        while (!start.isEqual(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }
        return totalDates;
    }


    /**
     *
     */
    public void initializeYearMonth() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);

        spinnerYear.setValueFactory(
                new SpinnerValueFactory
                        .IntegerSpinnerValueFactory(1991, year, year));

        SpinnerValueFactory<String> spinnerValueFactoryMonths =
                new SpinnerValueFactory
                        .ListSpinnerValueFactory<>(months);

        spinnerValueFactoryMonths
                .setValue(months.get(calendar.get(Calendar.MONTH)));

        monthSpinner.setValueFactory(spinnerValueFactoryMonths);
    }


    @FXML
    void openEditTransportWindow(ActionEvent event) {
        EditTransportController dialogController = fxWeaver.getBean(EditTransportController.class);
        Parent parent = fxWeaver.loadView(dialogController.getClass());

        Scene scene = new Scene(parent);
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        updateTableCalendar(event);
    }

    @FXML
    void openCalculateWindow(ActionEvent event) {
        CalculateController dialogController = fxWeaver.getBean(CalculateController.class);
        dialogController.setMonth(dateFrom(), dateTo());
        Parent parent = fxWeaver.loadView(dialogController.getClass());
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        updateTableCalendar(event);
    }

    public void initializeScrollPane() {
        scrollTimeSheet.vvalueProperty().addListener((observableValue, number, t1) -> {
            scrollTableDrivers.setVvalue(t1.doubleValue());
            scrollResult.setVvalue(t1.doubleValue());
        });
        scrollTableDrivers.vvalueProperty().addListener((observableValue, number, t1) -> {
            scrollTimeSheet.setVvalue(t1.doubleValue());
            scrollResult.setVvalue(t1.doubleValue());
        });
        scrollResult.vvalueProperty().addListener((observableValue, number, t1) -> {
            scrollTableDrivers.setVvalue(t1.doubleValue());
            scrollTimeSheet.setVvalue(t1.doubleValue());
        });
    }



    /**
     *
     */
    public void initializeResultTable() {
        tableResult.setItems(driverObservableList);
        columnResult = new TableColumn<>("Підсумок");
        columnResult.setPrefWidth(110);
        columnResult.setMaxWidth(110);
        tableResult.getColumns().add(columnResult);
        columnResult.setCellValueFactory(driverDriverCellDataFeatures -> {
            return new SimpleObjectProperty(driverDriverCellDataFeatures.getValue());
        });

        columnResult.setCellFactory(driverDriverTableColumn -> {
            return new TableCell<Driver, Driver>() {
                @Override
                protected void updateItem(Driver item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && item != null && item.getTimeSheets().size() != 0) {
                        setPrefHeight(86);

                        //Calculate total fuel that driver.
                        double sumFuel = item.getTimeSheets()
                                .stream()
                                .filter(timeSheet -> {
                                    return timeSheet.getTransport().getTypeFuel() == item.getTransport().getTypeFuel();
                                })
                                .mapToDouble(TimeSheet::getUseFuel)
                                .sum();
                        sumFuel = BigDecimal.valueOf(sumFuel)
                                .setScale(2, RoundingMode.HALF_UP)
                                .doubleValue();

                        //Calculate total result that driver
                        double sumResult = item.getTimeSheets()
                                .stream()
                                .filter(timeSheet -> {
                                    return timeSheet.getTransport().getTypeFuel() == item.getTransport().getTypeFuel();
                                })
                                .mapToDouble(TimeSheet::getResultWork)
                                .sum();

                        //Creation string for cell
                        String str = item.getTransport().getTypeFuel().getName()
                                + ":\n "
                                + sumFuel + " л.\n";
                        sumResult = BigDecimal.valueOf(sumResult)
                                .setScale(2, RoundingMode.HALF_UP)
                                .doubleValue();
                        if (item.getTransport().getTypeTransport().equals(0)) {
                            str += "Проїхав:\n "
                                    + sumResult
                                    + " км.\n";
                        } else {
                            int hour = (int) sumResult;
                            double min = sumResult >= 1 ? sumResult % (double) hour : sumResult;

                            min = BigDecimal.valueOf(min)
                                    .setScale(2, RoundingMode.HALF_UP)
                                    .doubleValue();

                            min *= 100;
                            min = min * 60 / 100;
                            str += "Працював:\n "
                                    + hour
                                    + " год. "
                                    + (int) min
                                    + "хв. \n";
                        }
                        setText(str);
                    }
                }
            };
        });
    }

    public void showUsedFuelPreMonth() {
        labelA95.setText(String.valueOf(sumFuel("A-95")));

        labelA92.setText(String.valueOf(sumFuel("A-92")));

        labelA80.setText(String.valueOf(sumFuel("A-80")));

        labelDP.setText(String.valueOf(sumFuel("ДП")));

        labelGAZ.setText(String.valueOf(sumFuel("ГАЗ")));

    }

    private double sumFuel(String typeFuel){
        return driverObservableList.stream()
                .mapToDouble(driver -> {
                    return driver.getTimeSheets()
                            .stream()
                            .filter(timeSheet -> timeSheet.getTransport().getTypeFuel().getName().equals(typeFuel))
                            .mapToDouble(TimeSheet::getUseFuel)
                            .sum();
                })
                .sum();
    }


    @FXML
    void createReport(ActionEvent event) {
        List<TimeSheet> timeSheets = new LinkedList<>();
        for (Driver driver : driverList) {
            List<TimeSheet> sheets = driver.getTimeSheets();
            timeSheets.addAll(sheets);
        }
        try {
            reportService.createFile();
//            reportService.printReport();
            reportService.writeReport(timeSheets, dateFrom());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.showAndWait();
        }
    }

}


