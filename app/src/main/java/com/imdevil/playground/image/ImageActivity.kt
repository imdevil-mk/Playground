package com.imdevil.playground.image

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.text.format.Formatter
import android.util.Log
import android.widget.ImageView
import com.imdevil.playground.R
import com.imdevil.playground.base.LogActivity


class ImageActivity : LogActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val drawable = BatteryDrawable()
        drawable.computeBattery(resources, R.drawable.battery)
        findViewById<ImageView>(R.id.shimmer).setImageDrawable(drawable)

        calculateSize()
    }

    private fun calculateSize() {
        val options = BitmapFactory.Options()

        val defaultBitmap = BitmapFactory.decodeResource(resources, R.drawable.def, options)
        val nodpi = BitmapFactory.decodeResource(resources, R.drawable.def_nodpi)
        val ldpi = BitmapFactory.decodeResource(resources, R.drawable.def_ldpi)
        val mdpi = BitmapFactory.decodeResource(resources, R.drawable.def_mdpi)
        val hdpi = BitmapFactory.decodeResource(resources, R.drawable.def_hdpi)
        val xhdpi = BitmapFactory.decodeResource(resources, R.drawable.def_xhdpi)
        val xxhdpi = BitmapFactory.decodeResource(resources, R.drawable.def_xxhdpi)
        val xxxhdpi = BitmapFactory.decodeResource(resources, R.drawable.def_xxxhdpi)

        val asset = BitmapFactory.decodeStream(resources.assets.open("def_asset.jpg"))
        val raw = BitmapFactory.decodeStream(resources.openRawResource(R.raw.def_raw))

        logBitmap("nodpi", nodpi)
        logBitmap("ldpi-120", ldpi)
        logBitmap("default-160", defaultBitmap, options)
        logBitmap("mdpi-160", mdpi)
        logBitmap("hdpi-240", hdpi)
        logBitmap("xhdpi-320", xhdpi)
        logBitmap("xxhdpi-480", xxhdpi)
        logBitmap("xxxhdpi-640", xxxhdpi)

        logBitmap("asset", asset)
        logBitmap("raw", raw)

        /*
        2600px * 1736px * 4byte = 18054400 byte  = 18.05 MB

        widthInPixel * ((screenDensity / inDensity) + 0.5) * heightInPixel * ((screenDensity / inDensity) + 0.5) * onePixelBytes

        nodpi:          allocationByteCount = 18054400  = 18.05 MB
        ldpi-120:       allocationByteCount = 288870400 = 18054400 * 4^2    = 289 MB
        default-160:    allocationByteCount = 162489600 = 18054400 * 3^2    = 162 MB
        mdpi-160:       allocationByteCount = 162489600 = 18054400 * 3^2    = 162 MB
        hdpi-240:       allocationByteCount = 72217600  = 18054400 * 2^2    = 72.22 MB
        xhdpi-320:      allocationByteCount = 40622400  = 18054400 * 1.5^2  = 40.62 MB
        xxhdpi-480:     allocationByteCount = 18054400  = 18.05 MB
        xxxhdpi-640:    allocationByteCount = 10155600  = 18054400 * 0.75^2 = 10.16 MB

        asset:          allocationByteCount = 18054400  = 18.05 MB
        raw:            allocationByteCount = 18054400  = 18.05 MB
        */
    }


    private fun reConfigBitmap() {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.def_nodpi, options)
        logBitmap("Config_RGB_565", bitmap, options)
        /*  2600px * 1736px * 2byte = 9027200 = 9.03 MB
        Config_RGB_565: allocationByteCount = 9027200 = 9.03 MB
         */
    }

    private fun sampleBitmap() {
        val imageView = findViewById<ImageView>(R.id.image)
        val viewTreeObserver = imageView.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener() {
            val (height: Int, width: Int) = run { imageView.height to imageView.width }
            Log.d("sampleBitmap", "ImageView: height = $height width = $width")
            val bitmap =
                decodeSampledBitmapFromResource(resources, R.drawable.def_nodpi, height, width)
            logBitmap("sampleBitmap", bitmap)
            imageView.setImageBitmap(bitmap)
        }

        /*  2600px * 1736px ---> inSampleSize = 2
        sampleBitmap: ImageView: height = 600 width = 600
        sampleBitmap: allocationByteCount = 4513600 = 4.51 MB
        sampleBitmap: byteCount = 4513600  ---> actualSize(18054400)/pow(inSampleSize)
         */
    }

    private fun decodeSampledBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(res, resId, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            Log.d("sampleBitmap", "decodeSampledBitmapFromResource: inSampleSize = $inSampleSize")

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false
            // get more smaller
            //inPreferredConfig = Bitmap.Config.RGB_565

            BitmapFactory.decodeResource(res, resId, this)
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        Log.d(
            "sampleBitmap",
            "calculateInSampleSize: bitmap actual height = $height width = $width"
        )
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /*
    加载大图
    */
    @SuppressLint("NewApi")
    private fun bigBitmap(fd: ParcelFileDescriptor) {
        val bitmapRegionDecoder: BitmapRegionDecoder = BitmapRegionDecoder.newInstance(fd)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        val bitmap = bitmapRegionDecoder.decodeRegion(
            Rect(/* you need to set a rect */), options
        )
    }

    private fun logBitmap(tag: String, bitmap: Bitmap, options: BitmapFactory.Options? = null) {
        Log.d(getLogTag() + " " + tag, "logBitmap: ----------------")
        Log.d(getLogTag() + " " + tag, "outConfig = ${options?.outConfig}")
        Log.d(getLogTag() + " " + tag, "outColorSpace = ${options?.outColorSpace}")
        Log.d(getLogTag() + " " + tag, "outWidth = ${options?.outWidth}")
        Log.d(getLogTag() + " " + tag, "outHeight = ${options?.outHeight}")
        Log.d(getLogTag() + " " + tag, "outMimeType = ${options?.outMimeType}")
        Log.d(
            getLogTag() + " " + tag,
            "allocationByteCount = ${bitmap.allocationByteCount} = ${
                Formatter.formatFileSize(
                    baseContext,
                    bitmap.allocationByteCount.toLong()
                )
            }"
        )
        Log.d(getLogTag() + " " + tag, "byteCount = ${bitmap.byteCount}")
    }
}