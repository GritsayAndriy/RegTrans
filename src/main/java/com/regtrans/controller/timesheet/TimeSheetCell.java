package com.regtrans.controller.timesheet;

import com.regtrans.model.Driver;
import com.regtrans.model.TimeSheet;
import com.regtrans.service.TimeSheetService;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.sqlite.SQLiteException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


public class TimeSheetCell<T> extends TableCell<T, TimeSheet> {


    private CheckBox isWorked;
    private TextField fieldDistance;
    private TextField fieldFlue;
    VBox vbox;
    LocalDate dateColumn;
    private TimeSheetService timeSheetService;

    TableColumn<Driver, String> columnDriver;
    TableView<Driver> tableDriver;
    Runnable updateTables;


    public TimeSheetCell(LocalDate key, TimeSheetService timeSheetService, Runnable updateTable) {
        this.updateTables = updateTable;
        dateColumn = key;
        this.timeSheetService = timeSheetService;
        vbox = new VBox();
        vbox.setMaxHeight(80);
        vbox.setAlignment(Pos.CENTER);
        setGraphic(vbox);
    }


    @Override
    protected void updateItem(TimeSheet item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && isWorked ==null) {
            initializeCheckCell(item);
            if (item != null) {
                isWorked.setSelected(true);
                vbox.getChildren().add(new Label(createCell(item)));
                vbox.setPrefHeight(80);
            }
        }

    }

    private void initializeEditCell() {
        vbox.setPrefHeight(88.0);
        vbox.setMaxWidth(81.0);
        fieldDistance = new TextField();
        fieldFlue = new TextField();
        vbox.getChildren().addAll(fieldDistance, fieldFlue);
    }

    private String createCell(TimeSheet item){
        String strCell = item.getTransport()
                .getBrand()
                +" "
                + item.getTransport()
                .getModel()+"\n";

        if (item.getTransport().getTypeTransport().equals(0)){
            strCell+= BigDecimal.valueOf(item.getResultWork())
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue()
                    +" км.\n";
        }else {
            double time = BigDecimal.valueOf(item.getResultWork())
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            int hour = (int) time;
            double min = time>=1?time% (double) hour: time;

            min = BigDecimal.valueOf(min)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            min *= 100;
            min = min*60/100;

            strCell+= + hour
                    + " год. "
                    +(int) min
                    +"хв. \n";
        }
            strCell+=BigDecimal.valueOf(item.getUseFuel())
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue()
                    +" л. "
                    +item.getTransport().getTypeFuel().getName()+"\n";

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

    @Override
    public void startEdit() {
        super.startEdit();
    }

    @Override
    public void commitEdit(TimeSheet newValue) {
        super.commitEdit(newValue);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
    }

    public void setColumnDriver(TableColumn<Driver, String> columnDriver) {
        this.columnDriver = columnDriver;
    }

    public void setTableDriver(TableView<Driver> tableDriver) {
        this.tableDriver = tableDriver;
    }
}
