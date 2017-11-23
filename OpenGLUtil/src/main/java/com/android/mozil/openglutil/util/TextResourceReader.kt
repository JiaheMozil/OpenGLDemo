package com.android.mozil.openglutil.util

import android.content.Context
import android.content.res.Resources
import android.support.annotation.RawRes
import android.util.Log

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.lang.StringBuilder

/**
 * Created by Administrator on 2015/8/9.
 */
object TextResourceReader {
    fun readTextFileFromResource(context: Context,@RawRes resourceId: Int): String {
        val body = StringBuilder()
        try {
            val inputStream = context.resources.openRawResource(resourceId)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader as Reader)
            var nextLine = bufferedReader.readLine()
            while (nextLine != null) {
                body.append(nextLine)
                body.append('\n')
                nextLine = bufferedReader.readLine()
            }
        } catch (e: IOException) {
            Log.v("IOException", e.cause.toString())
        } catch (nfe: Resources.NotFoundException) {
            Log.v("NotFoundException", nfe.cause.toString())
        }

        return body.toString()
    }

}
