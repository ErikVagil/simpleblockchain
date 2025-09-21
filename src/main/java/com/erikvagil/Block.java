package com.erikvagil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

public class Block {
	private final String data;
	private Hash hash;
	private Hash previousHash;
	private final long timestamp;
	private int nonce = 0;

	public Block(String data, Hash previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		timestamp = new Date().getTime();
		hash = initializeHash();
	}

	private Hash initializeHash() {
		return computeHash();
	}

	public Hash computeHash() {
		// Combine fields to be preserved to make the hash for this block:
		// Data stored in the block, the previous hash, the timestamp, and the nonce
		Hash currentHash = Hash.from(
			data +
			previousHash.getHash() +
			Long.toString(timestamp) +
			Integer.toString(nonce)
		);
		return currentHash;
	}

	public Duration mineBlock(int difficulty) {
		// Create a string of all 0's of size == difficulty
		String target = "0".repeat(difficulty);

		LocalDateTime startTime = LocalDateTime.now();

		// Recompute the hash with a different nonce until the first n characters are all 0's
		while (!hash.getHash().substring(0, difficulty).equals(target)) {
			nonce++;
			hash = computeHash();
		}

		LocalDateTime endTime = LocalDateTime.now();
		Duration timeToMine = Duration.between(startTime, endTime);

		return timeToMine;
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

	public int getNonce() {
		return nonce;
	}

    public void setHash(Hash hash) {
        this.hash = hash;
    }

    public void setPreviousHash(Hash previousHash) {
        this.previousHash = previousHash;
    }
}
