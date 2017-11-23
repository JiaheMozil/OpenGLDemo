package com.android.mozil.openglutil.shader

import android.content.Context
import android.opengl.GLES20
import com.android.mozil.openglutil.util.ShaderHelper
import com.android.mozil.openglutil.util.TextResourceReader

/**
 * Created by Administrator on 2015/8/11.
 */
open class ShaderProgram(context: Context, vertexShaderResourceId: Int, fragmentShaderResourceId: Int) {

    //Shader mProgram
    protected val mProgram: Int

    fun useProgram() {
        GLES20.glUseProgram(mProgram)
    }

    //Uniform constants
    protected val U_MATRIX = "uMatrix"
    protected val U_TEXTURE_UNIT = "uTextureUnit"
    protected val U_COLOR = "uColor"

    //Attribute constants
    protected val A_POSITION = "aPosition"
    protected val A_COLOR = "aColor"
    protected val A_TEXTURE_COORDINATES = "aTextureCoordinates"


    init {
        mProgram = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId), TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId))
    }

}
