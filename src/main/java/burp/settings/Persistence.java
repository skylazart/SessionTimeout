package burp.settings;

import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public interface Persistence {
    Settings load() throws IOException, ConfigurationException;
    void save(Settings settings) throws IOException, ConfigurationException;
}
