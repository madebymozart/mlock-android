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

import androidx.annotation.Nullable;

/**
 * Interface class defining the method to save and retrieve data from MLock
 */
interface MLockInterface {
  /**
   * Saves a value (Integer) in the MLock encrypted store synchronously.
   *
   * @param key  Key for the value.
   * @param data Data being saved. Must be one of the supported types listed in {@link MLockData}
   */
  void commit(String key, MLockData data);

  /**
   * Saves a value (Integer) in the MLock encrypted store asynchronously.
   *
   * @param key  Key for the value.
   * @param data Data being saved. Must be one of the supported types listed in {@link MLockData}
   */
  void apply(String key, MLockData data);

  /**
   * Retrieves a value in the MLock encrypted store synchronously.
   *
   * @param key Key for the value.
   */
  @Nullable
  MLockData get(String key);

  /**
   * Retrieves a value (String) in the MLock encrypted store asynchronously.
   *
   * @param key      Key for the value.
   * @param listener {@link MLockAsyncRetrieveListener}.
   */
  void retrieve(String key, MLockAsyncRetrieveListener listener);
}
