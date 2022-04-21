package com.example.nagwa_task.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.SyncStateContract
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {

        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
    fun convertBitmapIntoBytes(bitmap: Bitmap):ByteArray{
        val out =ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,30,out)
        var by:ByteArray=out.toByteArray()
        val quality:Float
        quality=if (by.size / 1024f >Constants.Max_Image_Size){

            100*Constants.Max_Image_Size/(by.size/1024f)
        }else{
            return out.toByteArray()
        }
        out.reset()
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality.toInt(),out)
        by=out.toByteArray()
        return by
    }
    fun getBitmap(path:String?):Bitmap{
        return BitmapFactory.decodeFile(path)
    }





    object Constants {
        const val Max_Image_Size=1024*4
    }

}