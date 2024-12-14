package com.imdevil.playground

import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.imdevil.kapt.annotation.OnClick
import com.imdevil.playground.base.LogActivity
import com.imdevil.playground.base.setupButtonClick
import com.imdevil.playground.fragment.BottomNavigationActivity
import com.imdevil.playground.fragment.ContainerActivity
import com.imdevil.playground.image.ImageActivity
import com.imdevil.playground.media.MusicActivity
import com.imdevil.playground.multirecyclerview.MultiRecyclerViewActivity
import com.imdevil.playground.preference.MySettingsActivity
import com.imdevil.playground.seekbar.SeekBarActivity
import com.imdevil.playground.service.ServiceActivity
import com.imdevil.playground.service.aidl.ClientActivity
import com.imdevil.playground.view.TouchActivity
import com.imdevil.playground.view.multistate.MultiStateActivity
import com.imdevil.playground.view.scroll.ScrollMainDemoActivity
import com.imdevil.playground.view.track.TrackActivity
import com.imdevil.playground.viewpager.ViewPagerActivity
import com.imdevil.playground.viewpager2.VerticalVp2Activity
import com.imdevil.playground.viewpager2.ViewPager2Activity

class MainActivity : LogActivity() {

    init {
        Log.d(getLogTag(), ": init")
    }

    private lateinit var contentParent: ViewGroup
    private var mainView: View? = null
    private var backView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentParent = findViewById(android.R.id.content)

        if (mainView == null) {
            mainView =
                LayoutInflater.from(this)?.inflate(R.layout.activity_main, contentParent, false)
        }
        setContentView(mainView)

        setupButtonClick(R.id.fragment, ContainerActivity::class.java)
        setupButtonClick(R.id.service, ServiceActivity::class.java)
        setupButtonClick(R.id.aidl, ClientActivity::class.java)
        setupButtonClick(R.id.view, TouchActivity::class.java)
        setupButtonClick(R.id.image, ImageActivity::class.java)
        setupButtonClick(R.id.multi_item_rv, MultiRecyclerViewActivity::class.java)
        setupButtonClick(R.id.media_session, MusicActivity::class.java)
        setupButtonClick(R.id.multi_state_view, MultiStateActivity::class.java)
        setupButtonClick(R.id.view_pager2, ViewPager2Activity::class.java)
        setupButtonClick(R.id.view_pager, ViewPagerActivity::class.java)
        setupButtonClick(R.id.vertical_vp2, VerticalVp2Activity::class.java)
        setupButtonClick(R.id.navigation, BottomNavigationActivity::class.java)
        setupButtonClick(R.id.scroll, ScrollMainDemoActivity::class.java)
        setupButtonClick(R.id.track, TrackActivity::class.java)
        setupButtonClick(R.id.settings, MySettingsActivity::class.java)
        setupButtonClick(R.id.seekbar, SeekBarActivity::class.java)

        mainView?.findViewById<Button>(R.id.set_back_view)?.setOnClickListener {
            if (backView == null) {
                backView = LayoutInflater.from(this)
                    ?.inflate(R.layout.activity_main_back, contentParent, false)
                backView?.findViewById<Button>(R.id.set_main_view)?.setOnClickListener {
                    setContentView(mainView)
                }
            }
            setContentView(backView)
        }

        mainView?.findViewById<Button>(R.id.window)?.setOnClickListener {
            addTextViewWindow(baseContext)
        }
    }

    private fun addTextViewWindow(context: Context) {
        val text = TextView(context)
        text.setText(R.string.app_name)
        //关键点1
        val mWindowManager =
            context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val wmParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        //关键点2
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG
        wmParams.format = PixelFormat.RGBA_8888
        wmParams.width = 800
        wmParams.height = 800
        //关键点3
        mWindowManager.addView(text, wmParams)
    }

    @OnClick(R.id.fragment, ScrollMainDemoActivity::class)
    private fun testKapt() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mainView = null
        backView = null
    }
}