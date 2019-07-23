/*
 * Copyright (c) 2019 Mozart Alexander Louis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.prodbymozat.mlock;

import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

final class MLockCipherAsymmetric extends MLockCipher<KeyStore.PrivateKeyEntry> {

    // Class Constants
    private static final String TAG = MLockCipherAsymmetric.class.getSimpleName();

    /**
     * Constructor.
     */
    MLockCipherAsymmetric() {
        super(TAG);
    }

    /**
     * @see MLockCipher#encrypt
     */
    @Override
    @Nullable
    final String encrypt(@NonNull KeyStore.PrivateKeyEntry key, @NonNull String data) {
        try {
            // Initialize Cipher in encryption mode with the secret key.
            final Cipher cipher = Cipher.getInstance(ASYMMETRIC_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, key.getCertificate().getPublicKey());

            // Create full encrypted string.
            return Base64.encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            logger.log(Level.WARNING, "Unable to encrypt data due to exception: " + e.getMessage());
        }

        return null;
    }

    /**
     * @see MLockCipher#decrypt
     */
    @Override
    @Nullable
    final String decrypt(@NonNull KeyStore.PrivateKeyEntry key, @NonNull String data) {
        try {

            // Initializing Cipher in Decrypt mode with IvParameterSpec.
            final Cipher cipher = Cipher.getInstance(ASYMMETRIC_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, key.getPrivateKey());

            // Decode the encrypted string to a byte array.
            final byte[] decodedData = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));

            // Create a string from the decrypted data in bytes.
            return new String(decodedData, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            logger.log(Level.WARNING, "Unable to decrypt data due to exception: "
                    + e.getMessage());
        }

        return null;
    }
}
