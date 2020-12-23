package com.example.islands

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IslandsFragment : Fragment() {

    private val viewModel: IslandsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_islands, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val islandTextView = view.findViewById<TextView>(R.id.islandsTextView)
        view.findViewById<Button>(R.id.restartButton).setOnClickListener {
            islandTextView.visibility = View.GONE
            val action = IslandsFragmentDirections.actionIslandsFragmentToStartFragment()
            findNavController().navigate(action)
        }

        val waitingProgressBar = view.findViewById<View>(R.id.waitingProgressBar)
        waitingProgressBar.visibility = View.VISIBLE
        viewModel.calculateNumOfIslands().observe(viewLifecycleOwner, {
            islandTextView.text = String.format(getString(R.string.foundIslands), it)
            lifecycleScope.launch(Dispatchers.Default) {
                val islandsGridBitmap = viewModel.islandsGridBitmap
                withContext(Dispatchers.Main) {
                    waitingProgressBar.visibility = View.GONE
                    islandTextView.visibility = View.VISIBLE
                    view.findViewById<ImageView>(R.id.islandsBitmapImageView).setImageBitmap(islandsGridBitmap)
                }
            }
        })
    }
}