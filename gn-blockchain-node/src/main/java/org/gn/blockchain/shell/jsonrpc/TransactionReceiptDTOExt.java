package org.gn.blockchain.shell.jsonrpc;

import static org.gn.blockchain.shell.jsonrpc.TypeConverter.toJsonHex;

import org.gn.blockchain.core.Block;
import org.gn.blockchain.core.TransactionInfo;

public class TransactionReceiptDTOExt extends TransactionReceiptDTO {

    public String returnData;
    public String error;

    public TransactionReceiptDTOExt(Block block, TransactionInfo txInfo) {
        super(block, txInfo);
        returnData = toJsonHex(txInfo.getReceipt().getExecutionResult());
        error = txInfo.getReceipt().getError();
    }
}
