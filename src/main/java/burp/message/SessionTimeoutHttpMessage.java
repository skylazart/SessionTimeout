package burp.message;

import burp.IHttpRequestResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/4/17.
 */
public class SessionTimeoutHttpMessage implements HttpMessage {
    private static final Logger logger = LogManager.getLogger();

    private final boolean request;
    private final String protocol;
    private final String host;
    private final int port;
    private final String method;
    private final String version;
    private final String uri;
    private final List<String> headers;
    private final byte[] raw;

    private final int len;
    private final byte[] body;

    private final Map<String, String> headersByName = new HashMap<>();

    /*
    public SessionTimeoutHttpMessage(BurpRequestMessage burpRequestMessage) {
        this.request = true;
        IHttpRequestResponse request = burpRequestMessage.getHttpRequest();
        this.raw = request.getRequest();
        this.protocol = request.getProtocol();
        this.host = request.getHost();
        this.port = request.getPort();

        this.method = parseMethod(raw);
        this.uri = parseUri(raw);
        this.version = parseVersion(raw);
        this.headers = parseHeaders(raw);
        this.body = parseBody(raw);
    }

    public SessionTimeoutHttpMessage(HttpResponse httpResponse, List<HttpContent> httpContentList) {
        this.request = false;
        this.protocol = httpResponse.protocolVersion().protocolName();
    }
    */

    public SessionTimeoutHttpMessage(byte[] raw, String protocol, String host, int port) {
        this.request = true;
        this.raw = raw;

        this.protocol = "HTTPS";
        this.host = host;
        this.port = port;

        this.method = parseMethod(raw);
        this.uri = parseUri(raw);
        this.version = parseVersion(raw);
        this.headers = parseHeaders(raw);
        this.len = getLength();
        this.body = parseBody(raw, len);
    }

    private int getLength() {
        String l = headersByName.get("Content-Length");
        if (l == null) return 0;
        return Integer.valueOf(l);
    }

    private byte[] parseBody(byte[] raw, int len) {
        return Arrays.copyOfRange(raw, raw.length - len, raw.length);
    }

    private List<String> parseHeaders(byte[] raw) {
        List<String> h = new ArrayList<>();
        BufferedReader br = new BufferedReader(new StringReader(new String(raw)));
        String l;
        try {
            while ((l = br.readLine()) != null && l.length() > 0) {
                if (l.startsWith("GET") || l.startsWith("POST"))
                    continue;

                int idx = l.indexOf(':');
                if (idx < 0)
                    continue;

                h.add(l);
                String k = l.substring(0, idx);
                String v = l.substring(idx + 2);

                headersByName.put(k, v);
            }
            return h;
        } catch (IOException e) {
            logger.error("Error parsing HTTP message", e);
        }

        return null;
    }

    private String parseUri(byte[] raw) {
        int b = 0;
        int e = 0;

        for (int i = method.length(); i < raw.length; i++) {
            if (raw[i] == ' ') {
                if (b == 0) {
                    b = i + 1;
                } else {
                    e = i;
                    break;
                }
            }
        }

        if (b > 0 && e > b)
            return new String(raw, b, e - b);
        return null;
    }

    private String parseVersion(byte[] raw) {
        int b = 0;
        if (method != null)
            b += method.length();
        if (uri != null)
            b += uri.length();

        for (int i = b; i < raw.length - 1; i++)
            if (raw[i] == '\r' && raw[i + 1] == '\n') {
                b = i - 8;
                return new String(raw, b, 8);
            }

        return null;
    }

    private String parseMethod(byte[] raw) {
        for (int i = 0; i < raw.length; i++) {
            if (raw[i] == ' ')
                return new String(raw, 0, i);
        }
        return null;
    }

    @Override
    public boolean isRequest() {
        return request;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public List<String> getHeaders() {
        return headers;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public String getHeaderValueByName(String name) {
        return headersByName.get(name);
    }
}
