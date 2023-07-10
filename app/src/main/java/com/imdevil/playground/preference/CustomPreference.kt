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

    private lateinit var pb: View

    private var count = 0
    private var showLoading = false

    init {
        layoutResource = R.layout.preference_custom
        widgetLayoutResource = R.layout.preference_widget
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        Log.d(TAG, "onBindViewHolder: $key $showLoading")

        pb = holder.itemView.findViewById(R.id.progressBar)
        pb.visibility = if (showLoading) View.VISIBLE else View.GONE
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