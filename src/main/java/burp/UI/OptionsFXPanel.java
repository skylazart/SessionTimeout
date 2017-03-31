package burp.UI;

import burp.IBurpExtenderCallbacks;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/22/17.
 */
public class OptionsFXPanel extends JFXPanel {
    public OptionsFXPanel(IBurpExtenderCallbacks callbacks) {
        showOPtions();
    }

    private void showOPtions() {
        OptionsBorderPane optionsBorderPane = new OptionsBorderPane();
        setScene(new Scene(optionsBorderPane));
    }
}
