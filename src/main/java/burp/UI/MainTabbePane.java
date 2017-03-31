package burp.UI;

import burp.IBurpExtenderCallbacks;

import javax.swing.*;

public class MainTabbePane extends JTabbedPane {
    public static final String SESSION_TAB_TITLE = "Sessions";
    public static final String OPTIONS_TAB_TITLE = "Options";

    private IBurpExtenderCallbacks callbacks;
    private OptionsFXPanel OptionsFXPanel;
    private SessionFXPanel SessionFXPanel;

    public MainTabbePane(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        OptionsFXPanel = new OptionsFXPanel(callbacks);
        SessionFXPanel = new SessionFXPanel(callbacks);

        this.addTab(SESSION_TAB_TITLE, SessionFXPanel);
        this.addTab(OPTIONS_TAB_TITLE, OptionsFXPanel);

        callbacks.customizeUiComponent(this);
    }
}
