package edu.oregonstate.cs492.ColorPaletteApp.ui

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.oregonstate.cs492.ColorPaletteApp.R

class ImportColorFragment : Fragment(R.layout.fragment_import) {
    private val viewModel: ColorSetViewModel by activityViewModels()
    private val args: ImportColorFragmentArgs by navArgs()

    private lateinit var imageView: ImageView
    private lateinit var colorPreview: View
    private var selectedColor: Int = Color.GRAY

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageView.setImageURI(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.iv_image_picker)
        colorPreview = view.findViewById(R.id.view_selected_color)
        val selectImageButton: Button = view.findViewById(R.id.button_select_image)
        val confirmButton: Button = view.findViewById(R.id.button_confirm_color)

        selectImageButton.setOnClickListener {
            getContent.launch("image/*")
        }

        imageView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
                bitmap?.let {
                    val x = event.x.toInt()
                    val y = event.y.toInt()

                    // Convert view coordinates to bitmap coordinates
                    val viewWidth = v.width
                    val viewHeight = v.height
                    val bitmapWidth = it.width
                    val bitmapHeight = it.height

                    val bitmapX = (x.toFloat() / viewWidth * bitmapWidth).toInt().coerceIn(0, bitmapWidth - 1)
                    val bitmapY = (y.toFloat() / viewHeight * bitmapHeight).toInt().coerceIn(0, bitmapHeight - 1)

                    selectedColor = it.getPixel(bitmapX, bitmapY)
                    colorPreview.setBackgroundColor(selectedColor)
                }
            }
            true
        }

        confirmButton.setOnClickListener {
            val hexColor = String.format("#%06X", 0xFFFFFF and selectedColor)
            viewModel.updateColor(args.index, hexColor)
            findNavController().navigateUp()
        }
    }
}
