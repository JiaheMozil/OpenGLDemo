package com.android.mozil.openglutil.util

import android.opengl.Matrix

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

//存储系统矩阵状态的类
object MatrixState {
    //获取投影矩阵
    val projMatrix = FloatArray(16)//4x4矩阵 投影用
    //获取摄像机朝向的矩阵
    val caMatrix = FloatArray(16)//摄像机位置朝向9参数矩阵
    //获取具体物体的变换矩阵
    var mMatrix: FloatArray? = null
        private set//当前变换矩阵
    var lightLocation = floatArrayOf(0f, 0f, 0f)//定位光光源位置
    lateinit var cameraFB: FloatBuffer
    lateinit var lightPositionFB: FloatBuffer

    //保护变换矩阵的栈
    internal var mStack = Array(10) { FloatArray(16) }
    internal var stackTop = -1


    //设置摄像机
    internal var llbb = ByteBuffer.allocateDirect(3 * 4)
    internal var cameraLocation = FloatArray(3)//摄像机位置
    //获取具体物体的总变换矩阵
    internal var mMVPMatrix = FloatArray(16)
    val finalMatrix: FloatArray
        get() {
            Matrix.multiplyMM(mMVPMatrix, 0, caMatrix, 0, mMatrix, 0)
            Matrix.multiplyMM(mMVPMatrix, 0, projMatrix, 0, mMVPMatrix, 0)
            return mMVPMatrix
        }


    //设置灯光位置的方法
    internal var llbbL = ByteBuffer.allocateDirect(3 * 4)

    /**
     * 初始化初始矩阵
     */
    fun setInitStack()
    {
        mMatrix = FloatArray(16)
        Matrix.setRotateM(mMatrix, 0, 0f, 1f, 0f, 0f)
    }

    /**
     * 保护变换矩阵
     */
    fun pushMatrix()
    {
        stackTop++
        for (i in 0..15) {
            mStack[stackTop][i] = mMatrix!![i]
        }
    }

    /**
     * 恢复变换矩阵
     */
    fun popMatrix()
    {
        for (i in 0..15) {
            mMatrix!![i] = mStack[stackTop][i]
        }
        stackTop--
    }

    /**
     * 设置沿xyz轴移动
     * @param x
     * @param y
     * @param z
     */
    fun translate(x: Float, y: Float, z: Float)
    {
        Matrix.translateM(mMatrix, 0, x, y, z)
    }

    fun rotate(angle: Float, x: Float, y: Float, z: Float)//设置绕xyz轴移动
    {
        Matrix.rotateM(mMatrix, 0, angle, x, y, z)
    }

    fun scale(x: Float, y: Float, z: Float) {
        Matrix.scaleM(mMatrix, 0, x, y, z)
    }

    //插入自带矩阵
    fun matrix(self: FloatArray) {
        val result = FloatArray(16)
        Matrix.multiplyMM(result, 0, mMatrix, 0, self, 0)
        mMatrix = result
    }

    fun setCamera(
            cx: Float, //摄像机位置x
            cy: Float, //摄像机位置y
            cz: Float, //摄像机位置z
            tx: Float, //摄像机目标点x
            ty: Float, //摄像机目标点y
            tz: Float, //摄像机目标点z
            upx: Float, //摄像机UP向量X分量
            upy: Float, //摄像机UP向量Y分量
            upz: Float   //摄像机UP向量Z分量
    ) {
        Matrix.setLookAtM(
                caMatrix,
                0,
                cx,
                cy,
                cz,
                tx,
                ty,
                tz,
                upx,
                upy,
                upz
        )

        cameraLocation[0] = cx
        cameraLocation[1] = cy
        cameraLocation[2] = cz

        llbb.clear()
        llbb.order(ByteOrder.nativeOrder())//设置字节顺序
        cameraFB = llbb.asFloatBuffer()
        cameraFB.put(cameraLocation)
        cameraFB.position(0)
    }

    //设置透视投影参数
    fun setProjectFrustum(
            left: Float, //near面的left
            right: Float, //near面的right
            bottom: Float, //near面的bottom
            top: Float, //near面的top
            near: Float, //near面距离
            far: Float       //far面距离
    ) {
        Matrix.frustumM(projMatrix, 0, left, right, bottom, top, near, far)
    }

    fun perspectiveM(yFovInDegress: Float, aspect: Float, n: Float, f: Float) {
        MatrixHelper.perspectiveM(projMatrix, yFovInDegress, aspect, n, f)
    }

    //设置正交投影参数
    fun setProjectOrtho(
            left: Float, //near面的left
            right: Float, //near面的right
            bottom: Float, //near面的bottom
            top: Float, //near面的top
            near: Float, //near面距离
            far: Float       //far面距离
    ) {
        Matrix.orthoM(projMatrix, 0, left, right, bottom, top, near, far)
    }

    fun setLightLocation(x: Float, y: Float, z: Float) {
        llbbL.clear()

        lightLocation[0] = x
        lightLocation[1] = y
        lightLocation[2] = z

        llbbL.order(ByteOrder.nativeOrder())//设置字节顺序
        lightPositionFB = llbbL.asFloatBuffer()
        lightPositionFB.put(lightLocation)
        lightPositionFB.position(0)
    }
}
