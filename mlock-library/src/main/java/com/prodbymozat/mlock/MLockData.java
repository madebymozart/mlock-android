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

import java.util.Date;

/**
 * Base data model class for MLock, Containing the key, value and date the object was first created.
 * <p>
 * These are the supported data types for MLock:
 * <ul>
 * <li>Integer
 * <li>Float
 * <li>Double
 * <li>String
 * </ul>
 * </p>
 */
abstract class MLockData<V> {
    /**
     * Key for the key/pair value.
     */
    private MLockKey key;

    /**
     * Value for the key/pair value.
     */
    private V value;

    /**
     * Data of the data type creation.
     */
    private Date date;

    /**
     * Constructor.
     *
     * @param key   {@link MLockKey} Key associated with value
     * @param value Value to be saved. Must be one of the supported types listed in the Javadoc.
     */
    MLockData(MLockKey key, V value) {
        this.key = key;
        this.value = value;
        this.date = new Date();
    }

    public MLockKey getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }
}
