package com.imdevil.playground.multirecyclerview

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Types
import java.lang.reflect.Type


private const val TAG = "IRecyclerData"

interface IRecyclerData {
    fun areItemsTheSame(oldItem: IRecyclerData): Boolean
    fun areContentsTheSame(oldItem: IRecyclerData): Boolean
}

data class FooData(
    val name: String,
    val age: Int,
) : IRecyclerData {
    override fun areItemsTheSame(oldItem: IRecyclerData) = when (oldItem) {
        is FooData -> oldItem == this
        else -> false
    }

    override fun areContentsTheSame(oldItem: IRecyclerData) = when (oldItem) {
        is FooData -> oldItem == this
        else -> false
    }
}


data class BarData(
    val name: String,
) : IRecyclerData {
    override fun areItemsTheSame(oldItem: IRecyclerData) = when (oldItem) {
        is BarData -> {
            Log.d(TAG, "areItemsTheSame: BarData ${oldItem == this}")
            oldItem == this
        }

        else -> {
            Log.d(TAG, "areContentsTheSame: BarData false")
            false
        }
    }

    override fun areContentsTheSame(oldItem: IRecyclerData) = when (oldItem) {
        is BarData -> {
            Log.d(TAG, "areItemsTheSame: BarData ${oldItem == this}")
            oldItem == this
        }

        else -> {
            Log.d(TAG, "areContentsTheSame: BarData false")
            false
        }
    }
}

data class ViewList<T : IRecyclerData>(
    val dataType: Type,
    val list: List<T>
) : IRecyclerData {
    override fun areItemsTheSame(oldItem: IRecyclerData) = when (oldItem) {
        is ViewList<*> -> oldItem == this
        else -> false
    }

    override fun areContentsTheSame(oldItem: IRecyclerData) = when (oldItem) {
        is ViewList<*> -> {
            var same = true
            if (oldItem.list.size != this.list.size) {
                same = false
            } else {
                for ((index, newItem) in list.withIndex()) {
                    val old = oldItem.list[index]
                    if (!newItem.areContentsTheSame(old)) {
                        same = false
                        break
                    }
                }
            }
            same
        }

        else -> false
    }

    fun getParameterType(): Type {
        return Types.newParameterizedType(ViewList::class.java, dataType)
    }
}


class IRecyclerDataDiffCallback : DiffUtil.ItemCallback<IRecyclerData>() {
    override fun areItemsTheSame(oldItem: IRecyclerData, newItem: IRecyclerData): Boolean {
        return newItem.areItemsTheSame(oldItem)
    }

    override fun areContentsTheSame(oldItem: IRecyclerData, newItem: IRecyclerData): Boolean {
        return newItem.areContentsTheSame(oldItem)
    }
}