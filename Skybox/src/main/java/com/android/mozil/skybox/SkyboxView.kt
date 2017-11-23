package com.android.mozil.skybox

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.android.mozil.openglutil.shader.TextureShaderProgram
import com.android.mozil.openglutil.util.MatrixState
import com.android.mozil.openglutil.util.TextureUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by M on 15/11/2017.
 */
class SkyboxView(context: Context) : GLSurfaceView(context) {
    private val mRenderer: Renderer

    private val TOUCH_SCALE_FACTOR = 180.0f / 320//角度缩放比例

    private var mPreviousY: Float = 0.toFloat()//上次的触控位置Y坐标
    private var mPreviousX: Float = 0.toFloat()//上次的触控位置X坐标

    //摄像机的位置角度
    var cx = 0f
    var cy = 2f
    var cz = 24f
    var cr = 24f//摄像机半径
    var cAngle = 0f

    init {
        setEGLContextClientVersion(2)
        mRenderer = SkyboxRenderer(context)
        setRenderer(mRenderer)
//        renderMode = RENDERMODE_WHEN_DIRTY
    }


    //触摸事件回调方法
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val y = e.y
        val x = e.x
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                val dy = y - mPreviousY//计算触控笔Y位移
                val dx = x - mPreviousX//计算触控笔X位移
                cAngle += dx * TOUCH_SCALE_FACTOR
                cx = (Math.sin(Math.toRadians(cAngle.toDouble())) * cr).toFloat()
                cz = (Math.cos(Math.toRadians(cAngle.toDouble())) * cr).toFloat()
                cy += dy / 10.0f
                requestRender()
            }
        }
        mPreviousY = y//记录触控笔位置
        mPreviousX = x//记录触控笔位置
        return true
    }


     inner class SkyboxRenderer(context: Context) : Renderer {
        val mContext: Context = context

        private lateinit var mSkybox: Skybox
        private var mTextureId = -1
        lateinit var mTextureShaderProgram: TextureShaderProgram
        var angle = 10f
        override fun onDrawFrame(gl: GL10?) {
            glClear(GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)
            MatrixState.setCamera(cx, cy, cz, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
            MatrixState.pushMatrix()
            MatrixState.rotate(angle,0f,1f,0f)
            mTextureShaderProgram.useProgram()
            mTextureShaderProgram.setUniforms(MatrixState.finalMatrix,mTextureId,GL_TEXTURE_CUBE_MAP)
            mSkybox.bindData(mTextureShaderProgram)
            mSkybox.draw()
            MatrixState.popMatrix()
            angle += 0.5f
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            glViewport(0, 0, width, height)
            val ratio = width.toFloat() / height.toFloat()
            // 调用此方法计算产生透视投影矩阵
//            MatrixState.setProjectFrustum(-1f,1f, -1f, 1f, 1f, 5f)
            MatrixState.perspectiveM(45f, ratio, 2f, 1000f)
//            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 1000f)

            // 调用此方法产生摄像机9参数位置矩阵
//            MatrixState.setCamera(0f, 0f, 6f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
//            glEnable(GL_DEPTH_TEST)
//            glEnable(GL_DEPTH_FUNC)
            glEnable(GL_CULL_FACE)
            glClearColor(0f,0f,0f,0.5f)
            MatrixState.setInitStack()
            mTextureShaderProgram = TextureShaderProgram(mContext,R.raw.skybox_vertex_shader,R.raw.skybox_fragment_shader)
            mSkybox = Skybox()
            /*mTextureId = TextureUtil.loadCubeMap(mContext,
                    intArrayOf(R.drawable.left, R.drawable.right,
                    R.drawable.bottom, R.drawable.top,
                    R.drawable.front, R.drawable.back))*/
            mTextureId = TextureUtil.loadCubeMap(mContext,
                    intArrayOf(R.drawable.skycubemap_right,R.drawable.skycubemap_left,
                            R.drawable.skycubemap_down, R.drawable.skycubemap_up,
                            R.drawable.skycubemap_front, R.drawable.skycubemap_back))
        }

    }


}