package com.android.mozil.openglutil.shader

import android.content.Context
import android.opengl.GLES20.*

/**
 * Created by Administrator on 2015/8/11.
 */
class TextureShaderProgram(context: Context,vertexShaderResourceId: Int, fragmentShaderResourceId: Int) :
        ShaderProgram(context, vertexShaderResourceId, fragmentShaderResourceId) {
    //Uniform
    val textureMatrixLocation: Int = glGetUniformLocation(mProgram, U_MATRIX)
    val textureUnitLocation: Int = glGetUniformLocation(mProgram, U_TEXTURE_UNIT)
    //Attribute locations
    val positionAttributeLocation: Int = glGetAttribLocation(mProgram, A_POSITION)
//    val textureCoordinatesLocation: Int = glGetAttribLocation(mProgram, A_TEXTURE_COORDINATES)

    fun setUniforms(matrix: FloatArray, textureId: Int, targetTexture: Int) {
        glEnableVertexAttribArray(targetTexture)
        glUniformMatrix4fv(textureMatrixLocation, 1, false, matrix, 0)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(targetTexture, textureId)
        glUniform1i(textureUnitLocation, 0)
    }
}
