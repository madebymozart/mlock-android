package com.prodbymozat.mlock.secure;

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
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

@TargetApi(Build.VERSION_CODES.M)
final class MLockSymmetricCipher extends MLockCipher {

    // Class Constants
    private static final String TAG = MLockSymmetricCipher.class.getSimpleName();

    /**
     * Constructor.
     */
    MLockSymmetricCipher() {
        super(TAG);
    }

    /**
     * @see MLockCipher#encrypt(Key, String)
     */
    @Override
    @Nullable
    final String encrypt(@NonNull Key key, @NonNull String data) {
        try {
            // Initialize Cipher in encryption mode with the secret key.
            final SecretKey secretKey = (SecretKey) key;
            final Cipher cipher = Cipher.getInstance(SYMMETRIC_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Create Base64 encoded IV
            final String iv = Base64.encodeToString(cipher.getIV(), Base64.DEFAULT);

            // Encrypt String and encode to Base64
            final String encryptedString =
                    Base64.encodeToString(cipher.doFinal(data.getBytes(UTF_8)), Base64.DEFAULT);

            // Create full encrypted string.
            return iv + IV_SEPARATOR + encryptedString;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IOException | BadPaddingException | IllegalBlockSizeException e) {
            logger.log(Level.WARNING, "Unable to encrypt data due to exception: " + e.getMessage());
        }

        return null;
    }

    /**
     * @see MLockCipher#decrypt(Key, String)
     */
    @Override
    @Nullable
    final String decrypt(@NonNull Key key, @NonNull String data) {
        try {
            // Splitting the iv from the encoded data.
            final String[] split = data.split(IV_SEPARATOR);
            final String ivString = split[0];
            final String encryptedString = split[1];

            // Initializing Cipher in Decrypt mode with IvParameterSpec.
            final SecretKey secretKey = (SecretKey) key;
            final Cipher cipher = Cipher.getInstance(SYMMETRIC_CIPHER);
            final GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(128, Base64.decode(ivString, Base64.DEFAULT));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

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
