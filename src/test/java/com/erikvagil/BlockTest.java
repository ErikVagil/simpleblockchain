package com.erikvagil;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class BlockTest {

	@Test
	void blockStoresDataCorrectly() {
		Hash genesisHash = Hash.from("genesis");
		Block block = new Block("First block data", genesisHash);

		assertEquals("First block data", block.getData());
	}

	@Test
	void blockStoresPreviousHashCorrectly() {
		Hash genesisHash = Hash.from("genesis");
		Block block = new Block("Any data", genesisHash);

		assertEquals(genesisHash, block.getPreviousHash());
	}

	@Test
	void blockHashIsDeterministic() {
		Hash genesisHash = Hash.from("genesis");
		Block block = new Block("Some data", genesisHash);

		assertEquals(block.getHash(), Hash.from(block.getData() + 
												 block.getPreviousHash().getHash() + 
												 Long.toString(block.getTimestamp()) +
												 Integer.toString(block.getNonce())));
	}

	@Test
	void blockHashStoredEqualsBlockHashComputed() {
		Hash genesisHash = Hash.from("genesis");
		Block block = new Block("Some data", genesisHash);

		assertEquals(block.getHash(), block.computeHash());
	}

	@Test
	void blocksWithDifferentDataHaveDifferentHashes() {
		Hash genesisHash = Hash.from("genesis");
		Block block1 = new Block("Data A", genesisHash);
		Block block2 = new Block("Data B", genesisHash);

		assertNotEquals(block1.getHash(), block2.getHash());
	}

	@Test
	void blocksWithDifferentPreviousHashesHaveDifferentHashes() {
		Hash genesisHash1 = Hash.from("genesis1");
		Hash genesisHash2 = Hash.from("genesis2");

		Block block1 = new Block("Same data", genesisHash1);
		Block block2 = new Block("Same data", genesisHash2);

		assertNotEquals(block1.getHash(), block2.getHash());
	}

	@Test
	void chainOfBlocksHasConsistentLinks() {
		Hash genesisHash = Hash.from("genesis");
		Block block1 = new Block("Block 1", genesisHash);
		Block block2 = new Block("Block 2", block1.getHash());
		Block block3 = new Block("Block 3", block2.getHash());

		// Verify the linking
		assertEquals(block1.getHash(), block2.getPreviousHash());
		assertEquals(block2.getHash(), block3.getPreviousHash());
	}

	@Test
	void changingDataBreaksChainIntegrity() {
		Hash genesisHash = Hash.from("genesis");
		Block block1 = new Block("Block 1", genesisHash);
		Block block2 = new Block("Block 2", block1.getHash());

		// Simulate a malicious actor changing block1's data (hypothetically)
		Block tamperedBlock1 = new Block("Tampered data", genesisHash);

		// The previous hash reference in block2 no longer matches the new hash
		assertNotEquals(tamperedBlock1.getHash(), block2.getPreviousHash());
	}

	@Test
	void timestampIsSetOnCreation() {
		Hash genesisHash = Hash.from("genesis");
		Block block = new Block("Some data", genesisHash);

		long now = System.currentTimeMillis();

		// Ensure timestamp is within a reasonable range of 'now'
		assertTrue(block.getTimestamp() <= now && block.getTimestamp() > now - 2000, "Timestamp should be close to the current system time");
	}

	@Test
	void mineBlockProducesHashWithCorrectDifficulty() {
		Hash genesisHash = Hash.from("genesis");
		Block block = new Block("Block to mine", genesisHash);

		int difficulty = 4;
		Duration timeToMine = block.mineBlock(difficulty);
		System.out.println("Time to mine in mineBlockProducesHashWithCorrectDifficulty() test: " + timeToMine.toString().substring(2) + " seconds");
		Hash minedHash = block.getHash();

		// Verify that the mined hash starts with the required number of zeroes
		assertTrue(minedHash.getHash().startsWith("0".repeat(difficulty)), "Mined hash should start with " + difficulty + " zeroes");

		// Ensure the block's stored hash was updated to the mined hash
		assertEquals(minedHash, block.getHash());
	}

	@Test
	void mineBlockIncreasesNonce() {
		Hash genesisHash = Hash.from("genesis");
		Block block = new Block("Another block to mine", genesisHash);

		int startingNonce = 0;
		int difficulty = 4;

		Duration timeToMine = block.mineBlock(difficulty);
		System.out.println("Time to mine in mineBlockIncreasesNonce() test: " + timeToMine.toString().substring(2) + " seconds");

		// Nonce should be greater than initial value (0) since mining increments it
		assertTrue(block.getNonce() != startingNonce);
		assertTrue(block.getHash().getHash().startsWith("0".repeat(difficulty)));
		assertTrue(
			block.computeHash() != null && block.computeHash().getHash().startsWith("0".repeat(difficulty)), 
			"After mining, computeHash should still yield a hash with required difficulty"
		);
	}
}
