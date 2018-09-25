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

package com.prodbymozat.mlock.keystore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyStoreException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
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
        mLockKeyStore = MLockKeyStore.getInstance(getInstrumentation().getContext());
    }

    @After
    public void tearDown() {
        mLockKeyStore.deleteAllKeys();
    }

    @Test
    public void generateKey_SecretKeyIsNotNull() {
        // Act
        final Object key = mLockKeyStore.generateKey(TEST_ALIAS);

        // Asset
        assertNotNull(key);
    }

    @Test
    public void getKey_shouldReturnNullIfNoKeyHasBeenGenerated() {
        // Act
        final Object symmetricKey = mLockKeyStore.getKey(TEST_ALIAS);

        // Asset
        assertNull(symmetricKey);
    }

    @Test
    public void getKey_shouldNotBeNullAfterKeyIsGeneratedAndGetKeyIsCalled() {
        // Arrange
        mLockKeyStore.generateKey(TEST_ALIAS);

        // Act
        final Object generatedKey = mLockKeyStore.getKey(TEST_ALIAS);

        // Assert
        assertNotNull(generatedKey);
    }

    @Test
    public void getKey_generatedKeyPersistsUponNewInstance() {
        // Arrange, Using 2 different instances of the MLockKeyStore Class
        mLockKeyStore.generateKey(TEST_ALIAS);
        final MLockKeyStore mLockKeyStore2 = MLockKeyStore.getInstance(getInstrumentation().getContext());

        // Act
        final Object generatedKey = mLockKeyStore.getKey(TEST_ALIAS);
        final Object generatedKey2 = mLockKeyStore2.getKey(TEST_ALIAS);

        // Assert, using string representation since Asymmetric keys Objects can't be directly compared to each other
        assertNotSame(mLockKeyStore, mLockKeyStore2);
        assertEquals(generatedKey.toString(), generatedKey2.toString());
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
        assertFalse(MLockKeyStore.getInstance(getInstrumentation().getContext()).hasKey(TEST_ALIAS));
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
