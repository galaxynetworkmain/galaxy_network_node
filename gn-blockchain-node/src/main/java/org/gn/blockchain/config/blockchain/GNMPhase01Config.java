package org.gn.blockchain.config.blockchain;

import java.math.BigInteger;

import org.gn.blockchain.config.ConstantsAdapter;

public class GNMPhase01Config extends GNMMainConfig {
	
	public GNMPhase01Config() {
		constants = new ConstantsAdapter(super.getConstants()) {
			private final BigInteger BLOCK_REWARD = new BigInteger("3600000000000000000000");
			
			@Override
            public BigInteger getBLOCK_REWARD() {
            	return BLOCK_REWARD;
            }
		};
	}
	
	
}