package burp;

import burp.UI.SessionFXPanel;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/22/17.
 */
public class SessionNotifierCreationSingleton {
    private static SessionNotifierCreationSingleton instance = null;
    private SessionFXPanel SessionFXPanel = null;

    private SessionNotifierCreationSingleton() {

    }

    public static SessionNotifierCreationSingleton getInstance() {
        if (instance == null)
            instance = new SessionNotifierCreationSingleton();

        return instance;
    }

    public void addListener(SessionFXPanel SessionFXPanel) {
        this.SessionFXPanel = SessionFXPanel;
    }

    public void update(RequestMessage requestMessage) {
        if (SessionFXPanel != null)
            SessionFXPanel.update(requestMessage);
    }
}
