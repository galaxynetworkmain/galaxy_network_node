package org.gn.blockchain.net.eth.handler;

import org.gn.blockchain.net.eth.EthVersion;

public interface EthHandlerFactory {

    /**
     * Creates EthHandler by requested Eth version
     *
     * @param version Eth version
     * @return created handler
     *
     * @throws IllegalArgumentException if provided Eth version is not supported
     */
    EthHandler create(EthVersion version);

}
