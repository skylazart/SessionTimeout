package burp.notifier;

import burp.message.BurpRequestMessage;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/31/17.
 */
public class NotifierRequestMessageSingleton {
    private static NotifierRequestMessageSingleton instance = null;
    private MessageUpdateNotifier<BurpRequestMessage> notifier;

    private NotifierRequestMessageSingleton() {
        notifier = new MessageUpdateNotifier<>();
    }

    public static NotifierRequestMessageSingleton getInstance() {
        if (instance == null)
            instance = new NotifierRequestMessageSingleton();
        return instance;
    }

    public MessageUpdateNotifier<BurpRequestMessage> getNotifier() {
        return notifier;
    }
}
