/*
  Copyright (c) 2018 Mozart Alexander Louis

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  <p>
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  <p>
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 */

package com.prodbymozat.mlock.keystore;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;
import java.util.Objects;
import java.util.logging.Level;

@TargetApi(Build.VERSION_CODES.M)
final class MLockKeyStoreSymmetric extends MLockKeyStore<SecretKey> {

    // Class Constants
    private static final String TAG = MLockKeyStoreSymmetric.class.getSimpleName();

    /**
     * Constructor.
     */
    MLockKeyStoreSymmetric() {
        super(TAG);
    }

    /**
     * @see MLockKeyStore#generateKey(String)
     */
    @Override
    public final SecretKey generateKey(@NonNull String alias) {
        try {
            final KeyGenerator keyGenerator =
                    KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

            keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());

            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            logger.log(Level.WARNING, "Could not generate symmetric key: " + e.getMessage());
            return null;
        }
    }

    /**
     * @see MLockKeyStore#getKey(String)
     */
    @Nullable
    @Override
    public SecretKey getKey(@NonNull String alias) {
        try {
            return (SecretKey) Objects.requireNonNull(getKeyStore()).getKey(alias, null);
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            logger.log(Level.WARNING, "Could not get key " + alias + " from the KeyStore: " + e.getMessage());
        }
        return null;
    }
}
