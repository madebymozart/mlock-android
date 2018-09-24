/**
 * mlock.cxx
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
 * Includes
 */
#include "mlock.hxx"

MLock::MLock() {}

MLock::~MLock() {}

void MLock::initDatabase() {}

void MLock::saveString(const std::string& key, const std::string& value) {}

void MLock::saveInteger(const std::string& key, const int value) {}

std::string MLock::getString(const std::string& key,
                             const std::string default_value) {
  return std::string();
}

int MLock::getInteger(const std::string& key, const int default_value) {
  return 0;
}

sqlite3* MLock::getDatabase() {
  // Use FileUtils to get the path of the database
  const auto& path =
      /* FileUtils::getInstance()->getWritablePath()  + */ __MLOCK_DB_NAME__;
  sqlite3* db;

  // Attempt to open this database on the device
  if (sqlite3_open(path, &db) == SQLITE_OK) {
    sqlite3_key(db, std::to_string(getDatabaseKey()).c_str(),
                int(strlen(std::to_string(getDatabaseKey()).c_str())));

    return db;
  }

  // This will only happen if the user has somehow tampered with the database.
  throw std::runtime_error("Error: Cannot open or create database...");
}

int MLock::getDatabaseKey() { return 0; }

int MLock::generateKey(const std::string& key) { return 0; }
