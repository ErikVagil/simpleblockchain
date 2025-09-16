package com.erikvagil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	public static final String HASHING_ALGORITHM = "SHA-256";
	public static final String CHARACTER_SET = "UTF-8";

	private final String hash;

	public Hash(String hash) {
		this.hash = hash;
	}

	public static Hash from(String data) {
		try {
			// Create a hasher using the specified hashing algorithm
			MessageDigest digest = MessageDigest.getInstance(HASHING_ALGORITHM);

			// Hash the data string into an array of bytes
			byte[] hashedBytes = digest.digest(data.getBytes(CHARACTER_SET));

			// Build a hexadecimal string representation of the hashed data
			StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < hashedBytes.length; i++) {
				String singleHex = Integer.toHexString(0xff & hashedBytes[i]);
				if (singleHex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(singleHex);
			}
			return new Hash(hexString.toString());
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getHash() {
		return hash;
	}

	@Override
	public String toString() {
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Hash other) && hash.equals(other.hash);
	}

	@Override
	public int hashCode() {
		return hash.hashCode();
	}
}
