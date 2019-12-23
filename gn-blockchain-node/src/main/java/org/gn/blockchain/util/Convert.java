package org.gn.blockchain.util;

import org.gn.blockchain.crypto.cryptohash.Shabal256;

public class Convert {

	public static Long addressToNumberic(byte[] addressBytes) {
		byte[] publicKeyHash = new Shabal256().digest(addressBytes);
		return Math.abs(ByteUtil.byteArrayToLong(publicKeyHash));
	}

}
