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

package com.prodbymozat.mlock.cipher;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

@TargetApi(Build.VERSION_CODES.M)
final class MLockCipherSymmetric extends MLockCipher<SecretKey> {

    // Class Constants
    private static final String TAG = MLockCipherSymmetric.class.getSimpleName();

    /**
     * Constructor.
     */
    MLockCipherSymmetric() {
        super(TAG);
    }

    /**
     * @see MLockCipher#encrypt(Object, String)
     */
    @Override
    @Nullable
    final String encrypt(@NonNull SecretKey key, @NonNull String data) {
        try {
            // Initialize Cipher in encryption mode with the secret key.
            final Cipher cipher = Cipher.getInstance(SYMMETRIC_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // Create Base64 encoded IV
            final String iv = Base64.encodeToString(cipher.getIV(), Base64.DEFAULT);

            // Encrypt String and encode to Base64
            final String encryptedString = Base64.encodeToString(cipher.doFinal(data.getBytes(UTF_8)), Base64.DEFAULT);

            // Create full encrypted string.
            return iv + IV_SEPARATOR + encryptedString;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IOException | BadPaddingException | IllegalBlockSizeException e) {
            logger.log(Level.WARNING, "Unable to encrypt data due to exception: " + e.getMessage());
        }

        return null;
    }

    /**
     * @see MLockCipher#decrypt(Object, String)
     */
    @Override
    @Nullable
    final String decrypt(@NonNull SecretKey key, @NonNull String data) {
        try {
            // Splitting the iv from the encoded data.
            final String[] split = data.split(IV_SEPARATOR);
            final String ivString = split[0];
            final String encryptedString = split[1];

            // Initializing Cipher in Decrypt mode with IvParameterSpec.
            final Cipher cipher = Cipher.getInstance(SYMMETRIC_CIPHER);
            final GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(GCM_SPEC_LENGTH, Base64.decode(ivString, Base64.DEFAULT));
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

            // Decode the encrypted string to a byte array.
            final byte[] decodedData =
                    cipher.doFinal(Base64.decode(encryptedString, Base64.DEFAULT));

            // Create a string from the decrypted data in bytes.
            return new String(decodedData, UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IOException | InvalidAlgorithmParameterException | BadPaddingException |
                IllegalBlockSizeException e) {
            logger.log(Level.WARNING, "Unable to decrypt data due to exception: "
                    + e.getMessage());
        }

        return null;
    }
}
