package com.imdevil.playground.multirecyclerview

import android.util.Log
import androidx.recyclerview.widget.DiffUtil

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


class IRecyclerDataDiffCallback : DiffUtil.ItemCallback<IRecyclerData>() {
    override fun areItemsTheSame(oldItem: IRecyclerData, newItem: IRecyclerData): Boolean {
        return newItem.areItemsTheSame(oldItem)
    }

    override fun areContentsTheSame(oldItem: IRecyclerData, newItem: IRecyclerData): Boolean {
        return newItem.areContentsTheSame(oldItem)
    }
}