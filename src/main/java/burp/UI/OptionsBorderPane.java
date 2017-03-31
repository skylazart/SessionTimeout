package burp.UI;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/29/17.
 */
public class OptionsBorderPane extends BorderPane {
    public OptionsBorderPane() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(OptionsBorderPane.class.getResource("/view/optionsLayout.fxml"));
            AnchorPane anchorPane = loader.load();
            setTop(anchorPane);

        } catch (IOException e) {

        }
    }
}
