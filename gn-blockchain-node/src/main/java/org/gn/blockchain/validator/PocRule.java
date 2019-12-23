package org.gn.blockchain.validator;

import java.math.BigInteger;

import org.gn.blockchain.config.BlockchainConfig;
import org.gn.blockchain.config.SystemProperties;
import org.gn.blockchain.core.BlockHeader;
import org.gn.blockchain.db.BlockStore;
import org.gn.blockchain.db.ByteArrayWrapper;
import org.gn.blockchain.util.Convert;
import org.apache.commons.collections4.CollectionUtils;

public class PocRule extends DependentBlockHeaderRule{
	SystemProperties config;
	BlockStore blockStore;
	
	public PocRule(SystemProperties config,BlockStore blockStore) {
		this.config = config;
		this.blockStore = blockStore;
	}
	
	@Override
	public boolean validate(BlockHeader header, BlockHeader parent) {
		if (header.isGenesis()){
			return true;
		}
		BlockchainConfig blockConfig = config.getBlockchainConfig().getConfigForBlock(header.getNumber());
		//check block time
		long nowTime = System.currentTimeMillis()/1000;
		if( nowTime < header.getTimestamp() - 30) {
			errors.add(String.format("block %s is bad time", header.getShortDescr()));
        	return false;
		}
		
		//check node miner
		if(CollectionUtils.isNotEmpty(blockConfig.getMinerNodes())) {
			boolean isNode = false;
			ByteArrayWrapper minerAddr = new ByteArrayWrapper(header.getCoinbase());
			for(ByteArrayWrapper addr : blockConfig.getMinerNodes()) {
				if(addr.equals(minerAddr)) {
					isNode = true;
					break;
				}
			}
			if(!isNode) {
				errors.add(String.format("block miner %s is not valid node", minerAddr.toString()));
	        	return false;
			}
		}
		
		//check block time
		if(header.getTimestamp() - parent.getTimestamp() < blockConfig.getEquilibriumTime() - 5) {
    		errors.add(String.format("block %s is not good enough,but sended", header.getShortDescr()));
        	return false;
		}
		
		//check deadline
		return true;
	}
}
