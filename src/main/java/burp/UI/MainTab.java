package burp.UI;

import burp.IBurpExtenderCallbacks;
import burp.ITab;
import burp.controller.MainController;
import burp.message.BurpRequestMessage;
import burp.notifier.NotifierAdapter;
import burp.notifier.NotifierRequestMessageSingleton;
import burp.uifactory.LayoutFactory;
import burp.uifactory.UITypes;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/22/17.
 */
public class MainTab extends Tab implements ITab, NotifierAdapter<BurpRequestMessage> {
    private static final Logger logger = LogManager.getLogger();

    private String SESSION_TIMEOUT = "Session Timeout";
    private JFXPanel mainJfxPanel;

    public MainTab(IBurpExtenderCallbacks callbacks) {
        try {
            mainJfxPanel = new JFXPanel();
            Platform.runLater(this::initFX);
        } catch (RuntimeException e) {
            logger.error("Runtime exception {}", e.getMessage(), e);
        }
    }

    private void initFX() {
        logger.debug("Initiating FX");

        try {
            LayoutFactory<MainController> layoutFactory = new LayoutFactory<>();
            AnchorPane mainAnchorPane = layoutFactory.makeLayout(UITypes.MAIN_LAYOUT);

            Scene scene = new Scene(mainAnchorPane);
            mainJfxPanel.setScene(scene);

            NotifierRequestMessageSingleton.getInstance().getNotifier().register(this);
        } catch (IOException|RuntimeException e) {
            logger.error("Error loading {}: {}", UITypes.MAIN_LAYOUT, e.getMessage(), e);
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
    public void update(BurpRequestMessage data) {
        logger.debug("MainTab changing color to RED");
    }
}
