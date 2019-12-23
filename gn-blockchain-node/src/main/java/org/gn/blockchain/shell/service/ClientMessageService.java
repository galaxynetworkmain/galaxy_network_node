package org.gn.blockchain.shell.service;

public interface ClientMessageService {
    void sendToTopic(String topic, Object dto);
}
