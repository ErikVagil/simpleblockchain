package com.erikvagil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class HashTest {
	
	@Test
	void from_generatesCorrectSha256Hash() {
		// Known SHA-256 hash for "hello"
		String expected = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824";

		Hash hash = Hash.from("hello");

		assertEquals(expected, hash.getHash());
	}

	@Test
	void from_isDeterministic() {
		Hash hash1 = Hash.from("test-string");
		Hash hash2 = Hash.from("test-string");

		assertEquals(hash1, hash2, "Hashes for the same input should be equal");
		assertEquals(hash1.hashCode(), hash2.hashCode(), "Hash codes should be equal");
	}

	@Test
	void equals_returnsFalseForDifferentHashes() {
		Hash hash1 = Hash.from("abc");
		Hash hash2 = Hash.from("xyz");

		assertNotEquals(hash1, hash2);
	}

	@Test
	void equals_returnsFalseForDifferentType() {
		Hash hash = Hash.from("abc");
		assertNotEquals(hash, "some-string");
	}

	@Test
	void equals_isReflexiveAndSymmetric() {
		Hash hash1 = Hash.from("same");
		Hash hash2 = Hash.from("same");

		assertEquals(hash1, hash1, "Reflexive property failed");
		assertEquals(hash1, hash2, "Symmetric property failed");
		assertEquals(hash2, hash1, "Symmetric property failed (reverse)");
	}

	@Test
	void toString_returnsHashValue() {
		Hash hash = Hash.from("hello");
		assertEquals(hash.getHash(), hash.toString());
	}

	@Test
	void getHash_returnsUnderlyingHashValue() {
		String data = "my-data";
		Hash hash = Hash.from(data);

		assertNotNull(hash.getHash());
		assertFalse(hash.getHash().isEmpty());
	}

	@Test
	void hashCode_consistentWithEquals() {
		Hash h1 = Hash.from("abc");
		Hash h2 = Hash.from("abc");
		assertEquals(h1.hashCode(), h2.hashCode());
	}
}
