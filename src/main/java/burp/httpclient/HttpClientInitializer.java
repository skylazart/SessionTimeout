package burp.httpclient;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.handler.ssl.SslContext;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public class HttpClientInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    HttpClientInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpProxyHandler(new InetSocketAddress("127.0.0.1", 8080)));

        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        p.addLast(new HttpClientCodec());
        p.addLast(new HttpContentDecompressor());
        p.addLast(new HttpObjectAggregator(1048576));
    }
}
