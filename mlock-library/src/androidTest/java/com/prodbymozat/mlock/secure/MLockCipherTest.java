/*
  Copyright (c) 2018 Mozart Alexander Louis

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 */

package com.prodbymozat.mlock.secure;

import com.prodbymozat.mlock.keystore.MLockKeyStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
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
        mLockKeyStore = MLockKeyStore.getInstance(getInstrumentation().getContext());
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
