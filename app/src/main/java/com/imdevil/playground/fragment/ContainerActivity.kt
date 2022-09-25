package com.imdevil.playground.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.commit
import com.imdevil.playground.R
import com.imdevil.playground.base.LogActivity

class ContainerActivity : LogActivity() {

    var n = 0

    private val one = OneFragment.newInstance()
    private val two = TwoFragment.newInstance()
    private val three = ThreeFragment.newInstance()

    init {
        Log.d(getLogTag(), ": init $this")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.d(getLogTag(), "onConfigurationChanged: ")
        super.onConfigurationChanged(newConfig)
    }

    fun fragment(view: View) {

        supportFragmentManager.commit {
            when (n) {
                0 -> add(R.id.container, one, "One")
                1 -> add(R.id.container, two, "Two")
                2 -> supportFragmentManager.findFragmentByTag("One")?.let { remove(it) }
                3 -> add(R.id.container, one, "One")
                4 -> replace(R.id.container, three)
                5 -> {
                    add(R.id.container, one)
                    add(R.id.container, two)
                    hide(one)
                }
                6 -> {
                    detach(one)
                }
                7 -> {
                    attach(one)
                    show(one)
                }
            }
            n++
            //addToBackStack(n.toString())
        }
    }
}