package burp.uifactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import sun.reflect.CallerSensitive;

import java.io.IOException;


/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/31/17.
 */
public class LayoutFactory {

    @CallerSensitive
    public static <T> T makeLayout(UITypes uiType) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(uiType.getResource());
        return loader.load();
    }
}
