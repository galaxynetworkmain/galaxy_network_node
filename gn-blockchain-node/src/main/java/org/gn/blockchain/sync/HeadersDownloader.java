package org.gn.blockchain.sync;

import static org.gn.blockchain.util.ByteUtil.toHexString;

import java.util.List;

import org.gn.blockchain.config.SystemProperties;
import org.gn.blockchain.core.BlockHeader;
import org.gn.blockchain.core.BlockHeaderWrapper;
import org.gn.blockchain.core.BlockWrapper;
import org.gn.blockchain.core.Blockchain;
import org.gn.blockchain.db.DbFlushManager;
import org.gn.blockchain.db.HeaderStore;
import org.gn.blockchain.db.IndexedBlockStore;
import org.gn.blockchain.net.server.Channel;
import org.gn.blockchain.net.server.ChannelManager;
import org.gn.blockchain.validator.BlockHeaderValidator;
import org.gn.blockchain.validator.PocRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class HeadersDownloader extends BlockDownloader {
    private final static Logger logger = LoggerFactory.getLogger("sync");

    @Autowired
    SyncPool syncPool;

    @Autowired
    ChannelManager channelManager;

    @Autowired
    IndexedBlockStore blockStore;

    @Autowired
    HeaderStore headerStore;

    @Autowired
    DbFlushManager dbFlushManager;

    @Autowired
    Blockchain blockchain;

    byte[] genesisHash;

    int headersLoaded  = 0;

    @Autowired
    public HeadersDownloader(BlockHeaderValidator headerValidator, SystemProperties systemProperties) {
        super(headerValidator);
        setHeaderQueueLimit(200000);
        setBlockBodiesDownload(false);
        logger.info("HeaderDownloader created.");
    }

    public void init(byte[] startFromBlockHash) {
        logger.info("HeaderDownloader init: startHash = " + toHexString(startFromBlockHash));
        SyncQueueReverseImpl syncQueue = new SyncQueueReverseImpl(startFromBlockHash, true);
        super.init(syncQueue, syncPool, "HeadersDownloader");
        syncPool.init(channelManager, blockchain);
    }

    @Override
    protected synchronized void pushBlocks(List<BlockWrapper> blockWrappers) {}

    @Override
    protected void pushHeaders(List<BlockHeaderWrapper> headers) {
        if (headers.get(headers.size() - 1).getNumber() == 0) {
            genesisHash = headers.get(headers.size() - 1).getHash();
        }
        if (headers.get(headers.size() - 1).getNumber() == 1) {
            genesisHash = headers.get(headers.size() - 1).getHeader().getParentHash();
        }
        logger.info(name + ": " + headers.size() + " headers loaded: " + headers.get(0).getNumber() + " - " + headers.get(headers.size() - 1).getNumber());
        for (BlockHeaderWrapper header : headers) {
            headerStore.saveHeader(header.getHeader());
            headersLoaded++;
        }
        dbFlushManager.commit();
    }

    /**
     * Headers download could block chain synchronization occupying all peers
     * Prevents this by leaving one peer without work
     * Fallbacks to any peer when low number of active peers available
     */
    @Override
    Channel getAnyPeer() {
        return syncPool.getActivePeersCount() > 2 ? syncPool.getNotLastIdle() : syncPool.getAnyIdle();
    }

    @Override
    protected int getBlockQueueFreeSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getMaxHeadersInQueue() {
        return getHeaderQueueLimit();
    }

    public int getHeadersLoaded() {
        return headersLoaded;
    }

    @Override
    protected void finishDownload() {
        stop();
    }

    public byte[] getGenesisHash() {
        return genesisHash;
    }

    @Override
    protected boolean isValid(BlockHeader header) {
        return super.isValid(header);
    }
}
