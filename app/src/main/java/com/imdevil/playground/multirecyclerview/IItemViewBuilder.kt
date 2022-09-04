package com.imdevil.playground.multirecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imdevil.playground.databinding.BarItemBinding
import com.imdevil.playground.databinding.FooItemBinding

import kotlin.reflect.KClass

interface IItemViewBuilder<T : IRecyclerData> {
    var dataType: KClass<T>
    var viewType: Int

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, data: IRecyclerData)
}

abstract class AbstractItemViewBuilder<T : IRecyclerData>(
    type: KClass<T>,
    override var viewType: Int
) : IItemViewBuilder<T> {
    override var dataType: KClass<T> = type
}

class FooItemBuilder : AbstractItemViewBuilder<FooData>(FooData::class, 0) {
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

class BarItemBuilder : AbstractItemViewBuilder<BarData>(BarData::class, 1) {
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