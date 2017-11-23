package com.android.mozil.light

import android.content.Context
import android.opengl.GLES20.*
import com.android.mozil.openglutil.util.*
import java.nio.FloatBuffer

/**
 * Created by M on 21/11/2017.
 */
class Ball {
    private var mProgram = -1
    private var muMVPMatrixHandle = -1
    private var muMMatrixHandle = -1
    //radius
    private var muRHandle = -1
    private var mPositionHandle = -1
    private var mTextureUnitHandle = -1
    private var mTextureCoordHandle = -1
    //定点法向量
    private var maNormalHandle = -1
    private var maLightLocationHandle = -1
    private var maCameraHandle = -1

    private lateinit var mVertexShader: String
    private lateinit var mFragmentShader: String

    private lateinit var mVertexBuffer: FloatBuffer
    private lateinit var mTextureBuffer: FloatBuffer
    private lateinit var mNormalBuffer: FloatBuffer

    private var vCount = 0
    var xAngle = 0f
    var yAngle = 0f
    var zAngle = 0f
    private var mR = 0.6f
    private val UNIT_SIZE = 1f

    constructor(context: Context) {
        initVertexData()

        initShader(context)

        initTexture(context)
    }

    private fun initVertexData() {
        var alVertex = ArrayList<Float>()
        var textureArray = ArrayList<Float>()
        val angleSpan = 5
        for (vAngle in -90..89 step angleSpan) { //垂直方向
            for (hAngle in 0..360 step angleSpan) { //水平方向
                val temp_vAngle = vAngle.toDouble()
                val temp_hAngle = hAngle.toDouble()

                var x0 = (mR * UNIT_SIZE * Math.cos(Math.toRadians(temp_vAngle)) * Math.cos(Math.toRadians(temp_hAngle))).toFloat()
                var y0 = (mR * UNIT_SIZE * Math.cos(Math.toRadians(temp_vAngle)) * Math.sin(Math.toRadians(temp_hAngle))).toFloat()
                var z0 = (mR * UNIT_SIZE * Math.sin(Math.toRadians(temp_vAngle))).toFloat()

                var x1 = (mR * UNIT_SIZE * Math.cos(Math.toRadians(temp_vAngle)) * Math.cos(Math.toRadians(temp_hAngle + angleSpan))).toFloat()
                var y1 = (mR * UNIT_SIZE * Math.cos(Math.toRadians(temp_vAngle)) * Math.sin(Math.toRadians(temp_hAngle + angleSpan))).toFloat()
                var z1 = (mR * UNIT_SIZE * Math.sin(Math.toRadians(temp_vAngle))).toFloat()

                var x2 = (mR * UNIT_SIZE * Math.cos(Math.toRadians(temp_vAngle + angleSpan)) * Math.cos(Math.toRadians(temp_hAngle + angleSpan))).toFloat()
                var y2 = (mR * UNIT_SIZE * Math.cos(Math.toRadians(temp_vAngle + angleSpan)) * Math.sin(Math.toRadians(temp_hAngle + angleSpan))).toFloat()
                var z2 = (mR * UNIT_SIZE * Math.sin(Math.toRadians(temp_vAngle + angleSpan))).toFloat()

                var x3 = (mR * UNIT_SIZE * Math.cos(Math.toRadians(temp_vAngle + angleSpan)) * Math.cos(Math.toRadians(temp_hAngle))).toFloat()
                var y3 = (mR * UNIT_SIZE * Math.cos(Math.toRadians(temp_vAngle + angleSpan)) * Math.sin(Math.toRadians(temp_hAngle))).toFloat()
                var z3 = (mR * UNIT_SIZE * Math.sin(Math.toRadians(temp_vAngle + angleSpan))).toFloat()

                val s0 = hAngle / 360f
                val s1 = (hAngle + angleSpan) /360f
                val t0 = 1 - (vAngle + 90) / 180f
                val t1 = 1 - (vAngle + angleSpan + 90) / 180f

                alVertex.add(x1)
                alVertex.add(y1)
                alVertex.add(z1)
                alVertex.add(x3)
                alVertex.add(y3)
                alVertex.add(z3)
                alVertex.add(x0)
                alVertex.add(y0)
                alVertex.add(z0)

                //x1,y1,z1
                textureArray.add(s1)
                textureArray.add(t0)
                //x3,y3,z3
                textureArray.add(s0)
                textureArray.add(t1)
                //x0,y0,z0
                textureArray.add(s0)
                textureArray.add(t0)

                alVertex.add(x1)
                alVertex.add(y1)
                alVertex.add(z1)
                alVertex.add(x2)
                alVertex.add(y2)
                alVertex.add(z2)
                alVertex.add(x3)
                alVertex.add(y3)
                alVertex.add(z3)

                //x1,y1,z1
                textureArray.add(s1)
                textureArray.add(t0)
                //x2,y2,z2
                textureArray.add(s1)
                textureArray.add(t1)
                //x3,y3,z3
                textureArray.add(s0)
                textureArray.add(t1)

            }
        }

        vCount = alVertex.size / 3

        var vertices = FloatArray(alVertex.size)
        for (i in 0 until alVertex.size) {
            vertices[i] = alVertex[i]
        }
        mVertexBuffer = BufferUtil.floatToBuffer(vertices)
        mNormalBuffer = BufferUtil.floatToBuffer(vertices)

        vertices = FloatArray(textureArray.size)
        for (i in 0 until textureArray.size) {
            vertices[i] = textureArray[i]
        }
        mTextureBuffer = BufferUtil.floatToBuffer(vertices)
    }

    private fun initShader(context: Context) {
        mVertexShader = TextResourceReader.readTextFileFromResource(context,R.raw.vertex_ball_light)
        mFragmentShader = TextResourceReader.readTextFileFromResource(context,R.raw.frag_ball_light)

        mProgram = ShaderHelper.buildProgram(mVertexShader,mFragmentShader)

        mPositionHandle = glGetAttribLocation(mProgram,"aPosition")

        muMVPMatrixHandle = glGetUniformLocation(mProgram,"uMVPMatrix")
        muMMatrixHandle = glGetUniformLocation(mProgram,"uMMatrix")
        muRHandle = glGetUniformLocation(mProgram,"uR")
        maNormalHandle = glGetAttribLocation(mProgram,"aNormal")
        maLightLocationHandle = glGetUniformLocation(mProgram,"uLightLocation")
        maCameraHandle = glGetUniformLocation(mProgram,"uCamera")
    }

    private fun initTexture(context: Context) {
        mTextureUnitHandle = glGetUniformLocation(mProgram,"uTextureUnit")
        mTextureCoordHandle = glGetAttribLocation(mProgram,"aTextureCoordinates")

        val textureId = TextureUtil.loadTexture(context,R.drawable.earth1)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D,textureId)
        glUniform1i(mTextureUnitHandle,0)
    }

    fun drawSelf() {
        MatrixState.rotate(xAngle,1f,0f,0f)
        MatrixState.rotate(yAngle,0f,1f,0f)
        MatrixState.rotate(zAngle,0f,0f,1f)

        glUseProgram(mProgram)
        glUniformMatrix4fv(muMVPMatrixHandle,1,false,MatrixState.finalMatrix,0)

        glUniformMatrix4fv(muMMatrixHandle,1,false,MatrixState.mMatrix,0)

        glUniform1f(muRHandle,mR * UNIT_SIZE)

        glUniform3fv(maLightLocationHandle,1,MatrixState.lightPositionFB)

        glUniform3fv(maCameraHandle,1,MatrixState.cameraFB)

        glEnableVertexAttribArray(mPositionHandle)
        glEnableVertexAttribArray(maNormalHandle)
        glEnableVertexAttribArray(mTextureCoordHandle)
        glVertexAttribPointer(mPositionHandle, 3, GL_FLOAT,
                false, 3 * 4, mVertexBuffer)
        glVertexAttribPointer(maNormalHandle, 3, GL_FLOAT, false,
                3 * 4, mNormalBuffer)
        glVertexAttribPointer(mTextureCoordHandle,2, GL_FLOAT,false,0,mTextureBuffer)
        glDrawArrays(GL_TRIANGLES, 0, vCount)

        glDisableVertexAttribArray(mPositionHandle)
        glDisableVertexAttribArray(maNormalHandle)
        glDisableVertexAttribArray(mTextureCoordHandle)
    }

}