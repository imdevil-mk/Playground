package com.imdevil.playground.preference

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.preference.*
import androidx.recyclerview.widget.RecyclerView
import com.imdevil.playground.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateRecyclerView(
        inflater: LayoutInflater,
        parent: ViewGroup,
        savedInstanceState: Bundle?
    ): RecyclerView {
        val list = super.onCreateRecyclerView(inflater, parent, savedInstanceState)
        list.addItemDecoration(CategoryItemDecoration())
        return list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateAdapter(preferenceScreen: PreferenceScreen): RecyclerView.Adapter<*> {
        return CategoryPreferenceAdapter(preferenceScreen)
    }

    @SuppressLint("RestrictedApi")
    class CategoryPreferenceAdapter(preferenceScreen: PreferenceGroup) :
        PreferenceGroupAdapter(preferenceScreen) {

        override fun onBindViewHolder(holder: PreferenceViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val preference = getItem(position)
            if (preference is PreferenceCategory) return
            val parent = preference?.parent
            if (parent !is PreferenceCategory) return
            /*val background: Int = if (position == itemCount - 1) {
                val prev = getItem(position - 1)
                if (prev is PreferenceCategory)
                    R.drawable.bg_corner_radius_all
                else R.drawable.bg_corner_radius_bottom
            } else {
                val prev = getItem(position - 1)
                val next = getItem(position + 1)
                if (prev is PreferenceCategory && next is PreferenceCategory) {
                    R.drawable.bg_corner_radius_all
                } else if (prev is PreferenceCategory) {
                    R.drawable.bg_corner_radius_top
                } else if (next is PreferenceCategory) {
                    R.drawable.bg_corner_radius_bottom
                } else {
                    R.drawable.bg_corner_radius_none
                }
            }*/
            val prev = getItem(position - 1)
            val prevParent = prev?.parent

            val next = getItem(position + 1)
            val nextParent = next?.parent

            val background = if (prev != null && next == null) {
                if (parent == prevParent) {
                    R.drawable.bg_corner_radius_bottom
                } else {
                    R.drawable.bg_corner_radius_all
                }
            } else {
                if (prevParent == parent) {
                    if (parent == nextParent) {
                        R.drawable.bg_corner_radius_none
                    } else {
                        R.drawable.bg_corner_radius_bottom
                    }
                } else {
                    if (parent == nextParent) {
                        R.drawable.bg_corner_radius_top
                    } else {
                        R.drawable.bg_corner_radius_all
                    }
                }
            }

            holder.itemView.background =
                AppCompatResources.getDrawable(holder.itemView.context, background)
        }
    }

    @SuppressLint("RestrictedApi")
    class CategoryItemDecoration : RecyclerView.ItemDecoration() {

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val childCount = parent.childCount
            val width = parent.width

            val divider = AppCompatResources.getDrawable(
                parent.context,
                android.R.drawable.divider_horizontal_bright
            )
            for (childViewIndex in 0 until childCount) {
                val view = parent.getChildAt(childViewIndex)
                if (shouldDrawDividerBelow(view, parent)) {
                    val top: Int = (view.y + view.height).toInt()
                    divider?.setBounds(0, top, width, top + 2)
                    divider?.draw(c)
                }
            }
        }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            if (shouldDrawDividerBelow(view, parent)) {
                outRect.bottom = 2
            }
        }

        private fun shouldDrawDividerBelow(view: View, parent: RecyclerView): Boolean {
            val adapter: PreferenceGroupAdapter = parent.adapter as PreferenceGroupAdapter

            val position = parent.indexOfChild(view);

            val prev = adapter.getItem(position - 1)
            val current = adapter.getItem(position)
            val next = adapter.getItem(position + 1)

            return current?.parent is PreferenceCategory && next != null && current.parent == next.parent

        }
    }

}