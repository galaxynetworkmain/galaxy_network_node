package org.gn.blockchain.vm.program.invoke;

import java.math.BigInteger;

import org.gn.blockchain.core.Block;
import org.gn.blockchain.core.Repository;
import org.gn.blockchain.core.Transaction;
import org.gn.blockchain.db.BlockStore;
import org.gn.blockchain.vm.DataWord;
import org.gn.blockchain.vm.program.Program;

public interface ProgramInvokeFactory {

    ProgramInvoke createProgramInvoke(Transaction tx, Block block,
                                      Repository repository, BlockStore blockStore);

    ProgramInvoke createProgramInvoke(Program program, DataWord toAddress, DataWord callerAddress,
                                             DataWord inValue, DataWord inGas,
                                             BigInteger balanceInt, byte[] dataIn,
                                             Repository repository, BlockStore blockStore,
                                            boolean staticCall, boolean byTestingSuite);


}
