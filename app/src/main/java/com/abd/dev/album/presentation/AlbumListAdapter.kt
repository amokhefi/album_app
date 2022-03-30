package com.abd.dev.album.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.abd.dev.album.R
import com.abd.dev.album.databinding.AlbumItemBinding


class AlbumListAdapter(
    private val onClick: (UiAlbum) -> Unit
) : ListAdapter<UiAlbum, AlbumListAdapter.ViewHolder>(AlbumDiffCallback) {

    var currentPosition = -1

    inner class ViewHolder(private val binding: AlbumItemBinding, onClick: (UiAlbum) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private var currentItem: UiAlbum? = null

        init {
            itemView.setOnClickListener {
                currentItem?.let {
                    notifyItemChanged(currentPosition)
                    currentPosition = position
                    notifyItemChanged(currentPosition)
                    onClick(it)
                }
            }
        }

        fun bind(item: UiAlbum, position: Int) {
            currentItem = item
            binding.title.text = item.title
            binding.cardContainer.background.setTint(
                binding.root.context.getColor(
                    if (currentPosition == position) R.color.orange
                    else R.color.white
                )
            )
            binding.category.text =
                String.format(itemView.context.getString(R.string.category), item.albumId)
            binding.thumbnailImage.load(item.thumbnailUrl) {
                crossfade(true)
                memoryCachePolicy(CachePolicy.ENABLED)
                placeholder(R.drawable.loading_image)
                error(R.drawable.broken_image)
            }
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
        holder.bind(item, position)
    }
}

object AlbumDiffCallback : DiffUtil.ItemCallback<UiAlbum>() {
    override fun areItemsTheSame(oldItem: UiAlbum, newItem: UiAlbum): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UiAlbum, newItem: UiAlbum): Boolean {
        return oldItem.id == newItem.id
    }
}

