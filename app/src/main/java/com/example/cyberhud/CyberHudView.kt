package com.example.cyberhud

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class CyberHudView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val red = Color.rgb(255, 48, 83)
    private val cyan = Color.rgb(52, 231, 229)
    private val pale = Color.argb(220, 225, 238, 238)
    private val faintRed = Color.argb(90, 255, 48, 83)
    private val faintCyan = Color.argb(85, 52, 231, 229)

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        textSize = 24f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat().coerceAtLeast(1f)
        val h = height.toFloat().coerceAtLeast(1f)
        val t = SystemClock.elapsedRealtime() / 1000f

        drawCornerFrames(canvas, w, h)
        drawTopLeft(canvas, w, h, t)
        drawTopRight(canvas, w, h, t)
        drawCenterReticle(canvas, w, h, t)
        drawBottomLeft(canvas, w, h, t)
        drawRadar(canvas, w, h, t)
        drawScanLine(canvas, w, h, t)
        drawMicroGlitches(canvas, w, h, t)

        postInvalidateOnAnimation()
    }

    private fun drawCornerFrames(canvas: Canvas, w: Float, h: Float) {
        val m = w * 0.035f
        val x = w * 0.19f
        val y = h * 0.20f
        linePaint.color = faintRed
        linePaint.strokeWidth = 3f

        val p = Path()
        p.moveTo(m, y)
        p.lineTo(m, m)
        p.lineTo(x, m)
        p.moveTo(w - x, m)
        p.lineTo(w - m, m)
        p.lineTo(w - m, y)
        p.moveTo(m, h - y)
        p.lineTo(m, h - m)
        p.lineTo(x, h - m)
        p.moveTo(w - x, h - m)
        p.lineTo(w - m, h - m)
        p.lineTo(w - m, h - y)
        canvas.drawPath(p, linePaint)
    }

    private fun drawTopLeft(canvas: Canvas, w: Float, h: Float, t: Float) {
        val x = w * 0.055f
        var y = h * 0.095f
        textPaint.color = red
        textPaint.textSize = h * 0.034f
        canvas.drawText("OPTICAL HUD // ONLINE", x, y, textPaint)

        y += h * 0.038f
        textPaint.color = pale
        textPaint.textSize = h * 0.021f
        val tick = ((t * 10).toInt() % 1000).toString().padStart(3, '0')
        canvas.drawText("LINK  SECURE    GRID-09    T+$tick", x, y, textPaint)

        y += h * 0.030f
        linePaint.color = red
        linePaint.strokeWidth = 2f
        canvas.drawLine(x, y, x + w * 0.24f, y, linePaint)
        fillPaint.color = red
        canvas.drawRect(x, y - 3f, x + w * (0.12f + 0.03f * wave(t * 1.7f)), y + 3f, fillPaint)
    }

    private fun drawTopRight(canvas: Canvas, w: Float, h: Float, t: Float) {
        val right = w * 0.945f
        var y = h * 0.095f
        textPaint.textAlign = Paint.Align.RIGHT
        textPaint.color = cyan
        textPaint.textSize = h * 0.029f
        canvas.drawText("SYS // NOMINAL", right, y, textPaint)

        y += h * 0.035f
        textPaint.color = pale
        textPaint.textSize = h * 0.020f
        canvas.drawText("BATT 87%   NET 5G   TEMP 36.4", right, y, textPaint)

        y += h * 0.030f
        linePaint.color = faintCyan
        canvas.drawLine(right - w * 0.22f, y, right, y, linePaint)
        fillPaint.color = cyan
        val pulse = 0.16f + 0.03f * ((wave(t * 2.2f) + 1f) * 0.5f)
        canvas.drawRect(right - w * pulse, y - 3f, right, y + 3f, fillPaint)
        textPaint.textAlign = Paint.Align.LEFT
    }

    private fun drawCenterReticle(canvas: Canvas, w: Float, h: Float, t: Float) {
        val cx = w * 0.5f
        val cy = h * 0.5f
        val r = h * (0.025f + 0.002f * wave(t * 2f))

        linePaint.color = red
        linePaint.strokeWidth = 2.5f
        canvas.drawCircle(cx, cy, r, linePaint)
        canvas.drawLine(cx - r * 2.2f, cy, cx - r * 1.2f, cy, linePaint)
        canvas.drawLine(cx + r * 1.2f, cy, cx + r * 2.2f, cy, linePaint)
        canvas.drawLine(cx, cy - r * 2.2f, cx, cy - r * 1.2f, linePaint)
        canvas.drawLine(cx, cy + r * 1.2f, cx, cy + r * 2.2f, linePaint)

        fillPaint.color = cyan
        canvas.drawCircle(cx, cy, 3.5f, fillPaint)
    }

    private fun drawBottomLeft(canvas: Canvas, w: Float, h: Float, t: Float) {
        val x = w * 0.055f
        var y = h * 0.82f
        textPaint.color = red
        textPaint.textSize = h * 0.026f
        canvas.drawText("NEURAL LINK", x, y, textPaint)
        y += h * 0.035f
        textPaint.color = pale
        textPaint.textSize = h * 0.019f
        canvas.drawText("SYNC  99.2%", x, y, textPaint)
        y += h * 0.029f
        canvas.drawText("LAT   ${(8 + ((wave(t) + 1f) * 2f).toInt())} ms", x, y, textPaint)
        y += h * 0.029f
        canvas.drawText("CORE  STABLE", x, y, textPaint)
    }

    private fun drawRadar(canvas: Canvas, w: Float, h: Float, t: Float) {
        val cx = w * 0.86f
        val cy = h * 0.80f
        val r = h * 0.10f
        linePaint.strokeWidth = 2f
        linePaint.color = faintCyan
        canvas.drawCircle(cx, cy, r, linePaint)
        canvas.drawCircle(cx, cy, r * 0.66f, linePaint)
        canvas.drawCircle(cx, cy, r * 0.33f, linePaint)
        canvas.drawLine(cx - r, cy, cx + r, cy, linePaint)
        canvas.drawLine(cx, cy - r, cx, cy + r, linePaint)

        val angle = t * 1.6f
        val ex = cx + waveCos(angle) * r
        val ey = cy + wave(angle) * r
        linePaint.color = cyan
        linePaint.strokeWidth = 3f
        canvas.drawLine(cx, cy, ex, ey, linePaint)

        fillPaint.color = red
        canvas.drawCircle(cx + r * 0.34f, cy - r * 0.18f, 4f, fillPaint)
        canvas.drawCircle(cx - r * 0.45f, cy + r * 0.27f, 3f, fillPaint)
    }

    private fun drawScanLine(canvas: Canvas, w: Float, h: Float, t: Float) {
        val phase = (t % 5.5f) / 5.5f
        val y = h * (0.18f + phase * 0.64f)
        val scanPaint = Paint().apply {
            color = Color.argb(45, 255, 48, 83)
            strokeWidth = 2f
        }
        canvas.drawLine(w * 0.08f, y, w * 0.92f, y, scanPaint)
    }

    private fun drawMicroGlitches(canvas: Canvas, w: Float, h: Float, t: Float) {
        val slot = (t * 4f).toInt() % 17
        if (slot != 0) return
        fillPaint.color = Color.argb(70, 255, 48, 83)
        val y = h * 0.31f
        canvas.drawRect(w * 0.08f, y, w * 0.21f, y + 3f, fillPaint)
        fillPaint.color = Color.argb(55, 52, 231, 229)
        canvas.drawRect(w * 0.64f, y + 11f, w * 0.90f, y + 14f, fillPaint)
    }

    private fun wave(value: Float): Float = sin(value.toDouble()).toFloat()
    private fun waveCos(value: Float): Float = cos(value.toDouble()).toFloat()
}
