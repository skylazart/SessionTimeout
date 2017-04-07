package burp.messagefactory;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/3/17.
 */
class InvalidMessage extends Exception {
    InvalidMessage(String s) {
        super(s);
    }
}
