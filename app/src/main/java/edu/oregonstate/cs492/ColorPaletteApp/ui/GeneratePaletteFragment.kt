package edu.oregonstate.cs492.ColorPaletteApp.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import edu.oregonstate.cs492.ColorPaletteApp.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

class GeneratePaletteFragment : Fragment(R.layout.fragment_generate) {
    private val viewModel: ColorSetViewModel by viewModels()
    private val colorSetAdapter = ColorSetAdapter()

    private lateinit var colorListRV: RecyclerView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        colorListRV = view.findViewById(R.id.rv_color_list)
        colorListRV.layoutManager = LinearLayoutManager(requireContext())
        colorListRV.setHasFixedSize(true)
        colorListRV.adapter = colorSetAdapter

        viewModel.colorSet.observe(viewLifecycleOwner) { colorSet ->
            if (colorSet != null) {
                colorSetAdapter.updateColors(colorSet)
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
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadColorPalette(listOf("N", "N", "N", "N", "N"), "default")
    }
}