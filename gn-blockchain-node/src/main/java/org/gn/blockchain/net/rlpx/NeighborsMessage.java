package org.gn.blockchain.net.rlpx;

import static org.gn.blockchain.util.ByteUtil.longToBytesNoLeadZeroes;

import java.util.ArrayList;
import java.util.List;

import org.gn.blockchain.crypto.ECKey;
import org.gn.blockchain.util.ByteUtil;
import org.gn.blockchain.util.RLP;
import org.gn.blockchain.util.RLPItem;
import org.gn.blockchain.util.RLPList;

public class NeighborsMessage extends Message {

    List<Node> nodes;
    long expires;

    @Override
    public void parse(byte[] data) {
        RLPList list = (RLPList) RLP.decode2OneItem(data, 0);

        RLPList nodesRLP = (RLPList) list.get(0);
        RLPItem expires = (RLPItem) list.get(1);

        nodes = new ArrayList<>();

        for (int i = 0; i < nodesRLP.size(); ++i) {
            RLPList nodeRLP = (RLPList) nodesRLP.get(i);
            Node node = new Node(nodeRLP);
            nodes.add(node);
        }
        this.expires = ByteUtil.byteArrayToLong(expires.getRLPData());
    }


    public static NeighborsMessage create(List<Node> nodes, ECKey privKey) {

        long expiration = 90 * 60 + System.currentTimeMillis() / 1000;

        byte[][] nodeRLPs = null;

        if (nodes != null) {
            nodeRLPs = new byte[nodes.size()][];
            int i = 0;
            for (Node node : nodes) {
                nodeRLPs[i] = node.getRLP();
                ++i;
            }
        }

        byte[] rlpListNodes = RLP.encodeList(nodeRLPs);
        byte[] rlpExp = longToBytesNoLeadZeroes(expiration);
        rlpExp = RLP.encodeElement(rlpExp);

        byte[] type = new byte[]{4};
        byte[] data = RLP.encodeList(rlpListNodes, rlpExp);

        NeighborsMessage neighborsMessage = new NeighborsMessage();
        neighborsMessage.encode(type, data, privKey);
        neighborsMessage.nodes = nodes;
        neighborsMessage.expires = expiration;

        return neighborsMessage;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public long getExpires() {
        return expires;
    }


    @Override
    public String toString() {

        long currTime = System.currentTimeMillis() / 1000;

        String out = String.format("[NeighborsMessage] \n nodes [%d]: %s \n expires in %d seconds \n %s\n",
                this.getNodes().size(), this.getNodes(), (expires - currTime), super.toString());

        return out;
    }


}
