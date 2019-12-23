package org.gn.blockchain.trie;

import org.gn.blockchain.datasource.Source;

public interface Trie<V> extends Source<byte[], V> {

    byte[] getRootHash();

    void setRoot(byte[] root);

    /**
     * Recursively delete all nodes from root
     */
    void clear();
}
