package com.imdevil.playground.multirecyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MultiViewsAdapter :
    ListAdapter<IRecyclerData, RecyclerView.ViewHolder>(IRecyclerDataDiffCallback()) {

    private val itemBuildersMap = mutableMapOf<Int, IItemViewBuilder<*>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return itemBuildersMap[viewType]!!.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //TODO two many loops
        return itemBuildersMap[getItemViewType(position)]!!.onBindViewHolder(
            holder,
            getItem(position)
        )
    }


    override fun getItemViewType(position: Int): Int {
        //TODO two many loops
        val data = getItem(position)
        for (value in itemBuildersMap.values) {
            if (value.dataType.isInstance(data)) {
                return value.viewType
            }
        }
        return -1
    }

    fun <T : IRecyclerData> registerItemBuilder(builder: AbstractItemViewBuilder<T>) {
        itemBuildersMap[builder.viewType] = builder
    }
}