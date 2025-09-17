package com.erikvagil;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleChainTest {

    private SimpleChain testChain;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        testChain = new SimpleChain();
    }

    @Test
    void addBlockShouldAddBlockToChain() {
        Block block = new Block("Block #1", Hash.from("genesis"));
        testChain.addBlock(block);

        assertEquals(block, testChain.getLastBlock(), "Last block should be the one that was just added.");
    }

    @Test
    void getLastBlockShouldReturnMostRecentBlock() {
        Block block1 = new Block("Block #1", Hash.from("genesis"));
        Block block2 = new Block("Block #2", block1.getHash());

        testChain.addBlock(block1);
        testChain.addBlock(block2);

        assertEquals(block2, testChain.getLastBlock(), "getLastBlock() should return the most recently added block.");
    }

    @Test
    void isValidChainShouldReturnTrueForSingleBlock() {
        Block block1 = new Block("Block #1", Hash.from("genesis"));
        testChain.addBlock(block1);

        assertTrue(testChain.isValidChain(), "A chain with only one block should be valid.");
    }

    @Test
    void isValidChainShouldReturnTrueForProperlyLinkedBlocks() {
        Block block1 = new Block("Block #1", Hash.from("genesis"));
        Block block2 = new Block("Block #2", block1.getHash());
        Block block3 = new Block("Block #3", block2.getHash());

        testChain.addBlock(block1);
        testChain.addBlock(block2);
        testChain.addBlock(block3);

        assertTrue(testChain.isValidChain(), "Properly linked blocks should result in a valid chain.");
    }

    @Test
    void isValidChainShouldReturnFalseIfBlockDataIsTampered() throws NoSuchFieldException, IllegalAccessException {
        Block block1 = new Block("Block #1", Hash.from("genesis"));
        testChain.addBlock(block1);

		// Tamper with block1's hash
		Field hashField = Block.class.getDeclaredField("hash");
		hashField.setAccessible(true);
		hashField.set(block1, Hash.from("fake hash"));

        assertFalse(testChain.isValidChain(), "Tampering with a block should make the chain invalid.");
    }

    @Test
    void isValidChainShouldReturnFalseIfPreviousHashLinkBroken() {
        Block block1 = new Block("Block #1", Hash.from("genesis"));
        testChain.addBlock(block1);

        // Add a block that doesn't link properly
        Block badBlock = new Block("Block #2", Hash.from("unrelated hash"));
        testChain.addBlock(badBlock);

        assertFalse(testChain.isValidChain(), "Chain with broken previous hash link should be invalid.");
    }

    @Test
    void isValidChainShouldHandleEmptyChainGracefully() {
        // No blocks added
        assertTrue(testChain.isValidChain(), "An empty chain should be considered valid.");
    }
}
