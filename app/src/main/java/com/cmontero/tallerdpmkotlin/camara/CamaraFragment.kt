package com.cmontero.tallerdpmkotlin.camara

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.databinding.FragmentCamaraBinding
import java.io.File
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class CamaraFragment : Fragment() {

    private var _binding: FragmentCamaraBinding? = null
    private val binding get() = _binding!!

    private var preview:Preview?=null
    private var imageCapture:ImageCapture?=null
    private var cameraProvider: ProcessCameraProvider? = null
    private var lensFacing:Int = CameraSelector.LENS_FACING_BACK
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor:ExecutorService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCamaraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        lensFacing = CameraSelector.LENS_FACING_BACK

        if(allPermissionGranted()){
            startCamara()
        }
        else{
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.btnphotoCapture.setOnClickListener{
            takePhoto()
        }

    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let{
            File(it, resources.getString(R.string.app_name)).apply{
                mkdirs()
            }
        }

        return if(mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    private fun takePhoto(){
        try {
            val imageCapture = imageCapture ?: return
            val format = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg"
            val photoFile = File(outputDirectory, format)

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Toast.makeText(requireContext(), "Photo capture failed: ${exc.message}", Toast.LENGTH_SHORT).show()
                    }
                    override fun onImageSaved(output: ImageCapture.OutputFileResults){
                        val savedUri = Uri.fromFile(photoFile)
                        Toast.makeText(requireContext(), "Imagen guardada $savedUri", Toast.LENGTH_SHORT).show()

                        setGalleryThumbnail(savedUri)

                        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(savedUri.toFile().extension)
                        MediaScannerConnection.scanFile(requireContext(), arrayOf(savedUri.toFile().absolutePath), arrayOf(mimeType)){ _,uri->
                            Toast.makeText(requireContext(),"Imagen capturada fue guardada:$uri",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun setGalleryThumbnail(savedUri: Uri?) {
        binding.btnphotoView.let { button ->
            button.post {
                button.setPadding(resources.getDimension(R.dimen.stroke_small).toInt())
                // Load thumbnail into circular button using Glide
                Glide.with(button)//button
                    .load(savedUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(button)
            }
        }
    }

    private fun startCamara() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            lensFacing = when{
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("No existen dispositivos de captura disponibles...")
            }

            updateCameraSwitchButton()

            bindCameraUseCases()

        }, ContextCompat.getMainExecutor(requireContext()))

    }

    @SuppressLint("WrongConstant")
    private fun bindCameraUseCases() {
        val metrics = DisplayMetrics().also {
            binding.viewFinder.display.getRealMetrics(it)
        }

        val screenAspectRadio = aspectRatio(metrics.widthPixels, metrics.heightPixels)

        val rotation = binding.viewFinder.display.rotation

        val cameraProvider = cameraProvider?:throw IllegalStateException("Error al iniciar la camara")

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        //Preview
        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRadio as Int)
            .setTargetRotation(rotation)
            .build()

        //ImageCapture
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRadio)
            .setTargetRotation(rotation)
            .build()


        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        }
        catch (e:Exception){
            e.printStackTrace()
        }


    }

    private fun aspectRatio(width: Int, height: Int): Any {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun updateCameraSwitchButton() {
        try {
            binding.btncameraSwitch.isEnabled = hasBackCamera() && hasFrontCamera()
        }
        catch (e:Exception){
            binding.btncameraSwitch.isEnabled = false
        }
    }

    // Devuelve true si el dispositivo cuenta con camara trasera, caso contrario devuelve false
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?:false
    }

    // Devuelve true si el dispositivo cuenta con camara frontal, caso contrario devuelve false
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?:false
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionGranted()){
                startCamara()
            }else{
                Toast.makeText(requireContext(),"Los permisos no han sido otorgados",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(),it) == PackageManager.PERMISSION_GRANTED
    }


    companion object{
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        private val REQUIRED_PERMISSIONS = mutableListOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).apply {
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}