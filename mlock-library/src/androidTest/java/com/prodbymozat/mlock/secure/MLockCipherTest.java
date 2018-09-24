package com.prodbymozat.mlock.secure;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
    private MLockCipher mLockCipher;

    @Before
    public void setUp() {
        mLockKeyStore = new MLockKeyStore();
        mLockCipher = new MLockCipher();
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
        final SecretKey key = mLockKeyStore.generateKey(TEST_KEY_ALIAS);

        // Act
        final String encryptedString = mLockCipher.encrypt(key, TEST_DATA_DECRYPTED);

        // Assert
        assertNotEquals(encryptedString, null);
        assertNotEquals(encryptedString, "");
        assertNotEquals(encryptedString, TEST_DATA_DECRYPTED);
    }

    @Test
    public void decrypt_shouldDecryptEncryptedData()
            throws BadPaddingException, IllegalBlockSizeException {
        // Arrange
        final SecretKey key = mLockKeyStore.generateKey(TEST_KEY_ALIAS);

        // Act
        final String encryptedString = mLockCipher.encrypt(key, TEST_DATA_DECRYPTED);
        final String decryptedString = mLockCipher.decrypt(key, encryptedString);

        // Assert
        assertEquals(decryptedString, TEST_DATA_DECRYPTED);
    }
}
