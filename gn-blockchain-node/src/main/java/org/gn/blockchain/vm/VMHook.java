package org.gn.blockchain.vm;

import org.gn.blockchain.vm.program.Program;

public interface VMHook {
    void startPlay(Program program);
    void step(Program program, OpCode opcode);
    void stopPlay(Program program);
}
