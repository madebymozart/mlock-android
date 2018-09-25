package com.prodbymozat.mlock.secure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.logging.Level;

public class MLockAsymmetricCipher extends MLockCipher {
    // Class Constants
    private static final String TAG = MLockSymmetricCipher.class.getSimpleName();

    /**
     * Constructor.
     */
    MLockAsymmetricCipher() {
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
            final PrivateKey privateKey = (PrivateKey) key;
            final Cipher cipher = Cipher.getInstance(ASYMMETRIC_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            // Create full encrypted string.
            return Base64.encodeToString(cipher.doFinal(data.getBytes(UTF_8)), Base64.DEFAULT);
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

            // Initializing Cipher in Decrypt mode with IvParameterSpec.
            final PrivateKey privateKey = (PrivateKey) key;
            final Cipher cipher = Cipher.getInstance(ASYMMETRIC_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // Decode the encrypted string to a byte array.
            final byte[] decodedData = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));

            // Create a string from the decrypted data in bytes.
            return new String(decodedData, UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IOException | BadPaddingException | IllegalBlockSizeException e) {
            logger.log(Level.WARNING, "Unable to decrypt data due to exception: "
                    + e.getMessage());
        }

        return null;
    }
}
