package com.prodbymozat.mlock.secure;

import com.prodbymozat.mlock.keystore.MLockKeyStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;

import static org.junit.Assert.*;

public class MLockCipherTest {

    // Test Constants
    private final static String TEST_KEY_ALIAS = "Some-Test-Key";
    private static final String TEST_DATA_DECRYPTED = "Some Test Data";

    /**
     * {@link MLockKeyStore}
     */
    private MLockKeyStore mLockKeyStore;

    /**
     * {@link MLockCipher}
     */
    private MLockCipher mLockSymmetricCipher;

    @Before
    public void setUp() {
        mLockKeyStore = MLockKeyStore.getInstance();
        mLockSymmetricCipher = MLockCipher.getInstance();
    }

    @After
    public void tearDown() {
        if (mLockKeyStore.hasKey(TEST_KEY_ALIAS)) {
            mLockKeyStore.deleteKey(TEST_KEY_ALIAS);
        }
    }

    @Test
    public void encrypt_shouldEncryptPlainData() {
        // Arrange
        final Key key = mLockKeyStore.generateKey(TEST_KEY_ALIAS);

        // Act
        final String encrypted = mLockSymmetricCipher.encrypt(key, TEST_DATA_DECRYPTED);

        // Assert
        assertNotEquals(encrypted, null);
        assertNotEquals(encrypted, "");
        assertNotEquals(encrypted, TEST_DATA_DECRYPTED);
    }

    @Test
    public void decrypt_shouldDecryptEncryptedData() {
        // Arrange
        final Key key = mLockKeyStore.generateKey(TEST_KEY_ALIAS);

        // Act
        final String encrypted = mLockSymmetricCipher.encrypt(key, TEST_DATA_DECRYPTED);
        final String decrypted = mLockSymmetricCipher.decrypt(key, encrypted);

        // Assert
        assertEquals(decrypted, TEST_DATA_DECRYPTED);
    }

    @Test
    public void decrypt_shouldNotDecryptEncryptedDataWithDifferentKey() {
        // Arrange
        final Key key = mLockKeyStore.generateKey(TEST_KEY_ALIAS);
        final Key key2 = mLockKeyStore.generateKey(TEST_KEY_ALIAS + "1");

        // Act
        final String encrypted = mLockSymmetricCipher.encrypt(key, TEST_DATA_DECRYPTED);
        final String decrypted = mLockSymmetricCipher.decrypt(key2, encrypted);

        // Assert
        assertNull(decrypted);
    }
}
