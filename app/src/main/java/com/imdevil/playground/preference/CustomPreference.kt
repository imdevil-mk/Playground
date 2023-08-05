package com.imdevil.playground.preference

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.imdevil.playground.R

private const val TAG = "imdevil-CustomPreference"

class CustomPreference @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    private var count = 0
    private var showLoading = false

    init {
        layoutResource = R.layout.preference_custom
        widgetLayoutResource = R.layout.preference_widget
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        Log.d(TAG, "onBindViewHolder: $key $showLoading")

        val pb = holder.findViewById(R.id.progressBar)
        pb.visibility = if (showLoading) View.VISIBLE else View.INVISIBLE
        pb.bringToFront()

        val widget = holder.findViewById(android.R.id.widget_frame)
        widget.visibility = if (showLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun getTitle(): CharSequence? {
        return " $count"
    }

    override fun onClick() {
        super.onClick()
        count++
        Log.d(TAG, "onClick: $key")
        showLoading = !showLoading
        notifyChanged()
    }
}