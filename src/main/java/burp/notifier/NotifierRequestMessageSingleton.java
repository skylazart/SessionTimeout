package burp.notifier;

import burp.message.RequestMessage;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/31/17.
 */
public class NotifierRequestMessageSingleton<T> {
    private static NotifierRequestMessageSingleton instance = null;
    private MessageUpdateNotifier<RequestMessage> notifier;

    private NotifierRequestMessageSingleton() {
        notifier = new MessageUpdateNotifier<>();
    }

    public static <T> NotifierRequestMessageSingleton<T> getInstance() {
        if (instance == null)
            instance = new NotifierRequestMessageSingleton();
        return instance;
    }

    public MessageUpdateNotifier<RequestMessage> getNotifier() {
        return notifier;
    }
}
