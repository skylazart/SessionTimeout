package burp.message;

import java.util.List;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/4/17.
 */
public interface HttpMessage {
    boolean isRequest();
    String getProtocol();
    String getHost();
    int getPort();
    String getMethod();
    String getVersion();
    String getUri();
    List<String> getHeaders();
    byte[] getBody();
    String getHeaderValueByName(String name);
}
