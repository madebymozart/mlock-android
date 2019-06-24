/*
  Copyright (c) 2019 Mozart Alexander Louis

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

package com.prodbymozat.mlock;

import java.util.logging.Logger;

/**
 * Internal implementation of {@link MLockInterface
 */
public class MLockInternal implements MLockInterface {

  // Class Constants
  private static final String TAG = MLock.class.getSimpleName();

  /**
   * Class Logger
   */
  private final Logger logger;

  /**
   * {@link MLockCipher}
   */
  private final MLockCipher cipher;

  /**
   * {@link MLockKeyStore}
   */
  private final MLockKeyStore keyStore;

  /**
   * {@link MLockNative}
   */
  private MLockNative lockNative;

  /**
   * Constructor
   */
  MLockInternal(MLockCipher cipher, MLockKeyStore keyStore, MLockNative lockNative) {
    this.logger = Logger.getLogger(TAG);
    this.cipher = cipher;
    this.keyStore = keyStore;
    this.lockNative = lockNative;
  }

  /**
   * Methods from {@link MLockInterface}.
   */
  @Override
  public void commit(String key, MLockData data) {

  }

  @Override
  public void apply(String key, MLockData data) {

  }

  @Override
  public MLockData get(String key) {
    return null;
  }

  @Override
  public void retrieve(String key, MLockAsyncRetrieveListener listener) {

  }
}
