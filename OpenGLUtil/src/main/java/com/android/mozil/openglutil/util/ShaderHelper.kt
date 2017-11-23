package com.android.mozil.openglutil.util

import android.opengl.GLES20
import android.util.Log

/**
 * Created by Administrator on 2015/8/9.
 */
object ShaderHelper {
    private val TAG = "ShaderHelper"

    private fun loadShader(shaderType: Int, source: String): Int {
        //创建一个新shader
        var shader = GLES20.glCreateShader(shaderType)
        //若创建成功则加载shader
        if (shader != 0) {
            //加载shader的源代码
            GLES20.glShaderSource(shader, source)
            //编译shader
            GLES20.glCompileShader(shader)
            //存放编译成功shader数量的数组
            val compiled = IntArray(1)
            //获取Shader的编译情况
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                //若编译失败则显示错误日志并删除此shader
                Log.e(TAG, "Could not compile shader $shaderType:")
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader))
                GLES20.glDeleteShader(shader)
                shader = 0
            }
        }

        return shader
    }

    fun createProgram(vertexSource: String, fragmentSource: String): Int {

        //加载顶点着色器
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)

        if (vertexShader == 0) {
            Log.e(TAG, "vertexShader == 0")
            return 0
        }

        //加载片元着色器
        val pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
        if (pixelShader == 0) {
            Log.e(TAG, "pixelShader == 0")
            return 0
        }

        //创建着色器程序

        var program = GLES20.glCreateProgram()

        //若程序创建成功则向程序中加入顶点着色器与片元着色器

        if (program != 0) {

            //向程序中加入顶点着色器

            GLES20.glAttachShader(program, vertexShader)

            //向程序中加入片元着色器

            GLES20.glAttachShader(program, pixelShader)

            //链接程序

            GLES20.glLinkProgram(program)

            //存放链接成功program数量的数组

            val linkStatus = IntArray(1)

            //获取program的链接情况

            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)

            //若链接失败则报错并删除程序

            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link mProgram: ")
                Log.e(TAG, GLES20.glGetProgramInfoLog(program))
                GLES20.glDeleteProgram(program)
                program = 0

            }

        }
        return program
    }

    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {

        //创建着色器程序
        var program = GLES20.glCreateProgram()
        //若程序创建成功则向程序中加入顶点着色器与片元着色器
        if (program != 0) {
            //向程序中加入顶点着色器
            GLES20.glAttachShader(program, vertexShaderId)
            //向程序中加入片元着色器
            GLES20.glAttachShader(program, fragmentShaderId)
            //链接程序
            GLES20.glLinkProgram(program)
            //存放链接成功program数量的数组
            val linkStatus = IntArray(1)
            //获取program的链接情况
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
            //若链接失败则报错并删除程序
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link mProgram: ")
                Log.e(TAG, GLES20.glGetProgramInfoLog(program))
                GLES20.glDeleteProgram(program)
                program = 0
            }
        }
        return program
    }

    fun buildProgram(vertexShaderSource: String, fragmentShaderSource: String): Int {
        val program: Int
        //加载顶点着色器
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource)

        if (vertexShader == 0) {
            Log.e(TAG, "vertexShader == 0")
            return 0
        }

        //加载片元着色器
        val pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource)
        if (pixelShader == 0) {
            Log.e(TAG, "pixelShader == 0")
            return 0
        }
        program = linkProgram(vertexShader, pixelShader)
        return program
    }
}
