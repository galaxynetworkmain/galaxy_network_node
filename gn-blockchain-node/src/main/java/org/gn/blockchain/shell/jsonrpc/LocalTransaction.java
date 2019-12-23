package org.gn.blockchain.shell.jsonrpc;

import org.gn.blockchain.core.Transaction;

/**
 * Transaction for making constant calls without changing network state.
 */
public class LocalTransaction extends Transaction {

    public LocalTransaction(byte[] rawData) {
        super(rawData);
    }

    public void setSender(byte[] sendAddress) {
        this.sendAddress = sendAddress;
    }
}
