package com.android.mozil.openglutil.shader

import android.content.Context
import android.opengl.GLES20.*

/**
 * Created by Administrator on 2015/8/11.
 */
class ColorShaderProgram(context: Context,vertexShaderResourceId: Int, fragmentShaderResourceId: Int) :
        ShaderProgram(context, vertexShaderResourceId, fragmentShaderResourceId) {

    private val uMatrixLocation: Int
    //Attribute locations
    val positionAttributeLocation: Int

    init {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        positionAttributeLocation = glGetAttribLocation(program, A_POSITION)
    }

    fun setUniforms(matrix: FloatArray) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

}
