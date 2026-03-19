package edu.oregonstate.cs492.ColorPaletteApp.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.ColorPaletteApp.R
import edu.oregonstate.cs492.ColorPaletteApp.data.Palette
import com.google.android.material.button.MaterialButton

class SavedPaletteAdapter(
    private val onEditClick: (Palette) -> Unit,
    private val onShareClick: (Palette) -> Unit,
    private val onCopyClick: (Palette) -> Unit,
    private val onDeleteClick: (Palette) -> Unit
) : RecyclerView.Adapter<SavedPaletteAdapter.ViewHolder>() {
    private var palettes: List<Palette> = listOf()

    fun updatePalettes(newPalettes: List<Palette>) {
        palettes = newPalettes
        notifyDataSetChanged()
    }

    override fun getItemCount() = palettes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.palette_list_item, parent, false)
        return ViewHolder(view, onEditClick, onShareClick, onCopyClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // inverting the order so the most recent palette is at the top
        val idx = palettes.size - 1 - position
        holder.bind(palettes[idx])
    }

    class ViewHolder(
        itemView: View,
        private val onEditClick: (Palette) -> Unit,
        private val onShareClick: (Palette) -> Unit,
        private val onCopyClick: (Palette) -> Unit,
        private val onDeleteClick: (Palette) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val swatchContainer: View = itemView.findViewById(R.id.layout_swatch_container)
        private val submenu: View = itemView.findViewById(R.id.layout_submenu)
        
        private val swatches = listOf<View>(
            itemView.findViewById(R.id.view_color_1),
            itemView.findViewById(R.id.view_color_2),
            itemView.findViewById(R.id.view_color_3),
            itemView.findViewById(R.id.view_color_4),
            itemView.findViewById(R.id.view_color_5)
        )

        private val editButton: MaterialButton = itemView.findViewById(R.id.button_palette_edit)
        private val shareButton: MaterialButton = itemView.findViewById(R.id.button_palette_share)
        private val copyButton: MaterialButton = itemView.findViewById(R.id.button_palette_copy)
        private val deleteButton: MaterialButton = itemView.findViewById(R.id.button_palette_delete)
        
        fun bind(palette: Palette) {
            palette.colors.forEachIndexed { index, hex ->
                if (index < swatches.size) {
                    try {
                        swatches[index].setBackgroundColor(Color.parseColor(hex))
                    } catch (e: Exception) {
                        swatches[index].setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }

            swatchContainer.setOnClickListener {
                submenu.visibility = if (submenu.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }

            editButton.setOnClickListener { onEditClick(palette) }
            shareButton.setOnClickListener { onShareClick(palette) }
            copyButton.setOnClickListener { onCopyClick(palette) }
            deleteButton.setOnClickListener { onDeleteClick(palette) }
        }
    }
}