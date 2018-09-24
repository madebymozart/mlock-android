package com.prodbymozat.mlock.secure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

final class MLockCipher {

    // Class Constants
    private static final String TAG = MLockCipher.class.getSimpleName();
    private static final String UTF_8 = "UTF-8";
    private static final String IV_SEPARATOR = "]";
    private static final String CIPHER = "AES/GCM/NoPadding";

    /**
     * Class Logger
     */
    private final Logger logger = Logger.getLogger(TAG);

    /**
     * Encrypts the given data using a {@link SecretKey}.
     *
     * @return Encrypted data in Base64 String or null if any error occur. Data also contains iv
     * key inside so it will be returned in this format <iv key>]<encrypted data>
     */
    @Nullable
    final String encrypt(@NonNull Key key, @NonNull String data) {
        try {
            // Initialize Cipher in encryption mode with the secret key.
            final Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, key);

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
     * Decrypts data that was encrypted by {@link MLockCipher#encrypt(Key, String)}.
     * Data should contain key inside in this format <iv key>]<encrypted data>. This is critical
     * as the {@link IvParameterSpec} will need it to properly initialize the {@link Cipher} in
     * {@link Cipher#DECRYPT_MODE}
     *
     * @param data Base64 Encrypted Data containing iv in this format <iv key>]<encrypted data>.
     * @return decrypted data or null if any error occur.
     */
    @Nullable
    final String decrypt(@NonNull Key key, @NonNull String data)
            throws BadPaddingException, IllegalBlockSizeException {
        try {
            // Splitting the iv from the encoded data.
            final String[] split = data.split(IV_SEPARATOR);
            final String ivString = split[0];
            final String encryptedString = split[1];

            // Initializing Cipher in Decrypt mode with IvParameterSpec.
            final Cipher cipher = Cipher.getInstance(CIPHER);
            final GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(128, Base64.decode(ivString, Base64.DEFAULT));
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

            // Decode the encrypted string to a byte array.
            final byte[] decodedData =
                    cipher.doFinal(Base64.decode(encryptedString, Base64.DEFAULT));

            // Create a string from the decrypted data in bytes.
            return new String(decodedData, UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IOException | InvalidAlgorithmParameterException e) {
            logger.log(Level.WARNING, "Unable to decrypt data due to exception: "
                    + e.getMessage());
        }

        return null;
    }
}
