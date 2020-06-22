package com.regtrans.controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public class ProgressIndicatorAlert<P> {

    Task<Void> taskWorker;
    private final ProgressIndicator progressIndicator = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
    private final Stage dialog = new Stage(StageStyle.UNDECORATED);
    private final Label label = new Label();
    private final Group root = new Group();
    private final Scene scene = new Scene(root, 330, 120, Color.WHITE);
    private final BorderPane mainPane = new BorderPane();
    private final VBox vbox = new VBox();


    /**
     *
     */
    public ProgressIndicatorAlert(Window owner, String label) {
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner);
        dialog.setResizable(false);
        this.label.setText(label);
    }

    public void start(Runnable func) {
        setupDialog();
        setupWorkerThread(func);
        dialog.show();
        new Thread(taskWorker).start();
    }

    private void setupDialog() {
        root.getChildren().add(mainPane);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinSize(330, 120);
        vbox.getChildren().addAll(label,progressIndicator);
        mainPane.setTop(vbox);
        dialog.setScene(scene);
    }

    private void setupWorkerThread(Runnable func) {

        taskWorker = new Task<Void>() {
            @Override
            public Void call() {
                func.run();
                return null;
            }
        };

        EventHandler<WorkerStateEvent> eh = event -> {
            progressIndicator.progressProperty().unbind();
            dialog.close();

        };

        taskWorker.setOnSucceeded(eh);
        taskWorker.setOnFailed(eh);

    }

}