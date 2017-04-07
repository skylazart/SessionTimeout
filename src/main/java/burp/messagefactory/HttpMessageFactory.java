package burp.messagefactory;

import burp.IHttpRequestResponse;
import burp.message.BurpRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/3/17.
 */
public class HttpMessageFactory {
    public static HttpRequest makeRequest(BurpRequestMessage burpRequestMessage) throws InvalidMessage {
        IHttpRequestResponse request = burpRequestMessage.getHttpRequest();

        HttpMethod method;
        HttpVersion version;

        byte[] b = request.getRequest();
        String str = new String(b);

        int idxEndMethod = str.indexOf(' ');
        String methodStr = str.substring(0, idxEndMethod);
        switch (methodStr) {
            case "POST":
                method = HttpMethod.POST;
                break;
            case "GET":
                method = HttpMethod.GET;
                break;
            default:
                throw new InvalidMessage("Not recognized method");
        }

        int idxBeginVersion = str.indexOf(" HTTP/1");
        String versionStr = str.substring(idxBeginVersion + 1, idxBeginVersion + 9);
        switch(versionStr) {
            case "HTTP/1.0":
                version = HttpVersion.HTTP_1_0;
                break;
            case "HTTP/1.1":
                version = HttpVersion.HTTP_1_1;
                break;
            default:
                throw new InvalidMessage("Unknown HTTP version");
        }

        String uri = str.substring(idxEndMethod + 1, idxBeginVersion);

        HttpRequest httpRequest = new DefaultFullHttpRequest(version, method, uri);
        httpRequest.headers().add("Host", burpRequestMessage.getHttpRequest().getHost());
        return httpRequest;
    }
}
