package com.abd.dev.album.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abd.dev.album.R
import com.abd.dev.album.databinding.AlbumItemBinding
import com.abd.dev.album.domain.model.Album


class AlbumListAdapter(
    private val onClick: (Album) -> Unit
) : ListAdapter<Album, AlbumListAdapter.ViewHolder>(AlbumDiffCallback) {


    inner class ViewHolder(private val binding: AlbumItemBinding, onClick: (Album) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private var currentItem: Album? = null

        init {
            itemView.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item: Album) {
            currentItem = item
            binding.title.text = item.title
            binding.category.text = String.format(itemView.context.getString(R.string.category), item.albumId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AlbumItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

object AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.id == newItem.id
    }
}

