package com.example.machineround.machinetask.utils

import android.content.Context
import java.io.IOException
import java.nio.charset.Charset

fun readJSONFromAssets(context: Context, fileName: String): String? {
    var jsonString: String? = null
    try {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        jsonString = String(buffer, Charset.defaultCharset())
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}