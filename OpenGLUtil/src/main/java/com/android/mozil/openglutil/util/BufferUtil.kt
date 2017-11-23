package com.android.mozil.openglutil.util

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

object BufferUtil {
    fun floatToBuffer (array: FloatArray) : FloatBuffer {
        var vertexBuffer: FloatBuffer
        //initialize vertex vertex byte buffer for shape coordinates
        var bb = ByteBuffer.allocateDirect(array.size *4)
        //use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder())
        //create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer()
        //add the coordinates to the FloatBuffer
        vertexBuffer.put(array)
        //set buffer to read the first coordinate
        vertexBuffer.position(0)
        return vertexBuffer
    }

    fun shortToBuffer (array: ShortArray) : ShortBuffer {
        var shortBuffer: ShortBuffer
        var bb = ByteBuffer.allocateDirect(array.size*2)
        bb.order(ByteOrder.nativeOrder())
        shortBuffer = bb.asShortBuffer()
        shortBuffer.put(array)
        shortBuffer.position(0)
        return shortBuffer
    }
}
