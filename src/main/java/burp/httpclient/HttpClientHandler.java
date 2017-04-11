package burp.httpclient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final HttpClientAdapter httpCLientAdapter;
    private HttpResponse httpResponse;
    private List<HttpContent> httpContentList = new ArrayList<>();

    public HttpClientHandler(HttpClientAdapter httpClientAdapter) {
        this.httpCLientAdapter = httpClientAdapter;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpResponse) {
            httpResponse = (HttpResponse) msg;
        }
        if (msg instanceof HttpContent) {
            httpContentList.add((HttpContent) msg);

            if (msg instanceof LastHttpContent) {
                ctx.close();

                if (httpCLientAdapter != null)
                    httpCLientAdapter.onDataRead(httpResponse, httpContentList);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();

        if (httpCLientAdapter != null)
            httpCLientAdapter.onError(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {

                if (httpCLientAdapter != null)
                    httpCLientAdapter.onError(ctx, new IOException("Connection timeout"));

                ctx.close();
            }
        }
    }

    public void errorConnecting() {
        if (httpCLientAdapter != null)
            httpCLientAdapter.onErrorConnecting();
    }
}
