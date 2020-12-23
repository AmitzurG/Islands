package com.example.islands

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PixelsFragment : Fragment() {

    private val viewModel: IslandsViewModel by activityViewModels()

    private val args: PixelsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_pixels, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.solveButton).setOnClickListener {
            val action = PixelsFragmentDirections.actionPixelsFragmentToIslandsFragment()
            findNavController().navigate(action)
        }
        val waitingProgressBar = view.findViewById<View>(R.id.waitingProgressBar)
        waitingProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.Default) {
            viewModel.createRandom01Grid(args.gridHeight, args.gridWidth)
            val pixelGridBitmap = viewModel.pixelsGridBitmap
            withContext(Dispatchers.Main) {
                waitingProgressBar.visibility = View.GONE
                view.findViewById<ImageView>(R.id.pixelsBitmapImageView).setImageBitmap(pixelGridBitmap)
            }
        }
    }
}