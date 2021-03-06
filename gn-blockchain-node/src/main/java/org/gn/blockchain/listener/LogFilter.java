package org.gn.blockchain.listener;

import static org.gn.blockchain.crypto.HashUtil.sha3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gn.blockchain.core.Bloom;
import org.gn.blockchain.vm.DataWord;
import org.gn.blockchain.vm.LogInfo;

public class LogFilter {

    private List<byte[][]> topics = new ArrayList<>();  //  [[addr1, addr2], null, [A, B], [C]]
    private byte[][] contractAddresses = new byte[0][];
    private Bloom[][] filterBlooms;

    public LogFilter withContractAddress(byte[] ... orAddress) {
        contractAddresses = orAddress;
        return this;
    }

    public LogFilter withTopic(byte[] ... orTopic) {
        topics.add(orTopic);
        return this;
    }

    private void initBlooms() {
        if (filterBlooms != null) return;

        List<byte[][]> addrAndTopics = new ArrayList<>(topics);
        addrAndTopics.add(contractAddresses);

        filterBlooms = new Bloom[addrAndTopics.size()][];
        for (int i = 0; i < addrAndTopics.size(); i++) {
            byte[][] orTopics = addrAndTopics.get(i);
            if (orTopics == null || orTopics.length == 0) {
                filterBlooms[i] = new Bloom[] {new Bloom()}; // always matches
            } else {
                filterBlooms[i] = new Bloom[orTopics.length];
                for (int j = 0; j < orTopics.length; j++) {
                    filterBlooms[i][j] = Bloom.create(sha3(orTopics[j]));
                }
            }
        }
    }

    public boolean matchBloom(Bloom blockBloom) {
        initBlooms();
        for (Bloom[] andBloom : filterBlooms) {
            boolean orMatches = false;
            for (Bloom orBloom : andBloom) {
                if (blockBloom.matches(orBloom)) {
                    orMatches = true;
                    break;
                }
            }
            if (!orMatches) return false;
        }
        return true;
    }

    public boolean matchesContractAddress(byte[] toAddr) {
        initBlooms();
        for (byte[] address : contractAddresses) {
            if (Arrays.equals(address, toAddr)) return true;
        }
        return contractAddresses.length == 0;
    }

    public boolean matchesExactly(LogInfo logInfo) {
        initBlooms();
        if (!matchesContractAddress(logInfo.getAddress())) return false;
        List<DataWord> logTopics = logInfo.getTopics();
        for (int i = 0; i < this.topics.size(); i++) {
            if (i >= logTopics.size()) return false;
            byte[][] orTopics = topics.get(i);
            if (orTopics != null && orTopics.length > 0) {
                boolean orMatches = false;
                DataWord logTopic = logTopics.get(i);
                for (byte[] orTopic : orTopics) {
                    if (new DataWord(orTopic).equals(logTopic)) {
                        orMatches = true;
                        break;
                    }
                }
                if (!orMatches) return false;
            }
        }
        return true;
    }
}
