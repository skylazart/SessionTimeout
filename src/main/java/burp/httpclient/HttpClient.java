package burp.httpclient;

import burp.message.BurpRequestMessage;
import burp.messagefactory.HttpMessageFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public class HttpClient implements Comparable<HttpClient> {
    private static final Logger logger = LogManager.getLogger();

    private final EventLoopGroup bossGroup;
    private final NioEventLoopGroup group;
    private final Bootstrap bootstrap;

    private final String name;

    public HttpClient(String name) throws SSLException {
        if (name == null)
            throw new IllegalArgumentException("Name can't be null");

        this.name = name;

        final SslContext sslCtx;
        sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        this.bossGroup = new NioEventLoopGroup(1);
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();

        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new HttpClientInitializer(sslCtx));
    }

    public void sendRequest(BurpRequestMessage burpRequestMessage, HttpClientHandler httpClientHandler) throws URISyntaxException {
        String host = burpRequestMessage.getHttpRequest().getHost();
        int port = burpRequestMessage.getHttpRequest().getPort();

        bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
            Channel ch = future.channel();
            ch.pipeline().addLast(httpClientHandler);

            HttpRequest request = HttpMessageFactory.makeRequest(burpRequestMessage);
            ch.writeAndFlush(request);
        });
    }

    public void finish() {
        bossGroup.shutdownGracefully();
        group.shutdownGracefully();
    }

    @Override
    public int compareTo(HttpClient o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpClient)) return false;

        HttpClient that = (HttpClient) o;

        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "HttpClient{" +
                "name='" + name + '\'' +
                '}';
    }
}
