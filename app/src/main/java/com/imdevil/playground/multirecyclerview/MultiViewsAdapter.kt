package com.imdevil.playground.multirecyclerview

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Type
import kotlin.reflect.typeOf

/*
 * https://rain9155.github.io/categories/recyclerView/ 关于RecyclerView的分析
 */

class MultiViewsAdapter :
    ListAdapter<IRecyclerData, RecyclerView.ViewHolder>(IRecyclerDataDiffCallback()) {

    private val itemBuildersViewTypeMap = mutableMapOf<Int, IItemViewBuilder<*>>()
    private val dataTypeToViewTypeMap = mutableMapOf<Type, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return itemBuildersViewTypeMap[viewType]!!.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return itemBuildersViewTypeMap[getItemViewType(position)]!!.onBindViewHolder(
            holder,
            getItem(position)
        )
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItem(position)
        val dataType: Type = data.javaClass

        val parameterizedType = if (data is ViewList<*>) {
            data.getParameterType()
        } else {
            dataType
        }
        return dataTypeToViewTypeMap[parameterizedType] ?: -1
    }

    fun <T : IRecyclerData> registerItemBuilder(
        itemBuilder: AbsItemViewBuilder<T>
    ) {
        Log.i(
            TAG,
            "registerItemBuilder: viewType = ${itemBuilder.viewType} dataType = ${itemBuilder.dataType}"
        )
        itemBuildersViewTypeMap[itemBuilder.viewType] = itemBuilder
        dataTypeToViewTypeMap[itemBuilder.dataType] = itemBuilder.viewType
    }

    fun <T : IRecyclerData> registerItemBuilder(
        viewType: Int,
        dataType: Type,
        itemBuilder: IItemViewBuilder<T>
    ) {
        Log.i(TAG, "registerItemBuilder: viewType = $viewType dataType = $dataType")
        itemBuildersViewTypeMap[viewType] = itemBuilder
        dataTypeToViewTypeMap[dataType] = viewType
    }

    companion object {
        private const val TAG = "MultiViewsAdapter"
    }
}

inline fun <reified T : IRecyclerData> MultiViewsAdapter.register(
    viewType: Int,
    itemBuilder: IItemViewBuilder<T>
) {
    registerItemBuilder(viewType, typeOf<T>()::class.java, itemBuilder)
}

inline fun <reified T : IRecyclerData> MultiViewsAdapter.register(
    viewType: Int,
    dataType: Type,
    itemBuilder: IItemViewBuilder<T>
) {
    registerItemBuilder(viewType, dataType, itemBuilder)
}