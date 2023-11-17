package com.boom.compass

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.boom.compass.databinding.DialogBinding

class WarningDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var adb = AlertDialog.Builder(requireContext())
        val binding = DialogBinding.inflate(layoutInflater,null,false)
        binding.button.setOnClickListener {
            dismiss()
        }
        adb = adb.setView(binding.root)
        val dialog = adb.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}