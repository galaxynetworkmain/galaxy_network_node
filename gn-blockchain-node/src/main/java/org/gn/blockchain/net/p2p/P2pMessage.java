package org.gn.blockchain.net.p2p;

import org.gn.blockchain.net.message.Message;

public abstract class P2pMessage extends Message {

    public P2pMessage() {
    }

    public P2pMessage(byte[] encoded) {
        super(encoded);
    }

    public P2pMessageCodes getCommand() {
        return P2pMessageCodes.fromByte(code);
    }
}
