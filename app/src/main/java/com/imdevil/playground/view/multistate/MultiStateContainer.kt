package com.imdevil.playground.view.multistate

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.util.containsKey
import androidx.core.util.forEach

class MultiStateContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val states = SparseArray<StateInfo>()


    fun addState(state: Int, layoutId: Int, init: View.() -> Unit) {
        states.put(state, StateInfo(layoutId, init, null))
    }

    fun moveToState(state: Int) {
        if (!states.containsKey(state)) {
            throw IllegalStateException("You have not set the state $state before!")
        }

        // init view if needed
        var (layoutId, init, view) = states[state]
        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutId, this, false)
            view.init()
            states[state] = states[state].copy(view = view)
        }

        // change state by set visible
        states.forEach { s, v ->
            setViewVisible(v.view, s == state)
        }
    }


    private fun setViewVisible(view: View?, visible: Boolean) {
        view?.let {
            view.visibility = if (visible) VISIBLE else GONE
            if (visible) {
                addView(view)
            } else {
                removeView(view)
            }
        }
    }
}


data class StateInfo(
    val layoutId: Int,
    val init: View.() -> Unit,
    var view: View?
)