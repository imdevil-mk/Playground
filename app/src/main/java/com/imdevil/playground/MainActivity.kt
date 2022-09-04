package com.imdevil.playground

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.imdevil.playground.base.LogActivity
import com.imdevil.playground.fragment.ContainerActivity
import com.imdevil.playground.image.ImageActivity
import com.imdevil.playground.multirecyclerview.MultiRecyclerViewActivity
import com.imdevil.playground.service.ServiceActivity
import com.imdevil.playground.view.TouchActivity

class MainActivity : LogActivity() {

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

    fun goToView(view: View) {
        startActivity(Intent(this, TouchActivity::class.java))

    }

    fun goToImageLoad(view: View) {
        startActivity(Intent(this, ImageActivity::class.java))
    }

    fun goToMultiRecyclerViewActivity(view: View) {
        startActivity(Intent(this, MultiRecyclerViewActivity::class.java))

    }
}