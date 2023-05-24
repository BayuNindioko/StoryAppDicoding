package com.example.storyapp.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ItemLayoutStoryBinding
import com.example.storyapp.main.DetailActivity
import com.example.storyapp.model.ListStoryItem

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null){
            holder.bind(data)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,DetailActivity::class.java)
            intent.putExtra("id", data?.id)
            holder.itemView.context.startActivity(intent)
        }

    }

    inner class ViewHolder(private val binding: ItemLayoutStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {

            binding.tvItemName.text = story.name
            binding.tvItemDescription.text = story.description

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)


        }

    }
    companion object {
         val diffCallback = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}
