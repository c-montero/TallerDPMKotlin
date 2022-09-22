package com.cmontero.tallerdpmkotlin.utils

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import java.util.*


class DatePickerFragment : DialogFragment() {
    private var listener: OnDateSetListener? = null

    private fun setListener(listener: OnDateSetListener) {
        this.listener = listener
    }

    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireActivity(), listener, year, month, day)
    }

    companion object {
        fun newInstance(listener: OnDateSetListener): DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.setListener(listener)
            return fragment
        }
    }
}