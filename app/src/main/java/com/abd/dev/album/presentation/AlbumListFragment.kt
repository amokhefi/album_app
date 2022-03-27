package com.abd.dev.album.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abd.dev.album.R
import com.abd.dev.album.databinding.FragmentAlbumListBinding
import com.abd.dev.album.domain.model.Album
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AlbumListFragment : Fragment() {

    private var _binding: FragmentAlbumListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlbumViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val albumListAdapter = AlbumListAdapter {
            onAlbumClicked(it)
        }
        binding.albumRecyclerview.adapter = albumListAdapter
        binding.albumRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launchWhenCreated {
            viewModel.albumList.collectLatest {
                albumListAdapter.submitList(it)
            }
        }
    }

    private fun onAlbumClicked(album: Album) {
        findNavController().navigate(R.id.action_listFragment_to_DetailFragment)
        viewModel.setSelectedItem(album)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}