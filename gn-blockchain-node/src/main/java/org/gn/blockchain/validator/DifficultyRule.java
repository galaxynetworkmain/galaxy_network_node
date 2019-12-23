package org.gn.blockchain.validator;

import static org.gn.blockchain.util.BIUtil.isEqual;

import java.math.BigInteger;

import org.gn.blockchain.config.SystemProperties;
import org.gn.blockchain.core.BlockHeader;

/**
 * Checks block's difficulty against calculated difficulty value
 */

public class DifficultyRule extends DependentBlockHeaderRule {

    private final SystemProperties config;

    public DifficultyRule(SystemProperties config) {
        this.config = config;
    }

    @Override
    public boolean validate(BlockHeader header, BlockHeader parent) {

        errors.clear();

        BigInteger calcDifficulty = header.calcDifficulty(config.getBlockchainConfig(), parent);
        BigInteger difficulty = header.getDifficultyBI();
        
        if (!isEqual(difficulty, calcDifficulty)) {

            errors.add(String.format("#%d: difficulty != calcDifficulty", header.getNumber()));
            return false;
        }

        return true;
    }
}
