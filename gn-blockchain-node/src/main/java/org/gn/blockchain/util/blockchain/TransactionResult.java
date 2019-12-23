package org.gn.blockchain.util.blockchain;

import org.gn.blockchain.core.TransactionExecutionSummary;
import org.gn.blockchain.core.TransactionReceipt;

public class TransactionResult {
    TransactionReceipt receipt;
    TransactionExecutionSummary executionSummary;

    public boolean isIncluded() {
        return receipt != null;
    }

    public TransactionReceipt getReceipt() {
        return receipt;
    }

    public TransactionExecutionSummary getExecutionSummary() {
        return executionSummary;
    }
}
