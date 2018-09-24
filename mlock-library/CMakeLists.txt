# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC or SHARED
add_library(
        # Sets the name of the library.
        mlock

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/mlock/mlock.hxx
        src/main/cpp/mlock/mlock.cxx
        src/main/cpp/mlock/msha2.hxx
        src/main/cpp/mlock/mqlite3.c
        src/main/cpp/mlock/mqlite3.h)

# Definitions
add_definitions(-DSQLITE_HAS_CODEC)

# Searches for a specified prebuilt library and stores the path as a variable.
find_library(log-lib log)

# Specifies libraries CMake should link to your target library.
target_link_libraries(mlock ${log-lib})