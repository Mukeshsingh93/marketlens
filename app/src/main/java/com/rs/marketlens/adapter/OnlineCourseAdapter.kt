package com.rs.marketlens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rs.marketlens.databinding.OnlinecourseListviewBinding
import com.rs.marketlens.network.OurServiceVideos

class OnlineCourseAdapter (private val onClickListener: OnCoursesClick ) :  ListAdapter<OurServiceVideos, RecyclerView.ViewHolder>(CoursesListDiffCallbacks()) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as OnlineCourseAdapter.ViewHolder).bind(getItem(position)!!,onClickListener,position)
        //     holder.bind(getItem(position)!!, onClickListener,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        return OnlineCourseAdapter.ViewHolder.from(parent)
        // return  ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: OnlinecourseListviewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: OurServiceVideos, clickListener: OnCoursesClick, position:Int) {
            binding.viewModel = item
            binding.index = position
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OnlinecourseListviewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}

class OnCoursesClick(val clickListener: (marsProperty: OurServiceVideos, type:Int, index:Int) -> Unit) {
    fun onClick(marsProperty: OurServiceVideos, index:Int) = clickListener(marsProperty,1,index)
    fun onDesc(marsProperty: OurServiceVideos, index:Int) = clickListener(marsProperty,2,index)
}

class CoursesListDiffCallbacks : DiffUtil.ItemCallback<OurServiceVideos>() {

    override fun areItemsTheSame(oldItem: OurServiceVideos, newItem: OurServiceVideos): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: OurServiceVideos, newItem: OurServiceVideos): Boolean {
        return oldItem == newItem
    }
}