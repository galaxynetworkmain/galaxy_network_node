package org.gn.blockchain.net.swarm;

/**
 * Self-explanatory interface
 *
 */
public interface ChunkStore {

    void put(Chunk chunk);

    Chunk get(Key key);
}
