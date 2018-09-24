package com.prodbymozat.mlock.secure;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

class MLockKeyStore {

    // Class Constants
    private static final String TAG = MLockKeyStore.class.getSimpleName();
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";

    /**
     * Class Logger
     */
    private final Logger logger = Logger.getLogger(TAG);

    /**
     * The Keystore file instance
     */
    private KeyStore fileKeyStore;

    /**
     * Creates an {@link MLockKeyStore} instance..
     */
    MLockKeyStore() {
    }

    /**
     * Generates and saves a 256 AES {@link SecretKey} using provided alias and password.
     *
     * @return {@link SecretKey} or null if any error occurs
     */
    final SecretKey generateKey(@NonNull String alias) {
        try {
            final KeyGenerator keyGenerator =
                    KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER);

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
     * Checks the KeyStore for a key with the given alias
     *
     * @param alias Alias of the key.
     * @return true if keystore has the key, otherwise false.
     */
    final boolean hasKey(@NonNull String alias) {
        final KeyStore androidKeyStore = getKeyStore();
        return isKeyEntry(alias, androidKeyStore);
    }

    /**
     * Deletes the key with the given alias.
     *
     * @param alias Alias of the key.
     */
    final void deleteKey(@NonNull String alias) {
        try {
            final KeyStore keyStore = getKeyStore();
            if (keyStore == null || !isKeyEntry(alias, keyStore)) {
                return;
            }

            keyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            logger.log(Level.WARNING,
                    "Unable to delete key by alias: \"" + alias + "\" because of exception: "
                            + e.getMessage());
        }
    }

    /**
     * Deleted all aliases and associated keys
     */
    final void deleteAllKeys() {
        try {
            final KeyStore keyStore = getKeyStore();
            if (keyStore == null) {
                return;
            }

            // Loop through all aliases and delete each key.
            final Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                deleteKey(aliases.nextElement());
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a key from the Keystore
     *
     * @param alias Name of the key
     * @return {@link SecretKey}
     */
    final SecretKey getKey(@NonNull String alias) {
        try {
            final KeyStore keyStore = getKeyStore();
            return (SecretKey) (keyStore != null ? keyStore.getKey(alias,
                    generateKeyEntryPassword(alias)) : null);
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            logger.log(Level.WARNING,
                    "Could not get key " + alias + " from the KeyStore: " + e.getMessage());
        }
        return null;
    }

    /**
     * Loads the KeyStore. We cache this to save from multiple FileInputStream executions
     *
     * @return {@link KeyStore}
     */
    private KeyStore getKeyStore() {
        try {
            if (fileKeyStore == null) {
                fileKeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
                fileKeyStore.load(null);
            }

            return fileKeyStore;
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            logger.log(Level.WARNING,
                    "Unable to obtain KeyStore: " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if the KeyStore has the given alias.
     *
     * @param alias String were looking for in the KeyStore.
     * @param keyStore The KeyStore
     * @return true if it contains the alias, otherwise false.
     */
    private boolean isKeyEntry(@NonNull String alias, KeyStore keyStore) {
        try {
            return keyStore != null && keyStore.isKeyEntry(alias);
        } catch (KeyStoreException e) {
            logger.log(Level.WARNING,
                    "Unable to check if key is entry, by alias \""
                            + alias + "\" because of exception: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a password for a key entry using the given alias
     *
     * @param alias The alias.
     * @return Generated Password suing MD5
     */
    private char[] generateKeyEntryPassword(@NonNull String alias) {
        return new char[] { 'a', 'b', 'c' };
    }

    /**
     * Generates the password to the keystore.
     *
     * @return Generated MD5 String Password
     */
    private char[] generateKeyStorePassword() {
        return new char[] { 'a', 'b', 'c' };
    }
}
