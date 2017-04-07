package burp.notifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/31/17.
 */
public class MessageUpdateNotifier<T> {
    private List<NotifierAdapter<T>> listOfAdapters = new ArrayList<>();
    private final Object mutex = new Object();

    MessageUpdateNotifier() {

    }

    public void update(T data) {
        if (listOfAdapters.size() == 0)
            return;

        synchronized (mutex) {
            for (NotifierAdapter<T> adapter: listOfAdapters)
                adapter.update(data);
        }
    }

    public void register(NotifierAdapter<T> adapter) {
        synchronized (mutex) {
            listOfAdapters.add(adapter);
        }
    }
}
