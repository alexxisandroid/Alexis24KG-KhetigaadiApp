package com.khetigaadi.commons
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
class CustomTypefaceSpan(private val typeface:Typeface):MetricAffectingSpan() {
    override fun updateDrawState(tp:TextPaint) {
        applyCustomTypeFace(tp,typeface)
    }
    override fun updateMeasureState(tp:TextPaint) {
        applyCustomTypeFace(tp,typeface)
    }
    private fun applyCustomTypeFace(paint:Paint,tf:Typeface) {
        paint.typeface = tf
    }
}
