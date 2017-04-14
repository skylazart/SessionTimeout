package burp;

import burp.UI.MainTab;
import burp.logappender.BurpLogWriterSingleton;
import burp.message.BurpRequestMessage;
import burp.notifier.MessageUpdateNotifier;
import burp.notifier.NotifierRequestMessageSingleton;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/31/17.
 */
@SuppressWarnings("unused")
public class BurpExtender implements IBurpExtender, IExtensionStateListener, ActionListener, IContextMenuFactory {
    private static final Logger logger = LogManager.getLogger();

    private static final String SEND_TO_SESSION_TIMEOUT_COMMAND = "SEND TO SESSION TIMEOUT";
    private static final String SEND_TO_SESSION_TIMEOUT_MENU_ITEM = "Send to Session Timeout";

    private static final String COPY_REQUEST_RESPONSE_TO_CLIPBOARD_COMMAND = "COPY REQ/RESP TO CLIPBOARD";
    private static final String COPY_REQUEST_RESPONSE_TO_CLIPBOARD = "Copy to Clipboard";

    private IHttpRequestResponse[] messages = null;

    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        callbacks.setExtensionName("Session timeout");
        // Setting log appender
        PrintWriter stdout = new PrintWriter(callbacks.getStdout(), true);
        BurpLogWriterSingleton.getInstance().setWriter(stdout);

        logger.info("Initializing Session Timeout Burp Extension....");

        callbacks.registerContextMenuFactory(this);
        callbacks.registerExtensionStateListener(this);

        callbacks.addSuiteTab(new MainTab(callbacks));
    }

    public void extensionUnloaded() {
        Platform.setImplicitExit(false);
    }

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation iContextMenuInvocation) {
        byte selectedContext = iContextMenuInvocation.getInvocationContext();
        messages = iContextMenuInvocation.getSelectedMessages();

        JMenuItem copyReqRespToClipboard = new JMenuItem(COPY_REQUEST_RESPONSE_TO_CLIPBOARD);
        copyReqRespToClipboard.setActionCommand(COPY_REQUEST_RESPONSE_TO_CLIPBOARD_COMMAND);
        copyReqRespToClipboard.addActionListener(this);

        if (selectedContext == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST ||
                selectedContext == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST) {

            List<JMenuItem> menu = new ArrayList<>();
            JMenuItem sendToSessionTimeout = new JMenuItem(SEND_TO_SESSION_TIMEOUT_MENU_ITEM);
            sendToSessionTimeout.setActionCommand(SEND_TO_SESSION_TIMEOUT_COMMAND);
            sendToSessionTimeout.addActionListener(this);

            menu.add(sendToSessionTimeout);
            menu.add(copyReqRespToClipboard);

            return menu;
        }

        if (selectedContext == IContextMenuInvocation.CONTEXT_PROXY_HISTORY) {
            List<JMenuItem> menu = new ArrayList<>();
            menu.add(copyReqRespToClipboard);
            return menu;
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case SEND_TO_SESSION_TIMEOUT_COMMAND:
                sendToSessionTimeout();
                break;
            case COPY_REQUEST_RESPONSE_TO_CLIPBOARD_COMMAND:
                copyRequestResponse();
            default:
                logger.error("Unknown command received");
        }
    }

    private void copyRequestResponse() {
        StringBuilder sb = new StringBuilder();

        if (messages != null) {
            for (int i = 0; i < messages.length; i++) {
                IHttpRequestResponse requestResponse = messages[i];
                byte[] requestBytes = requestResponse.getRequest();
                byte[] responseBytes = requestResponse.getResponse();

                if (requestBytes.length > 0) {
                    String t = new String(requestBytes);
                    t = t.replace("\r\n", "\n");

                    sb.append("Request:").append("\n\n");
                    sb.append(t);
                }

                if (responseBytes.length > 0) {
                    String t = new String(responseBytes);
                    t = t.replace("\r\n", "\n");

                    if (sb.length() > 0) {
                        char[] lastChars = new char[2];
                        sb.getChars(sb.length() -3, sb.length() - 1, lastChars, 0);

                        if (lastChars[0] != '\n' && lastChars[1] != '\n')
                            sb.append("\n\n");
                        else if (lastChars[1] == '\n')
                            sb.append("\n");

                    }

                    sb.append("Response:").append("\n\n");
                    sb.append(t).append("\n");
                }
            }
        }

        if (sb.length() == 0)
            return;

        StringSelection stringSelection = new StringSelection(sb.toString());
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    private void sendToSessionTimeout() {
        // AWT and JavaFX interaction
        Platform.runLater(this::sendMessage);
    }

    private synchronized void sendMessage() {
        logger.debug("Sending request message to session timeout tester");
        MessageUpdateNotifier<BurpRequestMessage> notifier = NotifierRequestMessageSingleton.getInstance().getNotifier();
        if (messages != null) {
            for (IHttpRequestResponse requestResponse : messages) {
                notifier.update(new BurpRequestMessage(requestResponse));
            }
            messages = null;
        }
    }
}
