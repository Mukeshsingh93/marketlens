package com.rs.marketlens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rs.marketlens.databinding.TopserviceListviewBinding
import com.rs.marketlens.network.OurService

class OurServiceAdapter (private val onClickListener: OnTopServiceClick ) :  ListAdapter<OurService, RecyclerView.ViewHolder>(completeListDiffCallbacks()) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as OurServiceAdapter.ViewHolder).bind(getItem(position)!!,onClickListener,position)
        //     holder.bind(getItem(position)!!, onClickListener,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        return OurServiceAdapter.ViewHolder.from(parent)
        // return  ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: TopserviceListviewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: OurService, clickListener: OnTopServiceClick, position:Int) {
            binding.viewModel = item
            binding.index = position
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TopserviceListviewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class OnTopServiceClick(val clickListener: (marsProperty: OurService, type:Int, index:Int) -> Unit) {
    fun onClick(marsProperty: OurService, index:Int) = clickListener(marsProperty,1,index)
    fun onDesc(marsProperty: OurService, index:Int) = clickListener(marsProperty,2,index)
}

class completeListDiffCallbacks : DiffUtil.ItemCallback<OurService>() {

    override fun areItemsTheSame(oldItem: OurService, newItem: OurService): Boolean {
        return oldItem.image == newItem.image
    }
    override fun areContentsTheSame(oldItem: OurService, newItem: OurService): Boolean {
        return oldItem == newItem
    }
}

