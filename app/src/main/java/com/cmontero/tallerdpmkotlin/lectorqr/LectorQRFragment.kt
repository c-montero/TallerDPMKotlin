package com.cmontero.tallerdpmkotlin.lectorqr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.cmontero.tallerdpmkotlin.databinding.FragmentLectorqrBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class LectorQRFragment : Fragment() {

    private var _binding: FragmentLectorqrBinding? = null
    private val binding get() = _binding!!
    private lateinit var barcodeLauncher:ActivityResultLauncher<ScanOptions>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        barcodeLauncher = registerForActivityResult(ScanContract())
        { result: ScanIntentResult ->
            if (result.contents != null) {
                binding.txtContenido.text = result.contents
                Toast.makeText(requireContext(),"Scanned: " + result.contents,Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLectorqrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLectorQR.setOnClickListener {
            val options = ScanOptions()
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                .setPrompt("Escaneando ando el QR Code")
                .setCameraId(0)
                .setBeepEnabled(true)
                .setTorchEnabled(false)
                .setBarcodeImageEnabled(true)

            barcodeLauncher.launch(options)
        }

    }



}