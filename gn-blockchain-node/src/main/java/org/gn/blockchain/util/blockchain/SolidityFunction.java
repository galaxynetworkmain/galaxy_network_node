package org.gn.blockchain.util.blockchain;

import org.gn.blockchain.core.CallTransaction;

public interface SolidityFunction {

    SolidityContract getContract();

    CallTransaction.Function getInterface();
}
