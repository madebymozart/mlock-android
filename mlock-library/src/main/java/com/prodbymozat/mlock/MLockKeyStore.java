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

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class MLockKeyStore<T> {

    // Class Constants
    static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    /**
     * Class Logger
     */
    protected Logger logger;

    /**
     * Constructor, initializes {@link MLockKeyStore##logger}. Made package-private to prevent outside
     * instantiation of this class.
     */
    MLockKeyStore(@NonNull String tag) {
        logger = Logger.getLogger(tag);
    }

    /**
     * Create a new instance of the {@link MLockKeyStore} based on the API version. If the Target platform is
     * less than API 23, it will create an instance of {@link MLockKeyStoreAsymmetric}, otherwise it will
     * create and instance of {@link MLockKeyStoreSymmetric}.
     *
     * @apiNote This is not a singleton, this will return a new object every time it is called.
     */
    public static MLockKeyStore getInstance(@NonNull Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? new MLockKeyStoreSymmetric()
                : new MLockKeyStoreAsymmetric(context);
    }

    /**
     * Generates a {@link T} and stores it in the AndroidKeyStore using the given alias.
     *
     * @return {@link Key} or null if any error occurs.
     */
    public abstract T generateKey(@NonNull String alias);

    /**
     * Gets a key from the {@link KeyStore} using the given alias
     *
     * @param alias Name of the key
     * @return {@link T from the {@link KeyStore}, otherwise null.
     */
    @Nullable
    public abstract T getKey(@NonNull String alias);

    /**
     * Checks the {@link KeyStore} for a key using the given alias.
     *
     * @param alias Alias of the key.
     * @return true if keystore has the key, otherwise false.
     */
    final boolean hasKey(@NonNull String alias) {
        try {
            return Objects.requireNonNull(getKeyStore()).isKeyEntry(alias);
        } catch (KeyStoreException e) {
            logger.log(Level.WARNING, "Unable to check if key is entry, by alias \"" + alias + "\" " +
                    "because of exception: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a key in the {@link KeyStore} using the given alias.
     *
     * @param alias Alias of the key.
     */
    final void deleteKey(@NonNull String alias) {
        try {
            final KeyStore keyStore = getKeyStore();
            if (keyStore == null || !hasKey(alias)) return;
            keyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            logger.log(Level.WARNING, "Unable to delete key by alias: \"" + alias + "\" because of " +
                    "exception: " + e.getMessage());
        }
    }

    /**
     * Deleted all keys in the {@link KeyStore} for this application.
     */
    final void deleteAllKeys() {
        try {
            // Loop through all aliases and delete each key.
            final KeyStore keyStore = getKeyStore();
            final Enumeration<String> aliases = Objects.requireNonNull(keyStore).aliases();
            while (aliases.hasMoreElements()) deleteKey(aliases.nextElement());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns and instance of the {@link KeyStore} The provider of this key store is
     * {@link MLockKeyStore##ANDROID_KEY_STORE}
     *
     * @return Instance of {@link KeyStore}
     */
    @Nullable
    final KeyStore getKeyStore() {
        try {
            final KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
            return keyStore;
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            logger.log(Level.WARNING, "Unable to obtain KeyStore: " + e.getMessage());
            return null;
        }
    }
}
