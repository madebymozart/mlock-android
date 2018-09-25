package com.prodbymozat.mlock.keystore;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;

import javax.crypto.KeyGenerator;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;

@TargetApi(Build.VERSION_CODES.M)
final class MLockSymmetricKeyStore extends MLockKeyStore {

    // Class Constants
    private static final String TAG = MLockSymmetricKeyStore.class.getSimpleName();

    /**
     * Constructor.
     */
    MLockSymmetricKeyStore() {
        super(TAG);
    }

    /**
     * @see MLockKeyStore#generateKey(String)
     */
    @Override
    public final Key generateKey(@NonNull String alias) {
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
}
