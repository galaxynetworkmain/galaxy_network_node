package org.gn.blockchain.net.eth.message;

import static org.gn.blockchain.util.ByteUtil.toHexString;

import java.math.BigInteger;

import org.gn.blockchain.util.ByteUtil;
import org.gn.blockchain.util.RLP;
import org.gn.blockchain.util.RLPList;

/**
 * Wrapper for Ethereum STATUS message. <br>
 *
 * @see EthMessageCodes#STATUS
 */
public class StatusMessage extends EthMessage {

    protected byte protocolVersion;
    protected int networkId;

    /**
     * Total difficulty of the best chain as found in block header.
     */
    protected byte[] totalDifficulty;
    /**
     * The hash of the best (i.e. highest TD) known block.
     */
    protected byte[] bestHash;
    /**
     * The hash of the Genesis block
     */
    protected byte[] genesisHash;

    public StatusMessage(byte[] encoded) {
        super(encoded);
    }

    public StatusMessage(byte protocolVersion, int networkId,
                         byte[] totalDifficulty, byte[] bestHash, byte[] genesisHash) {
        this.protocolVersion = protocolVersion;
        this.networkId = networkId;
        this.totalDifficulty = totalDifficulty;
        this.bestHash = bestHash;
        this.genesisHash = genesisHash;
        this.parsed = true;
    }

    protected synchronized void parse() {
        if (parsed) return;
        RLPList paramsList = (RLPList) RLP.decode2(encoded).get(0);

        this.protocolVersion = paramsList.get(0).getRLPData()[0];
        byte[] networkIdBytes = paramsList.get(1).getRLPData();
        this.networkId = networkIdBytes == null ? 0 : ByteUtil.byteArrayToInt(networkIdBytes);

        byte[] diff = paramsList.get(2).getRLPData();
        this.totalDifficulty = (diff == null) ? ByteUtil.ZERO_BYTE_ARRAY : diff;
        this.bestHash = paramsList.get(3).getRLPData();
        this.genesisHash = paramsList.get(4).getRLPData();

        parsed = true;
    }

    protected void encode() {
        byte[] protocolVersion = RLP.encodeByte(this.protocolVersion);
        byte[] networkId = RLP.encodeInt(this.networkId);
        byte[] totalDifficulty = RLP.encodeElement(this.totalDifficulty);
        byte[] bestHash = RLP.encodeElement(this.bestHash);
        byte[] genesisHash = RLP.encodeElement(this.genesisHash);

        this.encoded = RLP.encodeList( protocolVersion, networkId,
                totalDifficulty, bestHash, genesisHash);
    }

    @Override
    public byte[] getEncoded() {
        if (encoded == null) encode();
        return encoded;
    }

    @Override
    public Class<?> getAnswerMessage() {
        return null;
    }

    public byte getProtocolVersion() {
        parse();
        return protocolVersion;
    }

    public int getNetworkId() {
        parse();
        return networkId;
    }

    public byte[] getTotalDifficulty() {
        parse();
        return totalDifficulty;
    }

    public BigInteger getTotalDifficultyAsBigInt() {
        return new BigInteger(1, getTotalDifficulty());
    }

    public byte[] getBestHash() {
        parse();
        return bestHash;
    }

    public byte[] getGenesisHash() {
        parse();
        return genesisHash;
    }

    @Override
    public EthMessageCodes getCommand() {
        return EthMessageCodes.STATUS;
    }


    @Override
    public String toString() {
        parse();
        return "[" + this.getCommand().name() +
                " protocolVersion=" + this.protocolVersion +
                " networkId=" + this.networkId +
                " totalDifficulty=" + ByteUtil.toHexString(this.totalDifficulty) +
                " bestHash=" + toHexString(this.bestHash) +
                " genesisHash=" + toHexString(this.genesisHash) +
                "]";
    }
}
