package com.regtrans.controller;

import com.regtrans.controller.validation.Validation;
import com.regtrans.controller.widgets.MaskField;
import com.regtrans.model.Trailer;
import com.regtrans.service.TrailerService;
import javafx.beans.InvalidationListener;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sqlite.SQLiteException;

@Component
@FxmlView("edit_trailer.fxml")
public class TrailerController {

    @FXML
    private ListView<Trailer> listViewTrailers;

    @FXML
    private TextField fieldBrand;

    @FXML
    private MaskField fieldNumber;

    @FXML
    private Button btnAdding;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    private TrailerService trailerService;
    private ObservableList<Trailer> listTrailers = FXCollections.observableArrayList();

    @Autowired
    public void setTrailerService(TrailerService trailerService) {
        this.trailerService = trailerService;
    }

    @FXML
    public void initialize() {
        listTrailers.setAll(trailerService.getTrailers());
        listViewTrailers.setItems(listTrailers);
        listViewTrailers.setCellFactory(trailerListView ->
                new ListCell<>() {
                    @Override
                    protected void updateItem(Trailer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            setText(item.getBrand() + "\n    " + item.getNumber());
                        } else
                            setText(null);
                    }
                }
        );
        listViewTrailers.getSelectionModel().selectedItemProperty().addListener((observableValue, trailer, t1) -> {
            if (t1 != null) {
                fieldBrand.setText(t1.getBrand());
                fieldNumber.setText(t1.getNumber());
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);
            }
        });

        ChangeListener<String> changeListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                validationFields(s,t1);
                if (isEmpty()) {
                    btnAdding.setDisable(true);
                } else
                    btnAdding.setDisable(false);
            }
        };
        fieldBrand.textProperty().addListener(changeListener);
        fieldNumber.textProperty().addListener(changeListener);
    }

    @FXML
    void addTrailer(ActionEvent event) {
        Trailer trailer = new Trailer();
        trailer.setBrand(fieldBrand.getText());
        trailer.setNumber(fieldNumber.getText());
        try {
            trailer = trailerService.save(trailer);
            listTrailers.add(trailer);
        } catch (SQLiteException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Помилка");
            alert.setHeaderText("Помилка зберігання причіпа");
            alert.setContentText("Можливо вже існує причіп з таким державним номерем");
            alert.showAndWait();
        } finally {
            resetForm();
        }
    }

    @FXML
    void delete(ActionEvent event) {
        Trailer trailer = listViewTrailers.getFocusModel().getFocusedItem();
        int index = listTrailers.indexOf(trailer);
        trailerService.delete(trailer);
        listTrailers.remove(index);
        resetForm();
    }

    @FXML
    void update(ActionEvent event) {
        Trailer trailer = listViewTrailers.getFocusModel().getFocusedItem();
        int index = listTrailers.indexOf(trailer);
        trailer.setBrand(fieldBrand.getText());
        trailer.setNumber(fieldNumber.getText());
        trailer = trailerService.update(trailer);
        listTrailers.set(index, trailer);
        resetForm();
    }

    @FXML
    void cancel(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    private void resetForm() {
        fieldBrand.clear();
        fieldNumber.setPlaceholder("XX0000YY");
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        listViewTrailers.refresh();
        listViewTrailers.getSelectionModel().clearSelection();
    }

    private boolean isEmpty() {
        return fieldBrand.getText().isEmpty() ||
                fieldNumber.getText().isEmpty();
    }


    private void validationFields(String oldValue, String newValue) {
        if (fieldBrand.getText().equals(newValue)) {
            String text = fieldBrand.getText();
            if (Validation.isModel(text) && Validation.validateLength(text)) {
                text = text.toUpperCase();
                text = Validation.oneSpace(text);
                fieldBrand.setText(text);
            } else {
                fieldBrand.setText(oldValue);
            }
        } else if (fieldNumber.getText().equals(newValue)) {
            String text = fieldNumber.getText();
            text = text.toUpperCase();
            fieldNumber.setText(text);
        }
    }
}
