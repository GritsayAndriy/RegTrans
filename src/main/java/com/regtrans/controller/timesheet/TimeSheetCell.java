package com.regtrans.controller.timesheet;

import com.regtrans.model.Driver;
import com.regtrans.model.TimeSheet;
import com.regtrans.model.Trailer;
import com.regtrans.service.Decimal;
import com.regtrans.service.TimeSheetService;
import com.regtrans.service.TrailerService;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.sqlite.SQLiteException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;


public class TimeSheetCell<T> extends TableCell<T, TimeSheet> {


    public static final int HEIGHT_CELL = 95;
    public static final int HEIGHT_CELL_EMPTY = 25;

    private CheckBox isWorked;
    private ComboBox<Trailer> comboTrailer;
    private TextField fieldDistance;
    private TextField fieldFlue;
    private VBox vbox;
    private LocalDate dateColumn;
    private TimeSheetService timeSheetService;

    private TableColumn<Driver, String> columnDriver;
    private TableView<Driver> tableDriver;
    private Runnable updateTables;

    private TrailerService trailerService;


    public TimeSheetCell(LocalDate key, TimeSheetService timeSheetService, Runnable updateTable) {
        this.updateTables = updateTable;
        this.dateColumn = key;
        this.timeSheetService = timeSheetService;
        vbox = new VBox();
        vbox.setMaxHeight(HEIGHT_CELL);
        vbox.setAlignment(Pos.CENTER);
        setGraphic(vbox);

    }

    public void setTableDriver(TableView<Driver> tableDriver) {
        this.tableDriver = tableDriver;
    }

    public void setTrailerService(TrailerService trailerService) {
        this.trailerService = trailerService;
    }

    @Override
    protected void updateItem(TimeSheet item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && isWorked == null) {
            initializeCheckCell(item);
            if (item != null) {
                isWorked.setSelected(true);
                vbox.getChildren().add(new Label(createCell(item)));
                setPrefHeight(HEIGHT_CELL);
            }
        }

    }


    private String createCell(TimeSheet item) {
        String strCell = item.getTransport()
                .getBrand()
                + " "
                + item.getTransport()
                .getModel() + "\n";

        if (item.getTransport().getTypeTransport().equals(0)) {
            strCell += Decimal.cleaningExtra(item.getResultWork())
                    + " км.\n";
        } else {
            double time = Decimal.cleaningExtra(item.getResultWork());

            int hour = (int) time;
            double min = time >= 1 ? time % (double) hour : time;

            min = BigDecimal.valueOf(min)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            min *= 100;
            min = min * 60 / 100;

            strCell += +hour
                    + " год. "
                    + (int) min
                    + "хв. \n";
        }
        strCell += BigDecimal.valueOf(item.getUseFuel())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue()
                + " л. "
                + item.getTransport().getTypeFuel().getName() + "\n";

        if (item.getTrailer()!=null) {
            strCell += "Причіп: " + item.getTrailer().getBrand()
                    + "\n  "
                    + item.getTrailer().getNumber();

        }
        return strCell;
    }

    private void initializeCheckCell(TimeSheet item) {
        isWorked = new CheckBox();
        isWorked.setOnAction(actionEvent -> {
            Driver driver = (Driver) this.getTableRow().getItem();
            if (isWorked.isSelected()) {
                TimeSheet timeSheet = new TimeSheet();
                timeSheet.setDayDate(dateColumn.toString());
                timeSheet.setDriver(driver);
                timeSheet.setTransport(driver.getTransport());
                timeSheet.setResultWork(0);
                timeSheet.setUseFuel(0);
                showTrailerChoice(driver).ifPresent(timeSheet::setTrailer);
                try {
                    driver.addTimeSheets(timeSheetService.save(timeSheet));
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }

                updateTables.run();
                getTableView().refresh();
//                tableDriver.refresh();


            } else {
                driver.removeTimeSheet(timeSheetService.unselectTimeSheet(item));
                updateTables.run();
                getTableView().refresh();
//                tableDriver.refresh();

            }
        });
        vbox.getChildren().addAll(isWorked);
    }

    public Optional<Trailer> showTrailerChoice(Driver driver){
        Optional<Trailer> result;
        if (driver.getTransport().getTypeTransport() == 0) {
            ChoiceDialog<Trailer> dialog = new ChoiceDialog<>(null,trailerService.getTrailers());
            dialog.setTitle("Вибір причіпа");
            dialog.setHeaderText("Виберіть причіп або відмовтеся");
            dialog.setContentText("Причіп:");
            result = dialog.showAndWait();
            return result;
        }else {
            return Optional.empty();
        }
    }
}
