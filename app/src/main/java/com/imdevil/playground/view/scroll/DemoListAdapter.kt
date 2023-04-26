package com.imdevil.playground.view.scroll

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imdevil.playground.R
import com.imdevil.playground.databinding.DemoListItemBinding

class DemoListAdapter : ListAdapter<DemoData, DemoHolder>(DemoDataDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DemoHolder(
            DemoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DemoHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DemoData(
    val id: Int,
    val name: String,
    val icon: Int,
)

class DemoDataDiffCallBack : DiffUtil.ItemCallback<DemoData>() {
    override fun areItemsTheSame(oldItem: DemoData, newItem: DemoData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DemoData, newItem: DemoData): Boolean {
        return oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.icon == newItem.icon
    }
}

class DemoHolder(private val binding: DemoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: DemoData) {
        binding.name.text = data.name
        Glide.with(binding.root).load(data.icon).into(binding.icon)
    }
}

fun prepareDemoData(): List<DemoData> {
    val datas = mutableListOf<DemoData>()
    for (i in 0..100) {
        datas += DemoData(1, i.toString(), R.drawable.def_mdpi)
    }
    return datas
}