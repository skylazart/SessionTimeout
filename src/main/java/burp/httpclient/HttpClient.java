package burp.httpclient;

import burp.message.SessionTimeoutHttpMessage;
import burp.messagefactory.HttpMessageFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLException;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public class HttpClient {
    private static final Logger logger = LogManager.getLogger();

    private static HttpClient instance = null;

    //private final EventLoopGroup bossGroup;
    private final NioEventLoopGroup group;
    private final Bootstrap bootstrapSsl;
    private final Bootstrap bootstrap;
    private boolean finished;

    private HttpClient() throws SSLException {
        final SslContext sslCtx;
        sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        //this.bossGroup = new NioEventLoopGroup(1);
        this.group = new NioEventLoopGroup();

        this.bootstrapSsl = new Bootstrap();
        this.bootstrap = new Bootstrap();

        bootstrapSsl.group(group)
                .channel(NioSocketChannel.class)
                .handler(new HttpClientInitializer(sslCtx));

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new HttpClientInitializer(null));
    }

    public static synchronized HttpClient getInstance() throws SSLException {
        if (instance == null)
            instance = new HttpClient();

        return instance;
    }

    public void sendRequest(SessionTimeoutHttpMessage request, HttpClientHandler httpClientHandler) {
        String host = request.getHost();
        int port = request.getPort();

        if (request.getProtocol().compareToIgnoreCase("HTTPS") == 0) {
            connectWithSsl(request, httpClientHandler, host, port);
        } else {
            connect(request, httpClientHandler, host, port);
        }
    }

    private void connectWithSsl(SessionTimeoutHttpMessage request, HttpClientHandler httpClientHandler, String host, int port) {
        bootstrapSsl.connect(host, port).addListener((ChannelFutureListener) (ChannelFuture future) -> {
            Channel ch = future.channel();
            if (!ch.isActive()) {
                logger.error("Error establishing the connection");
                httpClientHandler.errorConnecting();
                return;
            }

            ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(60,
                    0, 0));
            ch.pipeline().addLast(httpClientHandler);

            HttpRequest nettyRequest = HttpMessageFactory.makeRequest(request);
            ch.writeAndFlush(nettyRequest);
        });
    }

    private void connect(SessionTimeoutHttpMessage request, HttpClientHandler httpClientHandler, String host, int port) {
        bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
            Channel ch = future.channel();
            if (!ch.isActive()) {
                logger.error("Error establishing the connection");
                httpClientHandler.errorConnecting();
                return;
            }

            ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(60,
                    0, 0));
            ch.pipeline().addLast(httpClientHandler);

            HttpRequest nettyRequest = HttpMessageFactory.makeRequest(request);
            ch.writeAndFlush(nettyRequest);
        });
    }

    public synchronized void finish() {
        if (!finished) {
            //bossGroup.shutdownGracefully();
            group.shutdownGracefully();
            finished = true;
        }
    }
}
