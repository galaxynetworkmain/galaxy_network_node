package org.gn.blockchain.config.blockchain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.gn.blockchain.config.ConstantsAdapter;
import org.gn.blockchain.config.blockchain.AbstractConfig;
import org.gn.blockchain.db.ByteArrayWrapper;
import org.gn.blockchain.util.ByteUtil;

public abstract class GNMMainConfig extends AbstractConfig {
	private String[] minerNodeAddr= {
			"0x212615d3048463dc2d84871bfcaf7705fddb958f",
	};
	private static int GNM_CHAIN_ID = 1;
	private static String BLOCK_NAME = "GN";
	private long equilibriumTime = 240;
	private List<ByteArrayWrapper> minerNodeWrapper;
	
	public GNMMainConfig() {
		constants = new ConstantsAdapter(super.getConstants()) {
			@Override
			public String getBlockName() {
				return BLOCK_NAME;
			}
			
            @Override
            public int getMAX_CONTRACT_SZIE() {
                return 0x6000;
            }
        };
        minerNodeWrapper = Arrays.asList(minerNodeAddr).stream()
        		.map(addr->new ByteArrayWrapper(ByteUtil.hexStringToBytes(addr)))
        		.collect(Collectors.toList());
	}

	@Override
	public long getEquilibriumTime() {
		return equilibriumTime;
	}
	
	@Override
    public Integer getChainId() {
        return GNM_CHAIN_ID;
    }

	@Override
	public List<ByteArrayWrapper> getMinerNodes() {
		return minerNodeWrapper;
	}
}