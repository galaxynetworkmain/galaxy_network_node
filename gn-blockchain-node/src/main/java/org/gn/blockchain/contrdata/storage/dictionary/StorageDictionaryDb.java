package org.gn.blockchain.contrdata.storage.dictionary;


import java.io.Closeable;
import java.io.Flushable;

import javax.annotation.PreDestroy;

import org.gn.blockchain.datasource.DbSource;
import org.gn.blockchain.datasource.Source;
import org.gn.blockchain.datasource.XorDataSource;
import org.gn.blockchain.util.ByteUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class StorageDictionaryDb implements Flushable, Closeable
{
    private DbSource<byte[]> db;
    
    public StorageDictionaryDb(@Qualifier("storageDict") DbSource<byte[]> dataSource) {
        this.db = dataSource;
    }
    
    @Override
    public void flush() {
        this.db.flush();
    }
    
    @PreDestroy
    @Override
    public void close() {
        this.db.flush();
    }
    
    public StorageDictionary getDictionaryFor(Layout.Lang lang, byte[] contractAddress) {
        byte[] key = ByteUtil.xorAlignRight(lang.getFingerprint(), contractAddress);
        XorDataSource<byte[]> dataSource = new XorDataSource<>(this.db, key);
        return new StorageDictionary((Source<byte[], byte[]>)dataSource);
    }
}
