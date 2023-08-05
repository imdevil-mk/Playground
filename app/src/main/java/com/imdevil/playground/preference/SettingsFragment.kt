package com.imdevil.playground.preference

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.preference.*
import androidx.recyclerview.widget.RecyclerView
import com.imdevil.playground.R

@SuppressLint("LongLogTag")
class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var mRecyclerView: RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onCreateRecyclerView(
        inflater: LayoutInflater,
        parent: ViewGroup,
        savedInstanceState: Bundle?
    ): RecyclerView {
        mRecyclerView = super.onCreateRecyclerView(inflater, parent, savedInstanceState)
        return mRecyclerView
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        mRecyclerView.removeItemDecorationAt(0)
        mRecyclerView.addItemDecoration(CategoryItemDecoration())
        return root
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
            Log.d(TAG, "onBindViewHolder: ${preference?.key} $position")
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

            val pb: ProgressBar? = holder.findViewById(R.id.progressBar) as ProgressBar?
            pb?.let {
                Log.d(TAG, "onBindViewHolder: progressbar ${pb.visibility}")
            }

            holder.itemView.background =
                AppCompatResources.getDrawable(holder.itemView.context, background)
        }

        companion object {
            private const val TAG = "CategoryPreferenceAdapter"
        }
    }

    /**
     * see https://juejin.cn/post/6998924524548784141
     */
    @SuppressLint("RestrictedApi")
    class CategoryItemDecoration : RecyclerView.ItemDecoration() {
        /**
         * 绘制在ItemView之下，每次绘制只调用一次，不是每个ItemView调用一次
         */
        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
            Log.d(TAG, "onDraw: ")
        }

        /**
         * 绘制在ItemView之上，每次绘制只调用一次，不是每个ItemView调用一次
         * @param c 这是整个RecyclerView的Canvas
         */
        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            Log.d(TAG, "onDrawOver: ")
            val childCount = parent.childCount
            val width = parent.width

            val divider = AppCompatResources.getDrawable(
                parent.context,
                R.drawable.list_devider
            )
            /**
             * 千万注意，这里拿到的是屏幕上的ItemView个数，所以这里的index并不是Adapter中的Index。
             * childViewIndex只是它在屏幕上的index
             */
            for (childViewIndex in 0 until childCount) {
                val view = parent.getChildAt(childViewIndex)
                if (shouldDrawDividerBelow(view, parent)) {
                    val top: Int = (view.y + view.height).toInt()
                    divider?.setBounds(0, top, width, top + 10)
                    divider?.draw(c)
                }
            }

            val niubi = AppCompatResources.getDrawable(
                parent.context,
                R.drawable.niubi
            )!!
            niubi.setBounds(600, 600, 1200, 1200)
            niubi.draw(c)
        }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            Log.d(TAG, "getItemOffsets: ")
            if (shouldDrawDividerBelow(view, parent)) {
                outRect.bottom = 10
            }
        }

        private fun shouldDrawDividerBelow(view: View, parent: RecyclerView): Boolean {
            val adapter: PreferenceGroupAdapter = parent.adapter as PreferenceGroupAdapter

            /**
             * 根据当前的ItemView，获取这个item在整个布局列表中的位置，也就是Adapter中的index
             */
            val layoutParams: RecyclerView.LayoutParams =
                view.layoutParams as RecyclerView.LayoutParams
            val position = layoutParams.viewLayoutPosition

            val prev = adapter.getItem(position - 1)
            val current = adapter.getItem(position)
            val next = adapter.getItem(position + 1)

            val should =
                current?.parent is PreferenceCategory && next != null && current.parent == next.parent

            Log.d(
                TAG, "shouldDrawDividerBelow: " + String.format(
                    "%1$-32s %2$-32s %3$-32s %4b",
                    "${prev?.parent?.key}--${prev?.key}",
                    "${current?.parent?.key}--${current?.key}",
                    "${next?.parent?.key}--${next?.key}",
                    should
                )
            )

            return should
        }

        companion object {
            private const val TAG = "CategoryItemDecoration"
        }
    }
}