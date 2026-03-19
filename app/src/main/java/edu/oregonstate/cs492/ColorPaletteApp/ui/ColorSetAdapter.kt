package edu.oregonstate.cs492.ColorPaletteApp.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.ColorPaletteApp.R
import edu.oregonstate.cs492.ColorPaletteApp.data.ColorSet

class ColorSetAdapter : RecyclerView.Adapter<ColorSetAdapter.ViewHolder>() {
    private var colorDisplay: List<String> = listOf()

    fun updateColors(colorSet: ColorSet?) {
        colorDisplay = colorSet?.colors ?: listOf()
        notifyDataSetChanged()
    }

    override fun getItemCount() = colorDisplay.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.color_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(colorDisplay[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hexTV: TextView = itemView.findViewById(R.id.tv_hex)
        private val colorSwatch: View = itemView.findViewById(R.id.view_color_swatch)

        fun bind(colorHex: String) {
            hexTV.text = colorHex
            try {
                colorSwatch.setBackgroundColor(Color.parseColor(colorHex))
            } catch (e: IllegalArgumentException) {
                colorSwatch.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}