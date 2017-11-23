package com.android.mozil.openglutil.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.GLUtils
import android.opengl.GLUtils.texImage2D
import android.support.annotation.DrawableRes
import android.util.Log

/**
 * Created by M on 04/11/2017.
 */

object TextureUtil {
    val TAG = "TextureUtil"
    fun loadTexture(context: Context,resourceID: Int ) : Int{
        //save the id of texture object
        val textureObjectId = IntArray(1)
        //generate a Texture
        glGenTextures(1,textureObjectId,0)
        if (textureObjectId[0] == 0) {
            Log.e(TAG,"generate a texture object failed!")
            return 0
        }
        //load bitmap
        val option =  BitmapFactory.Options()
        option.inScaled = false
        val bitmap = BitmapFactory.decodeResource(context.resources,resourceID,option)
//        val bitmap = TextTextureUtil.createTimeOrDateBitmap(context,256,256,72,)
        if (bitmap == null) {
            Log.e(TAG,"Resource id " + resourceID + "decoded failed")
            glDeleteTextures(1,textureObjectId,0)
            return 0
        }

        //bind texture
        glBindTexture(GL_TEXTURE_2D,textureObjectId[0])
        //filter
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        //load texture
        GLUtils.texImage2D(GL_TEXTURE_2D,0,bitmap,0)
        bitmap.recycle()

        // 我们为纹理生成MIP贴图，提高渲染性能，但是可占用较多的内存
        glGenerateMipmap(GL_TEXTURE_2D)
        glBindTexture(GL_TEXTURE_2D,0)
        return textureObjectId[0]
    }

   fun loadCubeMap (contxt:Context,@DrawableRes resourceID: Int) : Int{
       val options = BitmapFactory.Options()
       options.inScaled = false
       val bitmaps = arrayListOf<Bitmap>()
       var bitmap = BitmapFactory.decodeResource(contxt.resources,resourceID,options)
       for (i in 0..5) {
           bitmaps.add(Bitmap.createBitmap(bitmap))
       }
//       if (bitmap == null) {
//           Log.e(TAG,"Resource id " + resourceID + "decoded failed")
//       }

       var textureId = IntArray(1)
       glGenTextures(1,textureId,0)
       val finalId = textureId[0]
       glBindTexture(GL_TEXTURE_CUBE_MAP,finalId)
       glTexParameteri(GL_TEXTURE_CUBE_MAP,
               GL_TEXTURE_MIN_FILTER, GL_LINEAR)
       glTexParameteri(GL_TEXTURE_CUBE_MAP,
               GL_TEXTURE_MAG_FILTER, GL_LINEAR)
//       glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
//       glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)



       for (i in 0..5) {
           GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, bitmaps[i], 0)
       }
       glBindTexture(GL_TEXTURE_2D,0)
       for (i in 0..5) {
           bitmaps[i].recycle()
       }
       return finalId
   }

    /**
     * Loads a cubemap texture from the provided resources and returns the
     * texture ID. Returns 0 if the load failed.
     *
     * @param context
     * @param cubeResources
     * An array of resources corresponding to the cube map. Should be
     * provided in this order: left, right, bottom, top, front, back.
     * @return
     */
    fun loadCubeMap(context: Context, cubeResources: IntArray): Int {
        val textureObjectIds = IntArray(1)
        glGenTextures(1, textureObjectIds, 0)

        if (textureObjectIds[0] == 0) {
            Log.w(TAG, "Could not generate a new OpenGL texture object.")
            return 0
        }
        val options = BitmapFactory.Options()
        options.inScaled = false
        val cubeBitmaps = arrayOfNulls<Bitmap>(6)
        for (i in 0..5) {
            cubeBitmaps[i] = BitmapFactory.decodeResource(context.resources,
                    cubeResources[i], options)

            if (cubeBitmaps[i] == null) {
                Log.w(TAG, "Resource ID " + cubeResources[i]
                            + " could not be decoded.")
                glDeleteTextures(1, textureObjectIds, 0)
                return 0
            }
        }
        // Linear filtering for minification and magnification
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[0])

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0)
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0)

        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0)
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0)

        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0)
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0)
        glBindTexture(GL_TEXTURE_2D, 0)

        for (bitmap in cubeBitmaps) {
            bitmap!!.recycle()
        }

        return textureObjectIds[0]
    }
}