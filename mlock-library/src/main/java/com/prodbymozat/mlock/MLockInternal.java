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

import android.content.Context;
import com.prodbymozat.mlock.cipher.MLockCipher;
import com.prodbymozat.mlock.keystore.MLockKeyStore;

import java.util.logging.Logger;

/**
 * Internal implementation of MLock. This class contains all the
 */
public class MLockInternal {

  // Class Constants
  private static final String TAG = MLock.class.getSimpleName();

  /**
   * Class Logger
   */
  private final Logger logger;

  /**
   * {@link MLockCipher}
   */
  private final MLockCipher mLockCipher;

  /**
   * {@link MLockKeyStore}
   */
  private final MLockKeyStore mLockKeyStore;

  /**
   * {@link MLockNative}
   */
  private MLockNative mLockNative;

  /**
   * Constructor
   */
  MLockInternal(Context context) {
    this.logger = Logger.getLogger(TAG);
    this.mLockCipher = MLockCipher.getInstance();
    this.mLockKeyStore = MLockKeyStore.getInstance(context);
    this.mLockNative = new MLockNative();
  }
}
