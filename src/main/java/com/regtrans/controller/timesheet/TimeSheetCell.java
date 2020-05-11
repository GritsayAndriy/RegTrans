package com.regtrans.controller.timesheet;

import com.regtrans.model.Driver;
import com.regtrans.model.TimeSheet;
import com.regtrans.service.TimeSheetService;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.VBox;

import java.time.LocalDate;


public class TimeSheetCell<T> extends TableCell<T, TimeSheet> {


    private CheckBox isWorked;
    private TextField fieldDistance;
    private TextField fieldFlue;
    VBox vbox;
    LocalDate dateColumn;
    private TimeSheetService timeSheetService;


    public TimeSheetCell() {
    }

    public TimeSheetCell(LocalDate key, TimeSheetService timeSheetService) {
        dateColumn = key;
        this.timeSheetService = timeSheetService;
        vbox = new VBox();
//        vbox.setPrefHeight(88.0);
        vbox.setAlignment(Pos.CENTER);
        setGraphic(vbox);
    }


    @Override
    protected void updateItem(TimeSheet item, boolean empty) {
        if (!empty && isWorked ==null) {
            initializeCheckCell(item);
            if (item == null) {
                isWorked.setSelected(false);
//                fieldDistance.setPromptText("км. або год.");
//                fieldFlue.setPromptText("л. ");
            } else {
//                if (dateColumn.toString().equals(item.getDayDate())) {
                isWorked.setSelected(true);
//                setText(String.valueOf(item.getResultWork())+"\n"+String.valueOf(item.getUseFuel()));
                vbox.getChildren().add(new Label(createCell(item)));

//                setText(createCell(item));
//                    fieldDistance.setText(String.valueOf(item.getResultWork()));
//                    fieldFlue.setText(String.valueOf(item.getUseFuel()));
//                }
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
        String strCell = item.getTransport().getBrand()+" "
                + item.getTransport().getModel()+"\n";
        if (item.getTransport().getTypeTransport().equals(0)){
            strCell+= item.getResultWork() +" км.\n";
        }else {
            strCell+=item.getResultWork() + " год.\n";
        }
        if (item.getTransport().getTypeFlue().equals(0)){
            strCell+=item.getUseFuel()+"л. бен.\n";
        }else {
            strCell+=item.getUseFuel()+"л. диз.\n";
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
                timeSheet.setResultWork(20);
                float d = 20 * 0.1f;
                timeSheet.setUseFuel(Math.round(d));
                driver.addTimeSheets(timeSheetService.save(timeSheet));
                getTableView().refresh();
            } else {
                driver.removeTimeSheet(timeSheetService.unselectTimeSheet(item));
                getTableView().refresh();
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
}
