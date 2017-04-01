package burp.notifier;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/31/17.
 */
public interface NotifierAdapter <T> {
    void update(T data);
}
