# CMakeLists for libfacedetectcnn

project(facedetection)

cmake_minimum_required(VERSION 2.8)
option(ENABLE_OPENCV "use opencv" ON)
option(ENABLE_OPENMP "use openmp" ON)
option(ENABLE_INT8 "use int8" ON)
option(ENABLE_AVX2 "use avx2" OFF)
option(ENABLE_NEON "whether use neon, if use arm please set it on" ON)
add_definitions("-O3")

SET(fdt_base_dir   ${PROJECT_SOURCE_DIR})
SET(fdt_src_dir    ${fdt_base_dir})
SET(fdt_inc_dir    ${fdt_base_dir})

SET(fdt_lib_name   facedetection)
SET(fdt_lib_static ${fdt_lib_name})
SET(fdt_lib_shared ${fdt_lib_name}_shared)

FILE(GLOB_RECURSE fdt_source_files ${fdt_src_dir}/*.cpp)
LIST(SORT         fdt_source_files)

if(ENABLE_OPENMP)
    message("using openmp")
    add_definitions(-D_OPENMP)
endif()

if(ENABLE_INT8)
	message("using int8")
	add_definitions(-D_ENABLE_INT8)
endif()

if(ENABLE_AVX2)
	message("using avx2")
	add_definitions(-D_ENABLE_AVX2)
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -mavx2 -mfma")
endif()

if(ENABLE_NEON)
	message("using arm")
	add_definitions(-D_ENABLE_NEON)
endif()

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

INCLUDE_DIRECTORIES(${fdt_inc_dir})

# Create a static library (.a)
ADD_LIBRARY(${fdt_lib_static} STATIC ${fdt_source_files})

# Create a shared library (.so)
ADD_LIBRARY(${fdt_lib_shared} SHARED ${fdt_source_files})
SET_TARGET_PROPERTIES(${fdt_lib_shared} PROPERTIES OUTPUT_NAME "${fdt_lib_name}")
SET_TARGET_PROPERTIES(${fdt_lib_shared} PROPERTIES PREFIX "lib")

# Create demo. OpenCV is requred.
if (ENABLE_OPENCV)
    SET(OpenCV_INCLUDE_DIRS   ${PROJECT_SOURCE_DIR}/opencv2)
    SET(OpenCV_LIBS   ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})
    #    find_package(OpenCV REQUIRED)
    include_directories(${OpenCV_INCLUDE_DIRS})

    add_library(libopencv_java4 SHARED IMPORTED )
    set_target_properties(libopencv_java4 PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_java4.so")

    find_library( # Sets the name of the path variable.
            log-lib

            # Specifies the name of the NDK library that
            # you want CMake to locate.
            log)
    target_link_libraries(${fdt_lib_shared}
            ${fdt_lib_static}
            libopencv_java4     #opencv manager

            # REMOVED ALL SUB-LIBS! NOT NEEDED FOR FACE DETECTION! - simon
            ${log-lib}
            )
endif()
