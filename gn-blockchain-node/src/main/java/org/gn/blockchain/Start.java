package org.gn.blockchain;

import java.io.IOException;
import java.net.URISyntaxException;

import org.gn.blockchain.config.SystemProperties;
import org.gn.blockchain.facade.Ethereum;
import org.gn.blockchain.facade.EthereumFactory;

public class Start {

    public static void main(String args[]) throws IOException, URISyntaxException {
        //CLIInterface.call(args);
        final SystemProperties config = SystemProperties.getDefault();
        final boolean actionBlocksLoader = !config.blocksLoader().isEmpty();
        Ethereum ethereum = EthereumFactory.createEthereum();
        if (actionBlocksLoader) {
            ethereum.getBlockLoader().loadBlocks();
        }
    }

}
