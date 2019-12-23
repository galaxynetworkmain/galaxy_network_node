package org.gn.blockchain.vm.program.invoke;

import org.gn.blockchain.core.Repository;
import org.gn.blockchain.db.BlockStore;
import org.gn.blockchain.vm.DataWord;

public interface ProgramInvoke {

    DataWord getOwnerAddress();

    DataWord getBalance();

    DataWord getOriginAddress();

    DataWord getCallerAddress();

    DataWord getMinGasPrice();

    DataWord getGas();

    long getGasLong();

    DataWord getCallValue();

    DataWord getDataSize();

    DataWord getDataValue(DataWord indexData);

    byte[] getDataCopy(DataWord offsetData, DataWord lengthData);

    DataWord getPrevHash();

    DataWord getCoinbase();

    DataWord getTimestamp();

    DataWord getNumber();

    DataWord getDifficulty();

    DataWord getGaslimit();

    boolean byTransaction();

    boolean byTestingSuite();

    int getCallDeep();

    Repository getRepository();

    BlockStore getBlockStore();

    boolean isStaticCall();
}
