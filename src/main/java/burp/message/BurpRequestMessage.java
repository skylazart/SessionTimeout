package burp.message;

import burp.IHttpRequestResponse;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/22/17.
 */
public class BurpRequestMessage {
    private final IHttpRequestResponse httpRequest;

    public BurpRequestMessage(IHttpRequestResponse httpRequest) {
        this.httpRequest = httpRequest;
    }
    public IHttpRequestResponse getHttpRequest() {
        return httpRequest;
    }
}
