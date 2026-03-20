package edu.oregonstate.cs492.ColorPaletteApp.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.ColorPaletteApp.R
import edu.oregonstate.cs492.ColorPaletteApp.data.ColorSet
import com.google.android.material.button.MaterialButton

class ColorSetAdapter(
    private val viewModel: ColorSetViewModel,
    private val onAddClick: (Int) -> Unit
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
            viewModel,
            onAddClick)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hexTV: TextView = itemView.findViewById(R.id.tv_hex)
        private val colorSwatch: View = itemView.findViewById(R.id.view_color_swatch)
        private val lockButton: MaterialButton = itemView.findViewById(R.id.button_lock)
        private val addButton: MaterialButton = itemView.findViewById(R.id.button_add)

        fun bind(colorHex: String, lockState: Boolean, idx: Int, viewModel: ColorSetViewModel, onAddClick: (Int) -> Unit) {
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

            addButton.setOnClickListener {
                onAddClick(idx)
            }

            hexTV.setOnClickListener {
                val context = itemView.context
                val input = EditText(context)
                input.setText(colorHex)

                AlertDialog.Builder(context)
                    .setTitle("Edit Hex Code")
                    .setView(input)
                    .setPositiveButton("OK") { _, _ ->
                        val newHex = input.text.toString()
                        if (newHex.startsWith("#") && (newHex.length == 7)) {
                            viewModel.updateColor(idx, newHex)
                        } else if (newHex.length == 6) {
                            viewModel.updateColor(idx, "#$newHex")
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }

            colorSwatch.setOnClickListener {
                val context = itemView.context
                val currentColor = try { Color.parseColor(colorHex) } catch (e: Exception) { Color.GRAY }
                val r = Color.red(currentColor)
                val g = Color.green(currentColor)
                val b = Color.blue(currentColor)

                val dialogView = android.widget.LinearLayout(context).apply {
                    orientation = android.widget.LinearLayout.VERTICAL
                    setPadding(48, 32, 48, 16)
                }

                val preview = View(context).apply {
                    layoutParams = android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 120
                    )
                    setBackgroundColor(currentColor)
                }

                fun makeSliderRow(label: String, value: Int): Pair<android.widget.SeekBar, android.widget.TextView> {
                    val row = android.widget.LinearLayout(context).apply {
                        orientation = android.widget.LinearLayout.HORIZONTAL
                        layoutParams = android.widget.LinearLayout.LayoutParams(
                            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }
                    val lbl = android.widget.TextView(context).apply {
                        text = label
                        layoutParams = android.widget.LinearLayout.LayoutParams(40, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                    }
                    val seekBar = android.widget.SeekBar(context).apply {
                        max = 255
                        progress = value
                        layoutParams = android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    }
                    val valLabel = android.widget.TextView(context).apply {
                        text = value.toString()
                        layoutParams = android.widget.LinearLayout.LayoutParams(60, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                    }
                    row.addView(lbl); row.addView(seekBar); row.addView(valLabel)
                    return Pair(seekBar, valLabel)
                }

                val (rSeek, rVal) = makeSliderRow("R", r)
                val (gSeek, gVal) = makeSliderRow("G", g)
                val (bSeek, bVal) = makeSliderRow("B", b)

                dialogView.addView(preview)
                dialogView.addView(rSeek.parent as android.view.View)
                dialogView.addView(gSeek.parent as android.view.View)
                dialogView.addView(bSeek.parent as android.view.View)

                fun updatePreview() {
                    val newColor = Color.rgb(rSeek.progress, gSeek.progress, bSeek.progress)
                    preview.setBackgroundColor(newColor)
                    rVal.text = rSeek.progress.toString()
                    gVal.text = gSeek.progress.toString()
                    bVal.text = bSeek.progress.toString()
                }

                val listener = object : android.widget.SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(s: android.widget.SeekBar?, p: Int, u: Boolean) = updatePreview()
                    override fun onStartTrackingTouch(s: android.widget.SeekBar?) {}
                    override fun onStopTrackingTouch(s: android.widget.SeekBar?) {}
                }
                rSeek.setOnSeekBarChangeListener(listener)
                gSeek.setOnSeekBarChangeListener(listener)
                bSeek.setOnSeekBarChangeListener(listener)

                AlertDialog.Builder(context)
                    .setTitle("Pick Color")
                    .setView(dialogView)
                    .setPositiveButton("OK") { _, _ ->
                        val newHex = "#%02X%02X%02X".format(rSeek.progress, gSeek.progress, bSeek.progress)
                        viewModel.updateColor(idx, newHex)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }

            try {
                colorSwatch.setBackgroundColor(Color.parseColor(colorHex))
            } catch (e: IllegalArgumentException) {
                colorSwatch.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}