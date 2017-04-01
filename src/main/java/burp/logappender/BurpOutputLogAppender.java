package burp.logappender;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
@Plugin(name="Burp", category="Core", elementType="appender", printObject=true)
public class BurpOutputLogAppender extends AbstractAppender {
    private static volatile BurpOutputLogAppender instance;

    private BurpLogWriterSingleton burpWriter;

    public BurpOutputLogAppender(String name, Filter filter, Layout<? extends Serializable> layout,
                                 boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        burpWriter = BurpLogWriterSingleton.getInstance();
    }

    @Override
    public void append(LogEvent logEvent) {
        final byte[] b = getLayout().toByteArray(logEvent);
        String s = new String(b);
        burpWriter.logMsg(s);
    }

    @PluginFactory
    public static BurpOutputLogAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @PluginAttribute("otherAttribute") String otherAttribute) {

        if (name == null) {
            LOGGER.error("No name provided for MyCustomAppenderImpl");
            return null;
        }

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        // Save instance
        instance = new BurpOutputLogAppender(name, filter, layout, true);

        return instance;
    }

    public static BurpOutputLogAppender getInstance() {
        return instance;
    }
}
