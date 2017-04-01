package burp;

import burp.UI.MainTab;
import burp.logappender.BurpLogWriterSingleton;
import burp.message.RequestMessage;
import burp.notifier.MessageUpdateNotifier;
import burp.notifier.NotifierRequestMessageSingleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class BurpExtender implements IBurpExtender, IExtensionStateListener, ActionListener, IContextMenuFactory {
    private static final Logger logger = LogManager.getLogger();

    private static final String SEND_TO_SESSION_TIMEOUT = "SEND TO SESSION TIMEOUT";
    private static final String SEND_TO_SESSION_TIMEOUT_MENU_ITEM = "Send to Session Timeout";

    private PrintWriter stdout;
    private IHttpRequestResponse[] messages = null;

    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        callbacks.setExtensionName("Session timeout");
        stdout = new PrintWriter(callbacks.getStdout(), true);

        callbacks.registerContextMenuFactory(this);
        callbacks.registerExtensionStateListener(this);

        callbacks.addSuiteTab(new MainTab(callbacks));

        // Setting log appender
        BurpLogWriterSingleton.getInstance().setWriter(stdout);
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
        MessageUpdateNotifier<RequestMessage> notifier = NotifierRequestMessageSingleton.getInstance().getNotifier();
        if (messages != null) {
            for (IHttpRequestResponse requestResponse : messages) {
                notifier.update(new RequestMessage(requestResponse));
            }
            messages = null;
        }
    }
}
