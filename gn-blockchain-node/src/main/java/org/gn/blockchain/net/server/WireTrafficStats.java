package org.gn.blockchain.net.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;

import org.gn.blockchain.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

import static org.gn.blockchain.util.Utils.sizeToStr;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class WireTrafficStats  implements Runnable  {
    private final static Logger logger = LoggerFactory.getLogger("net");

    private ScheduledExecutorService executor;
    public final TrafficStatHandler tcp = new TrafficStatHandler();
    public final TrafficStatHandler udp = new TrafficStatHandler();

    public WireTrafficStats() {
        executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("WireTrafficStats-%d").build());
        executor.scheduleAtFixedRate(this, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
    	//!!
//        logger.info("TCP: " + tcp.stats());
//        logger.info("UDP: " + udp.stats());
    }

    @PreDestroy
    public void close() {
        executor.shutdownNow();
    }

    @ChannelHandler.Sharable
    static class TrafficStatHandler extends ChannelDuplexHandler {
        long outSizeTot;
        long inSizeTot;
        AtomicLong outSize = new AtomicLong();
        AtomicLong inSize = new AtomicLong();
        AtomicLong outPackets = new AtomicLong();
        AtomicLong inPackets = new AtomicLong();
        long lastTime = System.currentTimeMillis();

        public String stats() {
            long out = outSize.getAndSet(0);
            long outPac = outPackets.getAndSet(0);
            long in = inSize.getAndSet(0);
            long inPac = inPackets.getAndSet(0);
            outSizeTot += out;
            inSizeTot += in;
            long curTime = System.currentTimeMillis();
            long d = (curTime - lastTime);
            long outSpeed = out * 1000 / d;
            long inSpeed = in * 1000 / d;
            lastTime = curTime;
            return "Speed in/out " + sizeToStr(inSpeed) + " / " + sizeToStr(outSpeed) +
                    "(sec), packets in/out " + inPac + "/" + outPac +
                    ", total in/out: " + sizeToStr(inSizeTot) + " / " + sizeToStr(outSizeTot);
        }


        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            inPackets.incrementAndGet();
            if (msg instanceof ByteBuf) {
                inSize.addAndGet(((ByteBuf) msg).readableBytes());
            } else if (msg instanceof DatagramPacket) {
                inSize.addAndGet(((DatagramPacket) msg).content().readableBytes());
            }
            super.channelRead(ctx, msg);
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            outPackets.incrementAndGet();
            if (msg instanceof ByteBuf) {
                outSize.addAndGet(((ByteBuf) msg).readableBytes());
            } else if (msg instanceof DatagramPacket) {
                outSize.addAndGet(((DatagramPacket) msg).content().readableBytes());
            }
            super.write(ctx, msg, promise);
        }
    }
}
