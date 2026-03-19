package edu.oregonstate.cs492.ColorPaletteApp.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.ColorPaletteApp.R
import edu.oregonstate.cs492.ColorPaletteApp.data.ColorSet
import com.google.android.material.button.MaterialButton

class ColorSetAdapter(
    private val viewModel: ColorSetViewModel
) : RecyclerView.Adapter<ColorSetAdapter.ViewHolder>() {
    private var colorDisplay: List<String> = listOf()
    private var lockDisplay: List<Boolean> = listOf()

    fun updateColors(colorSet: ColorSet?, lockSet: List<Boolean>?) {
        colorDisplay = colorSet?.colors ?: listOf()
        lockDisplay = lockSet?: listOf()
        notifyDataSetChanged()
    }

    override fun getItemCount() = colorDisplay.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.color_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = colorDisplay[position]
        val lock = lockDisplay.getOrNull(position) ?: false

        holder.bind(color,
            lock,
            position,
            viewModel)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hexTV: TextView = itemView.findViewById(R.id.tv_hex)
        private val colorSwatch: View = itemView.findViewById(R.id.view_color_swatch)
        private val lockButton: MaterialButton = itemView.findViewById(R.id.button_lock)

        fun bind(colorHex: String, lockState: Boolean, idx: Int, viewModel: ColorSetViewModel) {
            hexTV.text = colorHex

            val lockIcon = if (lockState) {
                R.drawable.ic_locked
            } else {
                R.drawable.ic_unlocked
            }
            lockButton.setIconResource(lockIcon)

            lockButton.setOnClickListener {
                viewModel.toggleLock(idx, !lockState, lockButton)
                val tempIcon = if (!lockState) {
                    R.drawable.ic_locked
                } else {
                    R.drawable.ic_unlocked
                }
                lockButton.setIconResource(tempIcon)
            }

            try {
                colorSwatch.setBackgroundColor(Color.parseColor(colorHex))
            } catch (e: IllegalArgumentException) {
                colorSwatch.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}