package com.imdevil.playground.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.imdevil.playground.R

class CustomDrawView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.niubi)
    private var paint = Paint()
    private var camera: Camera = Camera()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        camera.save()
        camera.rotateX(30f)
        canvas.translate(bitmap.width / 2f, bitmap.height / 2f)
        camera.applyToCanvas(canvas)
        canvas.translate(-bitmap.width / 2f, -bitmap.height / 2f)
        camera.restore()
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restore()
    }
}