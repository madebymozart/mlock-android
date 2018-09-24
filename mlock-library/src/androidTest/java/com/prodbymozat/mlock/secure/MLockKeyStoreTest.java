package com.prodbymozat.mlock.secure;

import javax.crypto.SecretKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MLockKeyStoreTest {

    // Test Constants
    private final static String TEST_ALIAS = "Test Alias";

    /**
     * {@link MLockKeyStore}
     */
    private MLockKeyStore mLockKeyStore;

    @Before
    public void setUp() {
        mLockKeyStore = new MLockKeyStore();
    }

    @After
    public void tearDown() {
        mLockKeyStore.deleteAllKeys();
    }

    @Test
    public void generateKey_SecretKeyIsNotNull() {
        // Act
        final SecretKey secretKey = mLockKeyStore.generateKey(TEST_ALIAS);

        // Asset
        assertNotNull(secretKey);
    }

    @Test
    public void getKey_shouldReturnNullIfNoKeyHasBeenGenerated() {
        // Act
        final SecretKey symmetricKey = mLockKeyStore.getKey(TEST_ALIAS);

        // Asset
        assertNull(symmetricKey);
    }

    @Test
    public void getKey_shouldNotBeNullAfterKeyIsGeneratedAndGetKeyIsCalled() {
        // Arrange
        mLockKeyStore.generateKey(TEST_ALIAS);

        // Act
        final SecretKey generatedKey = mLockKeyStore.getKey(TEST_ALIAS);

        // Assert
        assertNotNull(generatedKey);
    }

    @Test
    public void getKey_generatedKeyPersistsUponNewInstance() {
        // Arrange
        mLockKeyStore.generateKey(TEST_ALIAS);
        final MLockKeyStore mLockKeyStore2 = new MLockKeyStore();

        // Act
        final SecretKey generatedKey = mLockKeyStore.getKey(TEST_ALIAS);
        final SecretKey generatedKey2 = mLockKeyStore2.getKey(TEST_ALIAS);

        // Assert
        assertEquals(generatedKey2, generatedKey);
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
        final MLockKeyStore mLockKeyStore2 =
                new MLockKeyStore();
        assertFalse(mLockKeyStore2.hasKey(TEST_ALIAS));
    }

    @Test
    public void deleteAllKeys_shouldDeleteAllKeys() {
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
    }
}
