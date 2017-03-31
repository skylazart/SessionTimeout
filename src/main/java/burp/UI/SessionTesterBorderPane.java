package burp.UI;

import burp.RequestMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/29/17.
 */
public class SessionTesterBorderPane extends BorderPane {
    private boolean inUse;
    private RequestMessage requestMessage = null;

    public SessionTesterBorderPane() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(OptionsBorderPane.class.getResource("/view/sessionLayout.fxml"));
            AnchorPane anchorPane = loader.load();
            setTop(anchorPane);
        } catch (IOException e) {

        }
    }

    public SessionTesterBorderPane(RequestMessage requestMessage) {
        this.inUse = true;
        this.requestMessage = requestMessage;
    }
}
