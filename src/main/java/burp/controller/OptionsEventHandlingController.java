package burp.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/31/17.
 */
public class OptionsEventHandlingController {
    @FXML
    private Label lblTotal;

    @FXML
    private CheckBox chk1m;
    @FXML
    private CheckBox chk2m;
    @FXML
    private CheckBox chk4m;
    @FXML
    private CheckBox chk6m;
    @FXML
    private CheckBox chk8m;
    @FXML
    private CheckBox chk12m;
    @FXML
    private CheckBox chk16m;
    @FXML
    private CheckBox chk20m;
    @FXML
    private CheckBox chk24m;

    public OptionsEventHandlingController() {

    }

    public void chkIntervalsUpdated(ActionEvent actionEvent) {
        updateTotalTime();
    }

    @FXML
    private void initialize() {
        chk1m.setOnAction(event -> updateTotalTime());
        chk2m.setOnAction(event -> updateTotalTime());
        chk4m.setOnAction(event -> updateTotalTime());
        chk6m.setOnAction(event -> updateTotalTime());
        chk8m.setOnAction(event -> updateTotalTime());
        chk12m.setOnAction(event -> updateTotalTime());
        chk16m.setOnAction(event -> updateTotalTime());
        chk20m.setOnAction(event -> updateTotalTime());
        chk24m.setOnAction(event -> updateTotalTime());

        updateTotalTime();
    }

    private void updateTotalTime() {
        int total = 0;
        if (chk1m.isSelected())
            total += 1;
        if (chk2m.isSelected())
            total += 2;
        if (chk4m.isSelected())
            total += 4;
        if (chk6m.isSelected())
            total += 6;
        if (chk8m.isSelected())
            total += 8;
        if (chk12m.isSelected())
            total += 12;
        if (chk16m.isSelected())
            total += 16;
        if (chk20m.isSelected())
            total += 20;
        if (chk24m.isSelected())
            total += 24;

        lblTotal.setText(Integer.toString(total) + " minutes");
    }
}
