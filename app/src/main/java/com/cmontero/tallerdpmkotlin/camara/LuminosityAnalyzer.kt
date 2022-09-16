package com.cmontero.tallerdpmkotlin.camara

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer


/** Helper type alias used for analysis use case callbacks */
typealias LumaListener = (luma: Double) -> Unit

class LuminosityAnalyzer(listener:LumaListener?=null): ImageAnalysis.Analyzer {

    private val frameRateWindow = 8
    private val frameTimestamps = ArrayDeque<Long>(5)
    private val listeners = ArrayList<LumaListener>().apply{listener?.let{add(it)}}
    private var lastAnalyzedTimestamp = 0L
    var framePerSecond:Double = -1.0
    private set

    fun onFrameAnalyzed(listener:LumaListener) = listeners.add(listener)

    private fun ByteBuffer.toByteArray():ByteArray{
        rewind() // rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data) // copy the buffer into a byte array
        return data  // return the byte array
    }

    override fun analyze(image: ImageProxy) {
        if(listeners.isEmpty()){
            image.close()
            return
        }

        val currentTime = System.currentTimeMillis()
        frameTimestamps.add(currentTime)

        while(frameTimestamps.size >= frameRateWindow)
            frameTimestamps.removeLast()

        val timestampFirst = frameTimestamps.first()
        val timestampLast = frameTimestamps.last()
        framePerSecond = 1.0 / ((timestampFirst - timestampLast) / frameTimestamps.size.coerceAtLeast(1).toDouble()) * 1000.0

        lastAnalyzedTimestamp = frameTimestamps.first()

        val buffer = image.planes[0].buffer

        val data = buffer.toByteArray()

        val pixels = data.map{it.toInt() and 0xFF}

        val luma = pixels.average()

        listeners.forEach { it(luma) }

        image.close()


    }


}