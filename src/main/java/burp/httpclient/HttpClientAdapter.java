package burp.httpclient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;

import java.util.List;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public interface HttpClientAdapter {
    void onDataRead(HttpResponse httpResponse, List<HttpContent> httpContentList);
    void onError(ChannelHandlerContext ctx, Throwable cause);
    void onErrorConnecting();
}
