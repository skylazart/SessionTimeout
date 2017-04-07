package burp.logappender;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public class BurpLogWriterSingleton {
    private static BurpLogWriterSingleton instance = null;
    private static Object mutex = new Object();

    private Queue<String> queue;
    private Timer timer;

    private PrintWriter outputWriter = null;

    private BurpLogWriterSingleton() {
        // Lazy instantiation because it may not be used
        queue = new ConcurrentLinkedDeque<>();
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                flushLogs();
                if (outputWriter != null)
                    timer.cancel();
            }
        }, 0, 500);
    }

    private void flushLogs() {
        if (outputWriter == null)
            return;

        while (!queue.isEmpty()) {
            String s = queue.remove();
            outputWriter.print(s);
            outputWriter.flush();
        }
    }

    public static BurpLogWriterSingleton getInstance() {
        if (instance == null) {
            synchronized (mutex) {
                instance = new BurpLogWriterSingleton();
            }
        }
        return instance;
    }

    public void setWriter(PrintWriter writer) {
        this.outputWriter = writer;
    }

    public void logMsg(String msg) {
        if (this.outputWriter == null)
            queue.add(msg);
        else {
            outputWriter.print(msg);
            outputWriter.flush();
        }
    }
}