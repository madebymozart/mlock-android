/*
 * Copyright (c) 2020 Mozart Alexander Louis
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

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.prodbymozat.mlock.exceptions.MLockException;
import com.prodbymozat.mlock.exceptions.MLockInitializedException;

/**
 * Entry point class into the MLock Library.
 */
public final class MLock {
    /**
     * {@link MLockInternal}.
     */
    static volatile MLockInternal internal = null;

    /**
     * Initializes the MLock Library.
     *
     * @param context  Application context. Use `getApplicationContext()` method.
     * @param listener {@link OnInitializeListener}. required to implement in case an exception is caught
     */
    public static void init(Context context, OnInitializeListener listener) {
        if (internal != null) {
            listener.onComplete(new MLockInitializedException());
            return;
        }

        // Assure MLock is using the Application context and not an activity one.
        final Context ctx = context instanceof Application ? context : context.getApplicationContext();

        // Initialize MLockInternal
        internal = new MLockInternal(MLockCipher.getInstance(),
                MLockKeyStore.getInstance(ctx), new MLockNative());

        // Complete Listener
        listener.onComplete(null);
    }

    public static void commit(String key, MLockData<?> data) {
        internal.commit(key, data);
    }

    public static void apply(String key, MLockData<?> data) {
        internal.apply(key, data);
    }

    @Nullable
    public static MLockData<?> get(String key) {
        return internal.get(key);
    }

    public static void retrieve(String key, MLockAsyncRetrieveListener<?> listener) {
        internal.retrieve(key, listener);
    }

    /**
     * Resets MLock to be reinitialized.
     */
    public static void reset() {
        internal = null;
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
