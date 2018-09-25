package com.prodbymozat.mlock.keystore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;
import java.security.KeyStoreException;

import static org.junit.Assert.*;

public class MLockKeyStoreTest {

    // Test Constants
    private final static String TEST_ALIAS = "Test Alias";

    /**
     * {@link MLockKeyStore}
     */
    private MLockKeyStore mLockKeyStore;

    @Before
    public void setUp() {
        mLockKeyStore = MLockKeyStore.getInstance();
    }

    @After
    public void tearDown() {
        mLockKeyStore.deleteAllKeys();
    }

    @Test
    public void generateKey_SecretKeyIsNotNull() {
        // Act
        final Key key = mLockKeyStore.generateKey(TEST_ALIAS);

        // Asset
        assertNotNull(key);
    }

    @Test
    public void getKey_shouldReturnNullIfNoKeyHasBeenGenerated() {
        // Act
        final Key symmetricKey = mLockKeyStore.getKey(TEST_ALIAS);

        // Asset
        assertNull(symmetricKey);
    }

    @Test
    public void getKey_shouldNotBeNullAfterKeyIsGeneratedAndGetKeyIsCalled() {
        // Arrange
        mLockKeyStore.generateKey(TEST_ALIAS);

        // Act
        final Key generatedKey = mLockKeyStore.getKey(TEST_ALIAS);

        // Assert
        assertNotNull(generatedKey);
    }

    @Test
    public void getKey_generatedKeyPersistsUponNewInstance() {
        // Arrange, Using 2 different instances of the MLockKeyStore Class
        mLockKeyStore.generateKey(TEST_ALIAS);
        final MLockKeyStore mLockKeyStore2 = MLockKeyStore.getInstance();

        // Act
        final Key generatedKey = mLockKeyStore.getKey(TEST_ALIAS);
        final Key generatedKey2 = mLockKeyStore2.getKey(TEST_ALIAS);

        // Assert
        assertNotSame(mLockKeyStore, mLockKeyStore2);
        assertEquals(generatedKey, generatedKey2);
    }

    @Test
    public void hasKey_shouldBeTrueAfterCallingGenerateKey() {
        // Act
        mLockKeyStore.generateKey(TEST_ALIAS);

        // Assert
        assertTrue(mLockKeyStore.hasKey(TEST_ALIAS));
    }

    @Test
    public void hasKey_shouldBeFalseIfNoKeyWasGenerated() {
        // Assert
        assertFalse(mLockKeyStore.hasKey(TEST_ALIAS));
    }

    @Test
    public void deleteKey_shouldDeleteKey() {
        // Arrange
        mLockKeyStore.generateKey(TEST_ALIAS);

        // Act
        mLockKeyStore.deleteKey(TEST_ALIAS);

        // Assert
        assertFalse(mLockKeyStore.hasKey(TEST_ALIAS));
    }

    @Test
    public void deleteKey_shouldNotPersistUponNewInstance() {
        // Arrange
        mLockKeyStore.generateKey(TEST_ALIAS);

        // Act
        mLockKeyStore.deleteKey(TEST_ALIAS);

        // Assert
        // Assure it has truly been deleted by loading the keystore in a different instance.
        assertFalse(MLockKeyStore.getInstance().hasKey(TEST_ALIAS));
    }

    @Test
    public void deleteAllKeys_shouldDeleteAllKeys() throws KeyStoreException {
        // Arrange
        mLockKeyStore.generateKey(TEST_ALIAS);
        mLockKeyStore.generateKey(TEST_ALIAS + "1");
        mLockKeyStore.generateKey(TEST_ALIAS + "2");

        // Act
        mLockKeyStore.deleteAllKeys();

        // Assert
        assertFalse(mLockKeyStore.hasKey(TEST_ALIAS));
        assertFalse(mLockKeyStore.hasKey(TEST_ALIAS + "1"));
        assertFalse(mLockKeyStore.hasKey(TEST_ALIAS + "2"));
        assertFalse(mLockKeyStore.getKeyStore().aliases().hasMoreElements());
    }
}
