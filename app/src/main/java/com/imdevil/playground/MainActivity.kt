package com.imdevil.playground

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.imdevil.kapt.annotation.OnClick
import com.imdevil.playground.base.LogActivity
import com.imdevil.playground.fragment.BottomNavigationActivity
import com.imdevil.playground.fragment.ContainerActivity
import com.imdevil.playground.image.ImageActivity
import com.imdevil.playground.media.MusicActivity
import com.imdevil.playground.multirecyclerview.MultiRecyclerViewActivity
import com.imdevil.playground.service.ServiceActivity
import com.imdevil.playground.service.aidl.ClientActivity
import com.imdevil.playground.view.TouchActivity
import com.imdevil.playground.view.multistate.MultiStateActivity
import com.imdevil.playground.view.scroll.ScrollListActivity
import com.imdevil.playground.viewpager2.ViewPager2Activity

class MainActivity : LogActivity() {

    init {
        Log.d(getLogTag(), ": init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToFragment(view: View) {
        startActivity(Intent(this, ContainerActivity::class.java))
    }

    fun goToService(view: View) {
        startActivity(Intent(this, ServiceActivity::class.java))
    }

    fun goToAIDL(view: View) {
        startActivity(Intent(this, ClientActivity::class.java))
    }

    fun goToView(view: View) {
        startActivity(Intent(this, TouchActivity::class.java))

    }

    fun goToImageLoad(view: View) {
        startActivity(Intent(this, ImageActivity::class.java))
    }

    fun goToMultiRecyclerViewActivity(view: View) {
        startActivity(Intent(this, MultiRecyclerViewActivity::class.java))
    }

    fun goToMusic(view: View) {
        startActivity(Intent(this, MusicActivity::class.java))
    }

    fun goToMultiStateActivity(view: View) {
        startActivity(Intent(this, MultiStateActivity::class.java))
    }

    fun goToViewPagerActivity(view: View) {
        startActivity(Intent(this, ViewPager2Activity::class.java))
    }

    fun goToBottomNavigationActivity(view: View) {
        startActivity(Intent(this, BottomNavigationActivity::class.java))
    }

    @OnClick(viewId = R.id.add, activityClz = ViewPager2Activity::class)
    fun test() {
        val clz = ViewPager2Activity::class
        Intent(this, clz.java)
    }

    fun goToScrollListActivity(view: View) {
        startActivity(Intent(this, ScrollListActivity::class.java))
    }
}