package org.gn.blockchain.validator;

import org.gn.blockchain.core.BlockHeader;
import org.gn.blockchain.util.FastByteComparisons;
import org.spongycastle.util.encoders.Hex;

public class BlockCustomHashRule extends BlockHeaderRule {

    public final byte[] blockHash;

    public BlockCustomHashRule(byte[] blockHash) {
        this.blockHash = blockHash;
    }

    @Override
    public ValidationResult validate(BlockHeader header) {
        if (!FastByteComparisons.equal(header.getHash(), blockHash)) {
            return fault("Block " + header.getNumber() + " hash constraint violated. Expected:" +
                    Hex.toHexString(blockHash) + ", got: " + Hex.toHexString(header.getHash()));
        }
        return Success;
    }
}
