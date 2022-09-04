package com.imdevil.playground.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.imdevil.playground.R
import com.imdevil.playground.base.LogActivity

class ContainerActivity : LogActivity() {

    var n = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
    }

    fun fragment(view: View) {

        supportFragmentManager.commit {
            when (n) {
                0, 3 -> add(R.id.container, OneFragment.newInstance(), n.toString())
                1 -> add(R.id.container, TwoFragment.newInstance(), n.toString())
                2 -> supportFragmentManager.findFragmentByTag("0")?.let { remove(it) }
                4 -> replace(R.id.container, ThreeFragment.newInstance())
            }
            n++
            addToBackStack(n.toString())
        }
    }
}