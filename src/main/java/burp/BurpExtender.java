package burp;

import burp.UI.MainTab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class BurpExtender implements IBurpExtender, IExtensionStateListener, ActionListener, IContextMenuFactory {
    public static final String SEND_TO_SESSION_TIMEOUT = "SEND TO SESSION TIMEOUT";
    public static final String SEND_TO_SESSION_TIMEOUT_MENU_ITEM = "Send to Session Timeout";

    private IBurpExtenderCallbacks callbacks;
    private PrintWriter stdout;
    private IHttpRequestResponse[] messages = null;

    private SessionNotifierCreationSingleton sessionNotifierCreationSingleton = SessionNotifierCreationSingleton.getInstance();

    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        callbacks.setExtensionName("Session timeout");
        stdout = new PrintWriter(callbacks.getStdout(), true);

        callbacks.registerContextMenuFactory(this);
        callbacks.registerExtensionStateListener(this);

        callbacks.addSuiteTab(new MainTab(callbacks));
    }

    public void extensionUnloaded() {

    }

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation iContextMenuInvocation) {
        byte selectedContext = iContextMenuInvocation.getInvocationContext();
        messages = iContextMenuInvocation.getSelectedMessages();

        if (selectedContext == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST ||
                selectedContext == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST) {
            List<JMenuItem> menu = new ArrayList<>();
            JMenuItem sendToSessionTimeout = new JMenuItem(SEND_TO_SESSION_TIMEOUT_MENU_ITEM);
            sendToSessionTimeout.setActionCommand(SEND_TO_SESSION_TIMEOUT);
            sendToSessionTimeout.addActionListener(this);

            menu.add(sendToSessionTimeout);
            return menu;
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case SEND_TO_SESSION_TIMEOUT:
                sendToSessionTimeout();
                break;
            default:
                stdout.println("Unknown command received");
        }
    }

    private void sendToSessionTimeout() {
        if (messages != null) {
            for (IHttpRequestResponse requestResponse : messages) {
                stdout.println("-->> Sending to session timeout Tab");
                sessionNotifierCreationSingleton.update(new RequestMessage(requestResponse));
            }

            messages = null;
        }
    }
}
