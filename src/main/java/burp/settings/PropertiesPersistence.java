package burp.settings;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public class PropertiesPersistence implements Persistence {
    private static final String CONFIG_FILENAME = "config.properties";
    private static final String CONFIG_TEMP_FILENAME = "temp.properties";
    private static final Logger logger = LogManager.getLogger();
    private URL url;
    private URL urlTemp;

    public PropertiesPersistence() throws IOException, ConfigurationException {
        ClassLoader classLoader = getClass().getClassLoader();
        url = classLoader.getResource(CONFIG_FILENAME);
        urlTemp = classLoader.getResource(CONFIG_TEMP_FILENAME);

        if (url == null || urlTemp == null)
            throw new IOException("Can not open configuration resources");
    }

    private Settings load(BufferedReader br) throws IOException, ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration();
        config.read(br);

        Settings settings = new Settings();
        settings.setIntervals1m(config.getBoolean("intervals.1m"));
        settings.setIntervals2m(config.getBoolean("intervals.2m"));
        settings.setIntervals4m(config.getBoolean("intervals.4m"));
        settings.setIntervals6m(config.getBoolean("intervals.6m"));
        settings.setIntervals8m(config.getBoolean("intervals.8m"));
        settings.setIntervals12m(config.getBoolean("intervals.12m"));
        settings.setIntervals16m(config.getBoolean("intervals.16m"));
        settings.setIntervals20m(config.getBoolean("intervals.20m"));
        settings.setIntervals24m(config.getBoolean("intervals.24m"));
        settings.setUseBurp(config.getBoolean("proxy.useBurp"));

        return settings;
    }

    @Override
    public Settings load() throws IOException, ConfigurationException {
        BufferedReader br = new BufferedReader(new FileReader(url.getFile()));
        Settings settings = load(br);
        br.close();
        return settings;
    }

    @Override
    public void save(Settings settings) throws IOException, ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration();
        config.setProperty("intervals.1m", settings.isIntervals1m());
        config.setProperty("intervals.2m", settings.isIntervals2m());
        config.setProperty("intervals.4m", settings.isIntervals4m());
        config.setProperty("intervals.6m", settings.isIntervals6m());
        config.setProperty("intervals.8m", settings.isIntervals8m());
        config.setProperty("intervals.12m", settings.isIntervals12m());
        config.setProperty("intervals.16m", settings.isIntervals16m());
        config.setProperty("intervals.20m", settings.isIntervals20m());
        config.setProperty("intervals.24m", settings.isIntervals24m());
        config.setProperty("proxy.useBurp", settings.useBurp());

        config.write(new FileWriter("/tmp/xx.txt"));
    }
}
