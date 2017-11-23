package com.android.mozil.skybox


import android.opengl.GLES20.*
import com.android.mozil.openglutil.shader.TextureShaderProgram
import com.android.mozil.openglutil.util.BufferUtil
import com.android.mozil.openglutil.util.VertexArray
import java.nio.ByteBuffer
import java.nio.ShortBuffer


/**
 * Created by M on 15/11/2017.
 */
class Skybox {
    private val POSITION_COMPONENT_COUNT = 3
    private val UNIT_SIZE = 28f
    private var vertexArray: VertexArray = VertexArray(floatArrayOf(
            -UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, // (0) Top-left near
            UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, // (1) Top-right near
            -UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE, // (2) Bottom-left near
            UNIT_SIZE, -UNIT_SIZE, UNIT_SIZE, // (3) Bottom-right near
            -UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE, // (4) Top-left far
            UNIT_SIZE, UNIT_SIZE, -UNIT_SIZE, // (5) Top-right far
            -UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE, // (6) Bottom-left far
            UNIT_SIZE, -UNIT_SIZE, -UNIT_SIZE      // (7) Bottom-right far
    ))
    private var indexBuffer: ShortBuffer = BufferUtil.shortToBuffer(
            shortArrayOf(
                    // Front
                    1, 3, 0, 0, 3, 2,

                    // Back
                    4, 6, 5, 5, 6, 7,

                    // Left
                    0, 2, 4, 4, 2, 6,

                    // Right
                    5, 7, 1, 1, 7, 3,

                    // Top
                    5, 1, 4, 4, 1, 0,

                    // Bottom
                    6, 2, 7, 7, 2, 3))

    fun bindData(textureShaderProgram: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(0,textureShaderProgram.positionAttributeLocation,
                POSITION_COMPONENT_COUNT,0)
    }

    fun draw() {
        glDrawElements(GL_TRIANGLES,36, GL_UNSIGNED_SHORT,indexBuffer)
    }

}