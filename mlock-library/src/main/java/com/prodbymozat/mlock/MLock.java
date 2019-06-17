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

import android.app.Application;
import android.content.Context;
import androidx.annotation.Nullable;
import com.prodbymozat.mlock.data.MLockDataType;
import com.prodbymozat.mlock.exceptions.MLockException;
import com.prodbymozat.mlock.exceptions.MLockInitializedException;
import com.prodbymozat.mlock.exceptions.MLockInvalidContextException;

/**
 * Entry point class into the MLock Library.
 */
public final class MLock implements MLockInterface {
  /**
   * {@link MLockInternal}.
   */
  private static volatile MLockInternal internal = null;

  /**
   * Initializes the MLock Library.
   *
   * @param context  Application context. use `getApplicationContext()` method.
   * @param listener {@link OnInitializeListener}. required to implement in case an exception is caught
   */
  public static void init(Context context, OnInitializeListener listener) {
    final MLockException exception = check(context);
    if (exception != null) {
      listener.onComplete(new MLockInitializedException());
      return;
    }

    // Initialize MLockInternal
    internal = new MLockInternal(MLockCipher.getInstance(),
        MLockKeyStore.getInstance(context), new MLockNative());

    // Complete Listener
    listener.onComplete(null);
  }

  /**
   * A pre-initialization method check method before initialization of the MLock.
   */
  @Nullable
  private static MLockException check(Context context) {
    // assure MLock has not already been initialized.
    if (internal != null) return new MLockInitializedException();

    // Assure context is from the `Application` class
    if (!(context instanceof Application)) return new MLockInvalidContextException();

    // All check pass, return null
    return null;
  }

  /**
   * Methods from {@link MLockInternal} start.
   */
  @Override
  public void commit(String key, MLockDataType data) {
    internal.commit(key, data);
  }

  @Override
  public void apply(String key, MLockDataType data) {
    internal.apply(key, data);
  }

  @Override
  @Nullable
  public MLockDataType get(String key) {
    return internal.get(key);
  }

  @Override
  public void get(String key, MLockAsyncRetrieveListener listener) {
    internal.get(key, listener);
  }

  /**
   * Completion listener for {@link MLock#init(Context, OnInitializeListener)}.
   */
  public interface OnInitializeListener {
    /**
     * Called when {@link MLock#init(Context, OnInitializeListener)}. has completed
     *
     * @param exception An exception if an error has occurred, otherwise null.
     */
    void onComplete(@Nullable MLockException exception);
  }
}
