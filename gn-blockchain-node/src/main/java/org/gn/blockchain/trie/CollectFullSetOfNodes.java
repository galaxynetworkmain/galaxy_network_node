package org.gn.blockchain.trie;

import java.util.HashSet;
import java.util.Set;

import org.gn.blockchain.db.ByteArrayWrapper;
import org.gn.blockchain.util.Value;

public class CollectFullSetOfNodes implements TrieImpl.ScanAction {
    Set<ByteArrayWrapper> nodes = new HashSet<>();

    @Override
    public void doOnNode(byte[] hash, TrieImpl.Node node) {
        nodes.add(new ByteArrayWrapper(hash));
    }

    @Override
    public void doOnValue(byte[] nodeHash, TrieImpl.Node node, byte[] key, byte[] value) {}

    public Set<ByteArrayWrapper> getCollectedHashes() {
        return nodes;
    }
}
