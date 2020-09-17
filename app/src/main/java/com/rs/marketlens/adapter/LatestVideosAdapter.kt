package com.rs.marketlens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rs.marketlens.databinding.LatestvideosListviewBinding
import com.rs.marketlens.network.LatestVideos

class LatestVideosAdapter (private val onClickListener: OnLatestVideosClick ) :  ListAdapter<LatestVideos, RecyclerView.ViewHolder>(latestvideosListDiffCallbacks()) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as LatestVideosAdapter.ViewHolder).bind(getItem(position)!!,onClickListener,position)
        //     holder.bind(getItem(position)!!, onClickListener,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        return LatestVideosAdapter.ViewHolder.from(parent)
        // return  ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: LatestvideosListviewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: LatestVideos, clickListener: OnLatestVideosClick, position:Int) {
            binding.viewModel = item
            binding.index = position
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LatestvideosListviewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}

class OnLatestVideosClick(val clickListener: (marsProperty: LatestVideos, type:Int, index:Int) -> Unit) {
    fun onClick(marsProperty: LatestVideos, index:Int) = clickListener(marsProperty,1,index)
    fun onDesc(marsProperty: LatestVideos, index:Int) = clickListener(marsProperty,2,index)
}

class latestvideosListDiffCallbacks : DiffUtil.ItemCallback<LatestVideos>() {

    override fun areItemsTheSame(oldItem: LatestVideos, newItem: LatestVideos): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: LatestVideos, newItem: LatestVideos): Boolean {
        return oldItem == newItem
    }
}
