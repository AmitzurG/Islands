package com.example.islands

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class StartFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_start, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.findViewById<Button>(R.id.goButton).setOnClickListener {
        try {
            val height = view.findViewById<EditText>(R.id.gridHeightEditText).text.toString().toInt()
            val width = view.findViewById<EditText>(R.id.gridWidthEditText).text.toString().toInt()
            if (height > 0 && width > 0) {
                val action = StartFragmentDirections.actionStartFragmentToPixelsFragment(height, width)
                findNavController().navigate(action)
            } else {
                InvalidSizesDialogFragment().show(childFragmentManager, null)
            }
        } catch (e: NumberFormatException) {
            val action = StartFragmentDirections.actionStartFragmentToPixelsFragment()
            findNavController().navigate(action)
        }
    }

    class InvalidSizesDialogFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.error)).setMessage(R.string.heightWidthNoGreater1)
                    .setPositiveButton(getString(R.string.ok)) { _, _ -> dismiss() }

            return alertDialog.create()
        }
    }
}