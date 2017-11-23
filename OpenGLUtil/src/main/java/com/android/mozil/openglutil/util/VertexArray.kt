/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 */
package com.android.mozil.openglutil.util

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glVertexAttribPointer

class VertexArray(vertexData: FloatArray) {
    private val floatBuffer: FloatBuffer = ByteBuffer
            .allocateDirect(vertexData.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)

    fun setVertexAttribPointer(dataOffset: Int, attributeLocation: Int,
                               componentCount: Int, stride: Int) {
        floatBuffer.position(dataOffset)
        glEnableVertexAttribArray(attributeLocation)
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT,
                false, stride, floatBuffer)
        floatBuffer.position(0)
    }
}
