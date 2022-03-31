package com.abd.dev.album.presentation

import android.app.AlertDialog
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
            viewModel.uiState.collectLatest {
                when (it) {
                    is AlbumState.Error -> showError(it.error)
                    AlbumState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is AlbumState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        albumListAdapter.submitList(it.albums)
                    }
                }

            }
        }
    }

    private fun showError(error: NetworkError) {
        binding.progressBar.visibility = View.GONE
        when (error) {
            NetworkError.UnAvailableError -> {
                showErrorMessage(
                    getString(R.string.network_issues_message),
                    getString(R.string.retry)
                ) {
                    viewModel.loadItems()
                }
            }
            NetworkError.UnknownError -> {
                showErrorMessage(
                    getString(R.string.unkonw_error),
                    getString(R.string.cancel)
                ) {
                    requireActivity().finish()

                }
            }
            NetworkError.RemoteError -> {
                showErrorMessage(
                    getString(R.string.remote_server_error),
                    getString(R.string.cancel)
                ) {
                    requireActivity().finish()
                }
            }
        }
    }

    private fun showErrorMessage(
        message: String,
        positiveButtonText: String,
        onClick: () -> Unit = {}
    ) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
                dialog.dismiss()
                onClick.invoke()
            }.create().show()
    }

    private fun onAlbumClicked(album: UiAlbum) {
        viewModel.setSelectedItem(album)
        if (requireContext().resources.getBoolean(R.bool.isLageScreen).not()) {
            findNavController().navigate(R.id.action_listFragment_to_DetailFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}