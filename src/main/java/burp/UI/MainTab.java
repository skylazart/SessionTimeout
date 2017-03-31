package burp.UI;

import burp.IBurpExtenderCallbacks;
import burp.ITab;
import javafx.scene.control.Tab;

import java.awt.*;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/22/17.
 */
public class MainTab extends Tab implements ITab {
    public static final String SESSION_TIMEOUT = "Session Timeout";
    private final IBurpExtenderCallbacks callbacks;
    private MainTabbePane mainTabbePane;

    public MainTab(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        mainTabbePane = new MainTabbePane(callbacks);
        callbacks.addSuiteTab(this);
    }

    @Override
    public String getTabCaption() {
        return SESSION_TIMEOUT;
    }

    @Override
    public Component getUiComponent() {
        return mainTabbePane;
    }
}
