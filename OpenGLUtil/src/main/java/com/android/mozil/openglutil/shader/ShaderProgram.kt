package com.android.mozil.openglutil.shader

import android.content.Context
import android.opengl.GLES20
import com.android.mozil.openglutil.util.ShaderHelper
import com.android.mozil.openglutil.util.TextResourceReader

/**
 * Created by Administrator on 2015/8/11.
 */
open class ShaderProgram(context: Context, vertexShaderResourceId: Int, fragmentShaderResourceId: Int) {

    //Shader program
    protected val program: Int

    fun useProgram() {
        GLES20.glUseProgram(program)
    }

    //Uniform constants
    protected val U_MATRIX = "u_Matrix"
    protected val U_TEXTURE_UNIT = "u_TextureUnit"
    protected val U_COLOR = "u_Color"

    //Attribute constants
    protected val A_POSITION = "a_Position"
    protected val A_COLOR = "a_Color"
    protected val A_TEXTURE_COORDINATES = "a_TextureCoordinates"


    init {
        program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId), TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId))
    }

}
