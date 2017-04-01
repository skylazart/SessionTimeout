package burp.notifier;

import burp.message.RequestMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/31/17.
 */
public class MessageUpdateNotifier<T> {
    public List<NotifierAdapter<T>> listOfAdapters = new ArrayList<>();
    private Object b;

    public MessageUpdateNotifier() {

    }

    public void update(T data) {
        synchronized (b) {
            for (NotifierAdapter<T> adapter: listOfAdapters)
                adapter.update(data);
        }
    }

    public void register(NotifierAdapter<T> adapter) {
        synchronized (b) {
            listOfAdapters.add(adapter);
        }
    }
}
