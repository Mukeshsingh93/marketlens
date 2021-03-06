package com.rs.marketlens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rs.marketlens.databinding.FeaturedListviewBinding
import com.rs.marketlens.databinding.TopserviceListviewBinding
import com.rs.marketlens.network.FeaturedVideos

class FeaturedAdapter (private val onClickListener: OnFeaturedClick ) :  ListAdapter<FeaturedVideos, RecyclerView.ViewHolder>(FeaturedListDiffCallbacks()) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as FeaturedAdapter.ViewHolder).bind(getItem(position)!!,onClickListener,position)
        //     holder.bind(getItem(position)!!, onClickListener,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        return FeaturedAdapter.ViewHolder.from(parent)
        // return  ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: FeaturedListviewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: FeaturedVideos, clickListener: OnFeaturedClick, position:Int) {
            binding.viewModel = item
            binding.index = position
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FeaturedListviewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}

class OnFeaturedClick(val clickListener: (marsProperty: FeaturedVideos, type:Int, index:Int) -> Unit) {
    fun onClick(marsProperty: FeaturedVideos, index:Int) = clickListener(marsProperty,1,index)
    fun onDesc(marsProperty: FeaturedVideos, index:Int) = clickListener(marsProperty,2,index)
}

class FeaturedListDiffCallbacks : DiffUtil.ItemCallback<FeaturedVideos>() {

    override fun areItemsTheSame(oldItem: FeaturedVideos, newItem: FeaturedVideos): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: FeaturedVideos, newItem: FeaturedVideos): Boolean {
        return oldItem == newItem
    }
}
