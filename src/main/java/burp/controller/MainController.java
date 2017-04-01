package burp.controller;

import burp.uifactory.LayoutFactory;
import burp.uifactory.UITypes;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/31/17.
 */
public class MainController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane mainLayout;

    @FXML
    private TabPane tabPaneSessions;

    @FXML
    private AnchorPane anchorOptions;

    @FXML
    private CheckBox chk2m;

    @FXML
    private CheckBox chk1m;

    @FXML
    private CheckBox chk6m;

    @FXML
    private CheckBox chk8m;

    @FXML
    private CheckBox chk16m;

    @FXML
    private CheckBox chk20m;

    @FXML
    private Label lblTotal;

    @FXML
    private CheckBox chk4m;

    @FXML
    private CheckBox chk12m;

    @FXML
    private CheckBox chk24m;

    @FXML
    void initialize() {
        assert mainLayout != null : "fx:id=\"mainLayout\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert tabPaneSessions != null : "fx:id=\"tabPaneSessions\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert anchorOptions != null : "fx:id=\"anchorOptions\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert chk2m != null : "fx:id=\"chk2m\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert chk1m != null : "fx:id=\"chk1m\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert chk6m != null : "fx:id=\"chk6m\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert chk8m != null : "fx:id=\"chk8m\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert chk16m != null : "fx:id=\"chk16m\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert chk20m != null : "fx:id=\"chk20m\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert lblTotal != null : "fx:id=\"lblTotal\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert chk4m != null : "fx:id=\"chk4m\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert chk12m != null : "fx:id=\"chk12m\" was not injected: check your FXML file 'mainLayout.fxml'.";
        assert chk24m != null : "fx:id=\"chk24m\" was not injected: check your FXML file 'mainLayout.fxml'.";

        updateTotalOfTime();

        try {
            Tab t = LayoutFactory.makeLayout(UITypes.TAB_LAYOUT);
            t.setText("Test 1");
            t.setId("Test 1");
            Node n = t.getContent();
            //n.addEventHandler(ActionEvent.ACTION, event -> System.out.println("T2 handling" + event));
            tabPaneSessions.getTabs().add(t);

            Tab t2 = LayoutFactory.makeLayout(UITypes.TAB_LAYOUT);
            t2.setText("Test 2");
            t2.setId("Test 2");
            Node n2 = t2.getContent();
            //n2.addEventHandler(ActionEvent.ACTION, event -> System.out.println("T3 handling" + event));
            tabPaneSessions.getTabs().add(t2);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Tab addTab = new Tab("New");
//        addTab.setClosable(false);
//        addTab.setOnSelectionChanged(event -> {
//            System.out.println("adding");
//            tabPaneSessions.getSelectionModel().select(1);
//        });
//
//        tabPaneSessions.getTabs().add(addTab);
    }

    @FXML
    void updateIntervalsCheckBox(ActionEvent event) {
        updateTotalOfTime();
    }

    private void updateTotalOfTime() {
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
