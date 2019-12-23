package org.gn.blockchain.config.blockchain;

import java.math.BigInteger;

import org.gn.blockchain.config.ConstantsAdapter;

public class GNMPhase02Config extends GNMMainConfig {
	
	public GNMPhase02Config() {
		constants = new ConstantsAdapter(super.getConstants()) {
			private final BigInteger BLOCK_REWARD = new BigInteger("1800000000000000000000");
			
			@Override
            public BigInteger getBLOCK_REWARD() {
            	return BLOCK_REWARD;
            }
		};
	}
	
	
}