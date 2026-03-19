package edu.oregonstate.cs492.ColorPaletteApp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import edu.oregonstate.cs492.ColorPaletteApp.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.ColorPaletteApp.data.Palette
import androidx.core.view.MenuHost
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.Lifecycle

class GeneratePaletteFragment : Fragment(R.layout.fragment_generate) {
    private val viewModel: ColorSetViewModel by viewModels()
    private val saveViewModel: PaletteViewModel by viewModels()

    private lateinit var colorSetAdapter: ColorSetAdapter

    private lateinit var colorListRV: RecyclerView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    private lateinit var shuffleButton: Button
    private lateinit var saveButton: Button
    private lateinit var shareButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        colorSetAdapter = ColorSetAdapter(viewModel)

        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        colorListRV = view.findViewById(R.id.rv_color_list)
        colorListRV.layoutManager = LinearLayoutManager(requireContext())
        colorListRV.setHasFixedSize(true)
        colorListRV.adapter = colorSetAdapter

        shuffleButton = view.findViewById(R.id.button_shuffle)
        saveButton = view.findViewById(R.id.button_save)
        shareButton = view.findViewById(R.id.button_share)

        viewModel.colorSet.observe(viewLifecycleOwner) { colorSet ->
            val locks = viewModel.locks.value
            if (colorSet != null) {
                colorSetAdapter.updateColors(colorSet, locks)
                colorListRV.visibility = View.VISIBLE
                colorListRV.scrollToPosition(0)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingIndicator.visibility = View.VISIBLE
                loadingErrorTV.visibility = View.INVISIBLE
                colorListRV.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                loadingErrorTV.text = getString(R.string.loading_error, error.message)
                loadingErrorTV.visibility = View.VISIBLE
                colorListRV.visibility = View.INVISIBLE
            }
        }

        shuffleButton.setOnClickListener {
            viewModel.loadColorPalette(viewModel.colorSet.value?.colors ?: listOf("N", "N", "N", "N", "N"), "default")
        }

        saveButton.setOnClickListener {
            saveViewModel.addPalette(Palette(colors=viewModel.colorSet.value?.colors ?: listOf("#000000","#000000","#000000","#000000","#000000")))
        }

        shareButton.setOnClickListener {
            share(viewModel)
        }

        val menuHost: MenuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.generate_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_palettes -> {
                            val directions = GeneratePaletteFragmentDirections.navigateToPalettes()
                            findNavController().navigate(directions)
                            true
                        }
                        else -> false
                    }
                }

            },
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )

    }

    override fun onResume() {
        super.onResume()
        // generateNewPalette()
    }

    private fun generateNewPalette() {
        val currentColors = viewModel.colorSet.value?.colors ?: listOf("N", "N", "N", "N", "N")
        val currentLocks = viewModel.locks.value ?: listOf(false, false, false, false, false)

        val input = currentColors.mapIndexed { index, color ->
            if (currentLocks[index]) color else "N"
        }

        viewModel.loadColorPalette(input, "default")
    }

    private fun share(viewModel: ColorSetViewModel) {
        if (viewModel.colorSet.value == null) {
            return
        }
        val shareText = "Check out this color palette: ${viewModel.colorSet.value!!.colors.joinToString(", ")}"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Share Palette"))
    }
}