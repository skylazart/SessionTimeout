package burp.settings;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public class PersistenceFactory {
    private static Persistence instance = null;

    public static synchronized Persistence make(PersistenceType type) {
        switch(type) {
            case BURP:
                if (instance == null)
                    instance = new BurpPersistence();
                break;
            case PROPERTIES:
                if (instance == null)
                    instance = new PropertiesPersistence();
                break;
            default:
                throw new IllegalArgumentException("Unknown persistence type");
        }

        return instance;
    }
}
