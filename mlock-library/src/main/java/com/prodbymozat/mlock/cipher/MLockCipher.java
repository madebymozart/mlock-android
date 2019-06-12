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

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.logging.Logger;

public abstract class MLockCipher<T> {

  // Class Constants
  static final int GCM_SPEC_LENGTH = 128;
  static final String UTF_8 = "UTF-8";
  static final String IV_SEPARATOR = "]";

  // Class Ciphers
  static final String SYMMETRIC_CIPHER = "AES/GCM/NoPadding";
  static final String ASYMMETRIC_CIPHER = "RSA/ECB/PKCS1Padding";

  /**
   * Class Logger
   */
  protected final Logger logger;

  /**
   * Constructor, initializes {@link MLockCipher##logger}. Made package-private to prevent outside
   * instantiation of this package.
   */
  MLockCipher(@NonNull String tag) {
    logger = Logger.getLogger(tag);
  }

  /**
   * Creates a new instance of the {@link MLockCipher} based on the API version. If the target platform is
   * less than API 23, it will create , otherwise it will create and instance of {@link MLockCipherSymmetric}
   *
   * @apiNote This is not a singleton, this will return a new object every time it is called.
   */
  public static MLockCipher getInstance() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? new MLockCipherSymmetric()
                                                          : new MLockCipherAsymmetric();
  }

  /**
   * Encrypts the given data using a {@link Key}.
   *
   * @return Encrypted data in Base64 String or null if any error occur. Data also contains iv key inside
   * so it will be returned in this format <iv key>]<encrypted data>
   */
  @Nullable
  abstract String encrypt(@NonNull T key, @NonNull String data);

  /**
   * Decrypts data that was encrypted by {@link MLockCipher#encrypt(T, String)}. Data should contain key
   * inside in this format <iv key>]<encrypted data>. This is critical as the {@link IvParameterSpec} will
   * need it to properly initialize the {@link Cipher} in {@link Cipher#DECRYPT_MODE}
   *
   * @param data Base64 Encrypted Data containing iv in this format <iv key>]<encrypted data>.
   * @return decrypted data or null if any error occur.
   */
  @Nullable
  abstract String decrypt(@NonNull T key, @NonNull String data);
}
