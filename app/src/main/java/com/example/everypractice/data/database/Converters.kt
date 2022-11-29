package com.example.everypractice.data.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

private const val DELIMITER = ","

/**
 * Esta clase convierte una lista a datos primitivos, esto debido a que Room no acepta objetos.
 */

class Converters {

    @TypeConverter
    fun fromList(list: List<Int>?): String? {
        return list?.joinToString()
    }

    @TypeConverter
    fun toList(linearValue: String?): List<Int>? {
        return if (linearValue.isNullOrEmpty()) emptyList() else linearValue.split(DELIMITER).map {
            it.trim().toInt()
        }
    }

    //  TODO AQUI SE SELECCIONA LA CALIDAD DE GUARDADO DE IMAGEN
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return  BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }


}