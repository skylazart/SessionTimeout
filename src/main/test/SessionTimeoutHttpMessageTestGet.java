import burp.message.SessionTimeoutHttpMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/5/17.
 */
public class SessionTimeoutHttpMessageTestGet {
    private String requestString = "GET /auth/login.do?lang=pt HTTP/1.1\r\n" +
            "Host: testxxx.xx\r\n" +
            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:52.0) Gecko/20100101 Firefox/52.0\r\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
            "Accept-Language: en-US,en;q=0.5\r\n" +
            "Cookie: JSESSIONID=4A36CA29AF411048C82696ED60BF4F92\r\n" +
            "Connection: close\r\n" +
            "Upgrade-Insecure-Requests: 1\r\n";

    private SessionTimeoutHttpMessage msg;

    @Before
    public void setUp() throws Exception {
        msg = new SessionTimeoutHttpMessage(requestString.getBytes(), "HTTPS",
                "testxxx.xx", 443);
    }

    @Test
    public void isRequest() throws Exception {
        assertEquals(msg.isRequest(), true);
    }

    @Test
    public void getProtocol() throws Exception {
        assertEquals(msg.getProtocol(), "HTTPS");
    }

    @Test
    public void getHost() throws Exception {
        assertEquals(msg.getHost(), "testxxx.xx");
    }

    @Test
    public void getPort() throws Exception {
        assertEquals(msg.getPort(), 443);
    }

    @Test
    public void getMethod() throws Exception {
        assertEquals(msg.getMethod(), "GET");
    }

    @Test
    public void getVersion() throws Exception {
        assertEquals(msg.getVersion(), "HTTP/1.1");
    }

    @Test
    public void getUri() throws Exception {
        assertEquals(msg.getUri(), "/auth/login.do?lang=pt");
    }

    @Test
    public void getHeaders() throws Exception {
        List<String> headers = msg.getHeaders();
        assertEquals(headers.size(), 7);
    }

    @Test
    public void getBody() throws Exception {
    }

    @Test
    public void getHeaderValueByName() throws Exception {
        assertEquals(msg.getHeaderValueByName("Host"), "testxxx.xx");
        assertEquals(msg.getHeaderValueByName("User-Agent"),
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:52.0) Gecko/20100101 Firefox/52.0");
        assertEquals(msg.getHeaderValueByName("Accept"),
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        assertEquals(msg.getHeaderValueByName("Accept-Language"), "en-US,en;q=0.5");
        assertEquals(msg.getHeaderValueByName("Cookie"), "JSESSIONID=4A36CA29AF411048C82696ED60BF4F92");
        assertEquals(msg.getHeaderValueByName("Connection"), "close");
        assertEquals(msg.getHeaderValueByName("Upgrade-Insecure-Requests"), "1");
    }

}