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

package com.prodbymozat.mlock;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.security.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.logging.Level;

final class MLockKeyStoreAsymmetric extends MLockKeyStore<KeyStore.PrivateKeyEntry> {

  // Class Constants
  private static final String TAG = MLockKeyStoreAsymmetric.class.getSimpleName();

  /**
   * {@link Context}. Should be application context.
   */
  private Context context;

  /**
   * Constructor.
   */
  MLockKeyStoreAsymmetric(@NonNull Context context) {
    super(TAG);
    this.context = context;
  }

  /**
   * @see MLockKeyStore#generateKey
   */
  @Override
  public KeyStore.PrivateKeyEntry generateKey(@NonNull String alias) {
    try {
      final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", ANDROID_KEY_STORE);

      // Set Calenders
      final Calendar start = new GregorianCalendar();
      final Calendar end = new GregorianCalendar();
      end.add(Calendar.ERA, 1);

      generator.initialize(new KeyPairGeneratorSpec.Builder(context)
          .setAlias(alias)
          .setSubject(new X500Principal("CN=" + alias))
          .setSerialNumber(BigInteger.valueOf(1000))
          .setStartDate(start.getTime())
          .setEndDate(end.getTime())
          .build());

      // Generating the keypair will also store it in the AndroidKeyStore, so we can retrieve a
      // KeyStore.PrivateKeyEntry afterwards.
      generator.genKeyPair();

      return getKey(alias);
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
      logger.log(Level.WARNING, "Could not generate symmetric key: " + e.getMessage());
      return null;
    }
  }

  /**
   * @see MLockKeyStore#getKey
   */
  @Nullable
  @Override
  public KeyStore.PrivateKeyEntry getKey(@NonNull String alias) {
    try {
      return (KeyStore.PrivateKeyEntry) Objects.requireNonNull(getKeyStore()).getEntry(alias, null);
    } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
        KeyStoreException e) {
      logger.log(Level.WARNING, "Could not get key " + alias + " from the KeyStore: " + e.getMessage());
      return null;
    }
  }
}
