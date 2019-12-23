package org.gn.blockchain.net.swarm.bzz;

import org.gn.blockchain.net.message.Message;
import org.gn.blockchain.net.message.MessageFactory;

public class BzzMessageFactory implements MessageFactory {

    @Override
    public Message create(byte code, byte[] encoded) {

        BzzMessageCodes receivedCommand = BzzMessageCodes.fromByte(code);
        switch (receivedCommand) {
            case STATUS:
                return new BzzStatusMessage(encoded);
            case STORE_REQUEST:
                return new BzzStoreReqMessage(encoded);
            case RETRIEVE_REQUEST:
                return new BzzRetrieveReqMessage(encoded);
            case PEERS:
                return new BzzPeersMessage(encoded);
            default:
                throw new IllegalArgumentException("No such message");
        }
    }
}
