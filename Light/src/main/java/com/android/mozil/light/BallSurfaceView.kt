package com.android.mozil.light

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20.*
import com.android.mozil.openglutil.util.MatrixState

/**
 * Created by M on 21/11/2017.
 */
class BallSurfaceView(context: Context) : GLSurfaceView(context) {
    val TOUCH_SCALE_FACTOR = 180f/320f
    val mContext = context
    private var mRenderer: BallRenderer
    lateinit var mBall: Ball
    var lightOffset = -4f
    var mPreviousX = 0f
    var mPreviousY = 0f
    init {
        setEGLContextClientVersion(2)
        mRenderer = BallRenderer()
        setRenderer(mRenderer)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val y = event!!.y
        val x = event!!.x
        when (event.action) {
             MotionEvent.ACTION_MOVE -> {
                 val dx = x - mPreviousX
                 val dy = y - mPreviousY
                 mBall.xAngle += dx * TOUCH_SCALE_FACTOR
                 mBall.yAngle += dy * TOUCH_SCALE_FACTOR
             }
        }
        mPreviousX = x
        mPreviousY = y
        return true
    }

    inner private class BallRenderer : Renderer {
        override fun onDrawFrame(gl: GL10?) {
            glClear(GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)
            MatrixState.setLightLocation(lightOffset,0f,1.5f)
            MatrixState.pushMatrix()

            MatrixState.pushMatrix()
            MatrixState.translate(1f,0f,0f)
            mBall.drawSelf()
            MatrixState.popMatrix()

            MatrixState.pushMatrix()
            MatrixState.translate(-1f,0f,0f)
            mBall.drawSelf()
            MatrixState.popMatrix()

            MatrixState.popMatrix()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            glViewport(0,0,width,height)
            val ratio = width.toFloat()/height.toFloat()
            MatrixState.setProjectFrustum(-ratio,ratio,-1f,1f,15f,100f)
            MatrixState.setCamera(0f,0f,16f,0f,0f,0f,0f,1f,0f)

            MatrixState.setInitStack()
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            glClearColor(0f,0f,0f,0.3f)
            mBall = Ball(mContext)

            glEnable(GL_DEPTH_TEST)
            glEnable(GL_CULL_FACE)
        }

    }
}