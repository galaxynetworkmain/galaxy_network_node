package org.gn.blockchain.facade;

import java.util.List;
import java.util.Set;

import org.gn.blockchain.core.*;

public interface PendingState {

    /**
     * @return pending state repository
     */
    org.gn.blockchain.core.Repository getRepository();

    /**
     * @return list of pending transactions
     */
    List<Transaction> getPendingTransactions();
}
