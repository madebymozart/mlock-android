package com.prodbymozat.mlock.cipher;

import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

class MLockCipherAsymmetric extends MLockCipher<KeyStore.PrivateKeyEntry> {

  // Class Constants
  private static final String TAG = MLockCipherAsymmetric.class.getSimpleName();

  /**
   * Constructor.
   */
  MLockCipherAsymmetric() {
    super(TAG);
  }

  /**
   * @see MLockCipher#encrypt(Object, String)
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
   * @see MLockCipher#decrypt(Object, String)
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
