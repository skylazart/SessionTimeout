package burp.messagefactory;

import burp.IHttpRequestResponse;
import burp.message.BurpRequestMessage;
import burp.message.SessionTimeoutHttpMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

import java.util.Map;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/3/17.
 */
public class HttpMessageFactory {
    public static HttpRequest makeRequest(SessionTimeoutHttpMessage request) throws InvalidMessage {
        HttpRequest httpRequest;

        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        HttpVersion httpVersion = HttpVersion.valueOf(request.getVersion());
        ByteBuf b = null;

        if (request.getBody() == null || request.getBody().length == 0) {
            httpRequest = new DefaultFullHttpRequest(httpVersion, httpMethod, request.getUri());
        } else {
            b = Unpooled.directBuffer();
            b.writeBytes(request.getBody());
            httpRequest = new DefaultFullHttpRequest(httpVersion, httpMethod, request.getUri(), b);
        }

        for (Map.Entry<String, String> KV: request.getHeadersKeySet()) {
            if (b != null && request.getBody().length > 0) {
                if (KV.getKey().compareToIgnoreCase("Content-Length") == 0) {
                    httpRequest.headers().add(KV.getKey(), b.readableBytes());
                    continue;
                }
            }
            httpRequest.headers().add(KV.getKey(), KV.getValue());
        }

        return httpRequest;
    }

}
