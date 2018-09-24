/**
 * msha2_tests.cxx
 *
 * Copyright (c) 2018 Mozart Alexander Louis
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
 **/

/**
 *
 */
#include "../mlock/msha2.hxx"
#include "gtest/gtest.h"

TEST(MSHA2, ShouldProduceCorrectSHAHashes) {
  // #1
  EXPECT_EQ("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
            msha2::hash256_hex_string(std::string("")));

  // #2
  EXPECT_EQ("d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592",
            msha2::hash256_hex_string(
                std::string("The quick brown fox jumps over the lazy dog")));

  // #3
  EXPECT_EQ("ef537f25c895bfa782526529a9b63d97aa631564d5d789c2b765448c8635fb6c",
            msha2::hash256_hex_string(
                std::string("The quick brown fox jumps over the lazy dog.")));

  // #4
  EXPECT_EQ(
      "f08a78cbbaee082b052ae0708f32fa1e50c5c421aa772ba5dbb406a2ea6be342",
      msha2::hash256_hex_string(std::string(
          "For this sample, this 63-byte string will be used as input data")));

  // #5
  EXPECT_EQ("248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1",
            msha2::hash256_hex_string(std::string(
                "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq")));

  // #6
  EXPECT_EQ("46db250ef6d667908de17333c25343778f495d7a8010b9cfa2af97940772e8cd",
            msha2::hash256_hex_string(std::string(
                "This is exactly 64 bytes long, not counting the terminati")));

  // #7
  EXPECT_EQ("439f59ea52ada46873017cc59979fb6013729828b19154287290ca5b1aee25a1",
            msha2::hash256_hex_string(std::string("Mozart")));

  // #8
  EXPECT_EQ("4815a5e4a263c8dceb672c9312e180f5536f42f16f4fe00342481d9ed3d1fd92",
            msha2::hash256_hex_string(std::string("MozartL")));

  // #9
  EXPECT_EQ("75064b0a8c24e9d32ab11662d0176b01a036afdfa68464bdbcaeb5c2e35e277c",
            msha2::hash256_hex_string(std::string("LMozart")));
}
