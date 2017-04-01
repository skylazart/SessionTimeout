package burp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;


/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public class TabController {
    private Double currentProgress = 0.0;

    @FXML
    private Tab sessionTab;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextArea txtRequest;

    @FXML
    private TableView<?> tblView;

    @FXML
    private TableColumn<?, ?> tblTimeColumn;

    @FXML
    private TableColumn<?, ?> tblResultColumn;

    @FXML
    private ButtonBar buttonBar;

    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonCancel;

    @FXML
    private ProgressIndicator progress;

    @FXML
    private Label lblElapsedTime;

    @FXML
    void btnStart(ActionEvent event) {
        System.out.println(txtRequest.getText());
        buttonCancel.setDisable(false);
        buttonStart.setDisable(true);
        progress.setDisable(false);
        progress.setVisible(true);
        progress.setProgress(currentProgress);
    }

    @FXML
    void btnCancel(ActionEvent event) {
        currentProgress += 0.1;
        progress.setProgress(currentProgress);
    }

    @FXML
    void onHelpClick(MouseEvent event) {

    }
}
