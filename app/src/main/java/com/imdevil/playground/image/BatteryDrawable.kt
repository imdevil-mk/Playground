package com.imdevil.playground.image

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlin.properties.Delegates

class BatteryDrawable : Drawable() {

    private lateinit var batteryBitmap: Bitmap
    private var mBatteryWidth by Delegates.notNull<Int>()
    private var mBatteryHeight by Delegates.notNull<Int>()
    private val batteryPaint: Paint = Paint()
    private lateinit var batteryShader: Shader

    private lateinit var highlightShader: Shader
    private val highlightMatrix = Matrix()

    private lateinit var composeShader: Shader
    private lateinit var composePaint: Paint

    @RequiresApi(Build.VERSION_CODES.Q)
    fun computeBattery(res: Resources, imgId: Int) {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        batteryBitmap = BitmapFactory.decodeResource(res, imgId, options)
        mBatteryWidth = options.outWidth
        mBatteryHeight = options.outHeight
        batteryShader = BitmapShader(batteryBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        Log.d(TAG, "computeBattery: width = $mBatteryWidth, height = $mBatteryHeight")

        val colors = arrayOf(
            0x00ffffff.toInt(),
            0x00ffffff.toInt(),
            0xAcffffff.toInt(),
            0x00ffffff.toInt(),
            0x00ffffff.toInt(),
        )

        /*        val colors = arrayOf(
                    0xFFE91E63.toInt(),
                    0xFF2196F3.toInt(),
                    0xFF2196F3.toInt(),
                    0xFFE91E63.toInt(),
                )*/

        /*        val positions = arrayOf(
                    0.25f, 0.4995f, 0.5005f, 0.75f
                )  */
        val edge = 0.2f
        val soc = 0.7f
        val positions = arrayOf(
            0.0f, soc - edge, soc, soc, 1.0f
        )

        highlightShader = LinearGradient(
            0f,
            0f,
            mBatteryWidth.toFloat(),
            0f,
            colors.toIntArray(),
            positions.toFloatArray(),
            Shader.TileMode.CLAMP
        )

        composeShader = ComposeShader(batteryShader, highlightShader, PorterDuff.Mode.DST_IN)
        composePaint = Paint()
        composePaint.shader = composeShader
    }


    override fun draw(canvas: Canvas) {
        Log.d(TAG, "draw: bounds = $bounds")
        //canvas.drawBitmap(batteryBitmap, bounds.left.toFloat(), bounds.top.toFloat(), batteryPaint)

        // compose draw
        //canvas.drawRect(bounds, composePaint)

        batteryPaint.reset()
        canvas.drawBitmap(batteryBitmap, bounds.left.toFloat(), bounds.top.toFloat(), batteryPaint)
        batteryPaint.shader = highlightShader
        batteryPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        highlightMatrix.reset()
        highlightMatrix.setRotate(10f, bounds.right / 2f, bounds.bottom / 2f)
        batteryPaint.shader.setLocalMatrix(highlightMatrix)
        canvas.drawRect(bounds, batteryPaint)

    }

    override fun setAlpha(alpha: Int) {
        // no-op
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // no-op
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
        Log.d(TAG, "setBounds: $bounds")
    }

    companion object {
        private const val TAG = "BatteryDrawable"
    }
}