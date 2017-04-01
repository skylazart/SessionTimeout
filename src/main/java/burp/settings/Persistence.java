package burp.settings;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public interface Persistence {
    Settings load();
    void save(Settings settings);
}
