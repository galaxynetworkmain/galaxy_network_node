package org.gn.blockchain.net.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultMessageSizeEstimator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import static org.gn.blockchain.util.ByteUtil.toHexString;

import org.gn.blockchain.config.SystemProperties;
import org.gn.blockchain.listener.EthereumListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * This class establishes a listener for incoming connections.
 * See <a href="http://netty.io">http://netty.io</a>.
 *
 */
@Component
public class PeerServer {

    private static final Logger logger = LoggerFactory.getLogger("net");

    private SystemProperties config;

    private ApplicationContext ctx;

    private EthereumListener ethereumListener;

    public EthereumChannelInitializer ethereumChannelInitializer;

    private boolean listening;

    EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;
    ChannelFuture channelFuture;

    @Autowired
    public PeerServer(final SystemProperties config, final ApplicationContext ctx,
                      final EthereumListener ethereumListener) {
        this.ctx = ctx;
        this.config = config;
        this.ethereumListener = ethereumListener;
    }

    public void start(int port) {

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        ethereumChannelInitializer = ctx.getBean(EthereumChannelInitializer.class, "");

        ethereumListener.trace("Listening on port " + port);


        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);

            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.MESSAGE_SIZE_ESTIMATOR, DefaultMessageSizeEstimator.DEFAULT);
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.peerConnectionTimeout());

            b.handler(new LoggingHandler());
            b.childHandler(ethereumChannelInitializer);

            // Start the client.
            logger.info("Listening for incoming connections, port: [{}] ", port);
            logger.info("NodeId: [{}] ", toHexString(config.nodeId()));

            channelFuture = b.bind(port).sync();

            listening = true;
            // Wait until the connection is closed.
            channelFuture.channel().closeFuture().sync();
            logger.debug("Connection is closed");

        } catch (Exception e) {
            logger.error("Peer server error: {} ({})", e.getMessage(), e.getClass().getName());
            throw new Error("Server Disconnected");
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            listening = false;
        }
    }

    public void close() {
        if (listening && channelFuture != null && channelFuture.channel().isOpen()) {
            try {
                logger.info("Closing PeerServer...");
                channelFuture.channel().close().sync();
                logger.info("PeerServer closed.");
            } catch (Exception e) {
                logger.warn("Problems closing server channel", e);
            }
        }
    }

    public boolean isListening() {
        return listening;
    }
}
