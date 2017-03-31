package burp;

import java.util.Observable;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/22/17.
 */
public class RequestMessage extends Observable {
    private final IHttpRequestResponse httpRequest;

    public RequestMessage(IHttpRequestResponse httpRequest) {
        this.httpRequest = httpRequest;
    }

    public IHttpRequestResponse getHttpRequest() {
        return httpRequest;
    }
}
