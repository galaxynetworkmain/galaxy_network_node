package org.gn.blockchain.config.net;

import org.gn.blockchain.config.blockchain.UknmConfig;

public class UknmNetConfig extends BaseNetConfig {
	
    public UknmNetConfig() {
        add(0, new UknmConfig());
    }
}
