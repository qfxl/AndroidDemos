package com.example.demoapp

import android.content.Context
import android.util.Log
import android.util.TypedValue

fun Context.dpToPixel(dp: Int): Int {
    val displayMetrics = resources.displayMetrics
    return if (dp < 0) dp else Math.round(dp * displayMetrics.density)
}

/**
 * sp转px
 * @param sp
 */
fun Context.spToPx(sp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, this.resources.displayMetrics)

fun String.log() {
    Log.i("qfxl", this)
}