package com.android.mozil.light

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.Window
import android.view.WindowManager
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_light.*

class LightActivity : AppCompatActivity() {

    private lateinit var mBallView: BallSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_light)
        mBallView = BallSurfaceView(this)
        val lp = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT)
        lp.topToBottom = R.id.seekbar
        mBallView.layoutParams = lp
        main.addView(mBallView)
        seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBallView.lightOffset = (seekBar!!.max / 2.0f -progress) / (seekBar.max /2.0f)* (-4)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        mBallView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBallView.onPause()
    }
}
