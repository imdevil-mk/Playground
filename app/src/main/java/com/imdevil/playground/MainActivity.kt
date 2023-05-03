package com.imdevil.playground

import android.os.Bundle
import android.util.Log
import com.imdevil.playground.base.LogActivity
import com.imdevil.playground.base.setupButtonClick
import com.imdevil.playground.fragment.BottomNavigationActivity
import com.imdevil.playground.fragment.ContainerActivity
import com.imdevil.playground.image.ImageActivity
import com.imdevil.playground.media.MusicActivity
import com.imdevil.playground.multirecyclerview.MultiRecyclerViewActivity
import com.imdevil.playground.service.ServiceActivity
import com.imdevil.playground.service.aidl.ClientActivity
import com.imdevil.playground.view.TouchActivity
import com.imdevil.playground.view.multistate.MultiStateActivity
import com.imdevil.playground.view.scroll.ScrollMainDemoActivity
import com.imdevil.playground.view.track.TrackActivity
import com.imdevil.playground.viewpager2.ViewPager2Activity

class MainActivity : LogActivity() {

    init {
        Log.d(getLogTag(), ": init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupButtonClick(R.id.fragment, ContainerActivity::class.java)
        setupButtonClick(R.id.service, ServiceActivity::class.java)
        setupButtonClick(R.id.aidl, ClientActivity::class.java)
        setupButtonClick(R.id.view, TouchActivity::class.java)
        setupButtonClick(R.id.image, ImageActivity::class.java)
        setupButtonClick(R.id.multi_item_rv, MultiRecyclerViewActivity::class.java)
        setupButtonClick(R.id.media_session, MusicActivity::class.java)
        setupButtonClick(R.id.multi_state_view, MultiStateActivity::class.java)
        setupButtonClick(R.id.view_pager2, ViewPager2Activity::class.java)
        setupButtonClick(R.id.navigation, BottomNavigationActivity::class.java)
        setupButtonClick(R.id.scroll, ScrollMainDemoActivity::class.java)
        setupButtonClick(R.id.track, TrackActivity::class.java)
    }
}