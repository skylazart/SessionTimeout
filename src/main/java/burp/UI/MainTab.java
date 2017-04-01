package burp.UI;

import burp.IBurpExtenderCallbacks;
import burp.ITab;
import burp.message.RequestMessage;
import burp.notifier.NotifierAdapter;
import burp.uifactory.LayoutFactory;
import burp.uifactory.UITypes;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.IOException;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/22/17.
 */
public class MainTab extends Tab implements ITab, NotifierAdapter<RequestMessage> {
    private static final String SESSION_TIMEOUT = "Session Timeout";
    private final IBurpExtenderCallbacks callbacks;
    private JFXPanel mainJfxPanel;

    public MainTab(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        mainJfxPanel = new JFXPanel();

        Platform.runLater(() -> {
            initFX();
        });
    }

    private void initFX() {
        try {
            AnchorPane mainAnchorPane = LayoutFactory.makeLayout(UITypes.MAIN_LAYOUT);
            Scene scene = new Scene(mainAnchorPane);
            mainJfxPanel.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTabCaption() {
        return SESSION_TIMEOUT;
    }

    @Override
    public Component getUiComponent() {
        return mainJfxPanel;
    }

    @Override
    public void update(RequestMessage data) {

    }
}
