package edu.oregonstate.cs492.ColorPaletteApp.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.ColorPaletteApp.R

class SavedPaletteFragment : Fragment(R.layout.fragment_palettes) {
    private val viewModel: PaletteViewModel by viewModels()
    private val colorSetViewModel: ColorSetViewModel by activityViewModels()
    private lateinit var adapter: SavedPaletteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedPalettesRV: RecyclerView = view.findViewById(R.id.rv_saved_palettes)
        val emptyMessageTV: TextView = view.findViewById(R.id.tv_empty_message)

        adapter = SavedPaletteAdapter(
            onEditClick = { palette ->
                colorSetViewModel.setNewPalette(palette.colors)
                findNavController().navigateUp()
            },
            onShareClick = { palette ->
                val shareText = "Check out this color palette: ${palette.colors.joinToString(", ")}"
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                startActivity(Intent.createChooser(intent, "Share Palette"))
            },
            onCopyClick = { palette ->
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("palette", palette.colors.joinToString(", "))
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { palette ->
                viewModel.deletePalette(palette)
            }
        )

        savedPalettesRV.layoutManager = LinearLayoutManager(requireContext())
        savedPalettesRV.adapter = adapter

        viewModel.palettes.observe(viewLifecycleOwner) { palettes ->
            if (palettes.isNullOrEmpty()) {
                emptyMessageTV.visibility = View.VISIBLE
                savedPalettesRV.visibility = View.GONE
            } else {
                emptyMessageTV.visibility = View.GONE
                savedPalettesRV.visibility = View.VISIBLE
                adapter.updatePalettes(palettes)
            }
        }
    }
}
