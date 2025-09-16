package com.erikvagil;

import java.util.Date;

public class Block {
	private String data;
	private Hash hash;
	private Hash previousHash;
	private long timestamp;

	public Block(String data, Hash previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		timestamp = new Date().getTime();
		hash = computeHash();
	}

	private Hash computeHash() {
		// Combine fields to be preserved to make the hash for this block:
		// Data stored in the block, the previous hash, and the timestamp
		Hash currentHash = Hash.from(
			data +
			previousHash.getHash() +
			Long.toString(timestamp)
		);
		return currentHash;
	}

	public String getData() {
		return data;
	}

	public Hash getHash() {
		return hash;
	}

	public Hash getPreviousHash() {
		return previousHash;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
