/**
 * mlock.hxx
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

#ifndef __MLOCK_HXX__
#define __MLOCK_HXX__

/**
 * Includes
 */
#include "mqlite3.h"
#include "msha2.hxx"

/**
 * Macros
 */
constexpr auto __MLOCK_DB_NAME__ = "mlock_db.mq3";

/**
 * This class manages, creates and execue sqlite3 tasks to store and retrieve
 * data in the database.
 */
class MLock {
 public:
  /**
   * Contructor
   */
  MLock();

  /**
   * Contructor
   */
  virtual ~MLock();

  /**
   * Initializes the database for the first time.
   */
  static void initDatabase();

  /**
   * Saves a string in the mqlite3 database
   *
   * @param key   Key to access a value in database.
   * @param value Value to store in database.
   */
  static void saveString(const std::string& key, const std::string& value);

  /**
   * Saves an integer in the mqlite3 database
   *
   * @param key   Key to access a value in database.
   * @param value Value to store in database.
   */
  static void saveInteger(const std::string& key, const int value);

  /**
   * Retrieves a string from the mqlite3 database.
   *
   * @param key           Key used to lookup data.
   * @param default_value What we should return if nothing is found.
   */
  static std::string getString(const std::string& key,
                               const std::string default_value = std::string());

  /**
   * Retrieves an integer from the mqlite3 database.
   *
   * @param key           Key used to lookup data.
   * @param default_value What we should return if nothing is found.
   */
  static int getInteger(const std::string& key, const int default_value = 0);

 private:
  /**
   * Gets and instance of the database. This will allow the database connections
   * be thread safe if multiple threads are attempting to access the database at
   * the same time.
   *
   * @returns SQLite3 database instance
   */
  static sqlite3* getDatabase();

  /**
   * Using the msha2 library and a developers backend to create
   *
   * @returns Generated database key
   */
  static int getDatabaseKey();

  /**
   *
   * @param key Key to be hashed
   *
   * @returns Integer hashed key.
   */
  static int generateKey(const std::string& key);
};

#endif  // __MLOCK_HXX__
