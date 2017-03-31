package burp.UI;

import burp.IBurpExtenderCallbacks;
import burp.RequestMessage;
import burp.SessionNotifierCreationSingleton;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

import java.io.PrintWriter;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/22/17.
 */
public class SessionFXPanel extends JFXPanel {
    private final IBurpExtenderCallbacks callsbacks;
    private SessionNotifierCreationSingleton sessionNotifier = SessionNotifierCreationSingleton.getInstance();
    private PrintWriter stdout;

    public SessionFXPanel(IBurpExtenderCallbacks callbacks) {
        this.callsbacks = callbacks;
        stdout = new PrintWriter(callbacks.getStdout(), true);
        sessionNotifier.addListener(this);

        showSessions();
    }

    private void showSessions() {
        SessionTesterBorderPane sessionTesterBorderPane = new SessionTesterBorderPane();
        setScene(new Scene(sessionTesterBorderPane));
    }


    public void update(RequestMessage requestMessage) {
        if (requestMessage != null) {
            byte[] bytes = requestMessage.getHttpRequest().getRequest();
            stdout.println(new String(bytes));
        }
    }
}
