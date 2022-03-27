package com.abd.dev.album.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.request.CachePolicy
import com.abd.dev.album.R
import com.abd.dev.album.databinding.FragmentDetailAlbumBinding
import com.abd.dev.album.domain.model.Album
import kotlinx.coroutines.flow.collectLatest

class AlbumDetailFragment : Fragment() {

    private var _binding: FragmentDetailAlbumBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlbumViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenCreated {
            viewModel.selectedAlbum.collectLatest {
                it?.let {
                    updateAlbumInfo(it)
                }
            }
        }
    }

    private fun updateAlbumInfo(album: Album) {
        binding.apply {
            title.text = album.title
            category.text = String.format(getString(R.string.category), album.albumId)
            albumImage.load(album.url) {
                crossfade(true)
                memoryCachePolicy(CachePolicy.ENABLED)
                placeholder(R.drawable.loading_image)
                error(R.drawable.broken_image)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}