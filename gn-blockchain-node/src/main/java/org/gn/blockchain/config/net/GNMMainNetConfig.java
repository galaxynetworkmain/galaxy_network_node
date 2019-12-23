package org.gn.blockchain.config.net;

import org.gn.blockchain.config.blockchain.*;
import org.gn.blockchain.config.net.BaseNetConfig;

public class GNMMainNetConfig extends BaseNetConfig {
	
    public GNMMainNetConfig() {
        add(0, new GNMPhase01Config());
        add(86500, new GNMPhase02Config());
        add(196000, new GNMPhase03Config());
        add(262800, new GNMPhase04Config());
    }
}
