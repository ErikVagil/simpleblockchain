package com.erikvagil;

import java.util.ArrayList;

public class SimpleChain {
	private Block genesis = new Block("This is the genesis block", Hash.from("simplegenesis"));
	private final ArrayList<Block> blockChain = new ArrayList<>();

	public SimpleChain() {
		blockChain.add(genesis);
	}

	public void addBlock(Block newBlock) {
		blockChain.add(newBlock);
	}

	public Block getLastBlock() {
		return blockChain.getLast();
	}

	public boolean isValidChain() {
		// Empty chains are valid
		if (blockChain.isEmpty()) {
			return true;
		}

		Block current;
		Block previous;
		for (int i = 0; i < blockChain.size(); i++) {
			current = blockChain.get(i);

			// Compare the hash stored to the hash it's supposed to be
			if (!current.getHash().equals(current.computeHash())) {
				return false;
			}

			// Only go on if there's a previous block to check
			if (i == 0) continue;
			
			previous = blockChain.get(i - 1);

			// Compare the previous hash stored to the previous block's hash
			if (!current.getPreviousHash().equals(previous.getHash())) {
				return false;
			}
		}

		return true;
	}

	public Block getGenesisBlock() {
		return genesis;
	}
}
