package com.imdevil.playground.multirecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imdevil.playground.databinding.BarItemBinding
import com.imdevil.playground.databinding.FooItemBinding
import com.imdevil.playground.databinding.ViewListFooItemBinding
import com.squareup.moshi.Types
import java.lang.reflect.Type

interface IItemViewBuilder<T : IRecyclerData> {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, data: IRecyclerData)
}

abstract class AbsItemViewBuilder<T : IRecyclerData>(
    val viewType: Int,
    val dataType: Type,
) : IItemViewBuilder<T>

class FooItemBuilder : AbsItemViewBuilder<FooData>(0, FooData::class.java) {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return FooItemViewHolder(
            FooItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, data: IRecyclerData) {
        (holder as FooItemViewHolder).bind(data as FooData)
    }

    class FooItemViewHolder(private val binding: FooItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FooData) {
            binding.name.text = data.name
            binding.age.text = data.age.toString()
        }
    }
}

class BarItemBuilder : AbsItemViewBuilder<BarData>(1, BarData::class.java) {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return BarItemViewHolder(
            BarItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, data: IRecyclerData) {
        (holder as BarItemViewHolder).bind(data as BarData)
    }

    class BarItemViewHolder(private val binding: BarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BarData) {
            binding.name.text = data.name
        }
    }
}

class ViewListItemBuilder : AbsItemViewBuilder<ViewList<FooData>>(
    2,
    Types.newParameterizedType(ViewList::class.java, FooData::class.java)
) {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewListFooHolder(
            ViewListFooItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, data: IRecyclerData) {
        (holder as ViewListFooHolder).bind(data as ViewList<FooData>)
    }

    class ViewListFooHolder(private val binding: ViewListFooItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ViewList<FooData>) {
            binding.name.text = data.list[0]!!.name
            binding.name1.text = data.list[1]!!.name
            binding.name2.text = data.list[2]!!.name
        }
    }
}