#设置编译 native library 需要最小的 cmake 版本
cmake_minimum_required(VERSION 3.4.1)

#project(cmake)

#将指定的源文件编译为名为 libnative-lib.so 的动态库
add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             native-lib.cpp )

#查找本地 log 库
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

#将预构建的库添加到自己的原生库
target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )