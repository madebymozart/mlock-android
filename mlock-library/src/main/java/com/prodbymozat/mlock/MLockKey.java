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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Base key class for MLock
 * <p>
 * These are the supported data types for keys are:
 * <ul>
 * <li>Integer
 * <li>String
 * </ul>
 * </p>
 */
public class MLockKey {
    // Class Constants
    private static final String ALGORITHM_SHA_256 = "SHA-256";

    /**
     * String key. This will be null if the Integer constructor is used.
     */
    private String key;

    /**
     * Constructor.
     *
     * @param strKey String key to be used with {@link MLockData}
     */
    public MLockKey(String strKey) {
        this.key = generateSHA256Key(strKey);
    }

    /**
     * Getter for {@link MLockKey##key}.
     *
     * @return {@link MLockKey##key}.
     */
    public String getKey() {
        return key;
    }

    /**
     * Generates a SHA-256.
     *
     * @param key String key to be used in the hash.
     * @return SHA-256 generated string.
     */
    private String generateSHA256Key(String key) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(ALGORITHM_SHA_256);
            final byte[] encodedHash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hexString = new StringBuilder();

            // Obtain the hex value of each byte.
            for (final byte b : encodedHash) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // Return SHA-256 String
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return key;
    }
}
