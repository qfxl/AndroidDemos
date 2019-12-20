package com.github.customview.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import java.util.*
import kotlin.math.roundToInt

/**
 *
 * @ProjectName: AndroidDemos
 * @Package: com.github.customview.views
 * @ClassName: ClockView
 * @Description: 练练手
 * @Author: 清风徐来
 * @CreateDate: 2019/12/18 16:28
 * @UpdateUser: 清风徐来
 * @UpdateDate: 2019/12/18 16:28
 * @UpdateRemark:
 * @Version: 1.0
 */
class ClockView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    private val centerX by lazy {
        (width shr 1).toFloat()
    }

    private val centerY by lazy {
        (height shr 1).toFloat()
    }
    private val textPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.WHITE
            textSize = spToPx(16f)
            textAlign = Paint.Align.CENTER
        }
    }

    private var currentTime = "00:00"
    private var currentDate = "00.00 星期一"
    /**
     * hour
     */
    private var hour = 24
    /**
     * minute
     */
    private var minute = 59
    /**
     * seconds
     */
    private var seconds = 59
    /**
     * hour draw x position
     */
    private var hourX = 0f
    /**
     * minute draw x position
     */
    private var minuteX = 0f
    /**
     * seconds draw x position
     */
    private var secondsX = 0f
    /**
     * hour、seconds、minutes degree
     */
    private var secondsDegree = 0f
    private var minutesDegree = 0f
    private var hourDegree = 0f

    private var mAnimator = ValueAnimator.ofFloat(0f, 6f).apply {
        interpolator = LinearInterpolator()
        duration = 400
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        refresh()
    }

    /**
     * 刷新时间
     */
    private fun refresh() {
        Calendar.getInstance().run {
            hour = get(Calendar.HOUR_OF_DAY)
            minute = get(Calendar.MINUTE)
            seconds = get(Calendar.SECOND)
            val hourStr = if (hour < 10) {
                "0$hour"
            } else {
                "$hour"
            }
            val minuteStr = if (minute < 10) {
                "0$minute"
            } else {
                "$minute"
            }
            currentTime = "${hourStr}:${minuteStr}"
            val month = get(Calendar.MONTH) - 1
            val day = get(Calendar.DAY_OF_MONTH)
            val monthStr = if (month < 10) {
                "0$month"
            } else {
                "$month"
            }
            val dayStr = if (day < 10) {
                "0$day"
            } else {
                "$day"
            }
            val dayOfWeekStr = when (get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY -> "星期日"
                Calendar.MONDAY -> "星期一"
                Calendar.TUESDAY -> "星期二"
                Calendar.WEDNESDAY -> "星期三"
                Calendar.THURSDAY -> "星期四"
                Calendar.FRIDAY -> "星期五"
                Calendar.SATURDAY -> "星期六"
                else -> "未知"
            }
            currentDate = "${monthStr}月${dayStr}日 $dayOfWeekStr"
        }

        secondsDegree = -360 / 60f * (seconds - 1)
        if (seconds != 0) {
            minutesDegree = -360 / 60f * minute
        }
        if (seconds != 0 && minute != 0) {
            hourDegree = -360 / 24f * hour
        }
        mAnimator.removeAllUpdateListeners()
        val hd = hourDegree
        val md = minutesDegree
        val sd = secondsDegree
        mAnimator.addUpdateListener { v ->
            val value = v.animatedValue as Float
            if (seconds == 0) {
                minutesDegree = md - value
            }
            if (seconds == 0 && minute == 0) {
                hourDegree = hd - value * 60 / 24
            }
            secondsDegree = sd - value
            invalidate()
        }
        postDelayed({
            refresh()
        }, 1000)
        mAnimator.start()
    }

    private val currentTimeRect by lazy {
        val hourBounds = Rect()
        textPaint.getTextBounds("00时", 0, "00时".length, hourBounds)
        RectF(
            hourX - dpToPx(5),
            centerY - hourBounds.height(),
            secondsX + textPaint.measureText("00秒") + dpToPx(5),
            centerY + textPaint.descent()
        )
    }

    private val boundsPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = dpToPx(1)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val itemMaxWidth = width / 2 / 3
        hourX = centerX + itemMaxWidth + textPaint.measureText("00时")
        minuteX = centerX + itemMaxWidth * 2
        secondsX = centerX + itemMaxWidth * 3 - textPaint.measureText("00秒")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制中间文本
        drawCenterText(canvas)
        //绘制内圈时
        drawHour(canvas)
        //绘制内圈分
        drawMinute(canvas)
        //绘制内圈秒
        drawSeconds(canvas)
        //绘制当前时间框
        canvas?.drawRoundRect(currentTimeRect, dpToPx(5), dpToPx(5), boundsPaint)
    }

    /**
     * 绘制时
     * @param canvas
     */
    private fun drawHour(canvas: Canvas?) {
        canvas?.run {
            save()
            rotate(hourDegree, centerX, centerY)
            for (h in 0 until 24) {
                save()
                rotate(360 / 24f * h, centerX, centerY)
                val hourText = if (h < 10) {
                    "0${h}时"
                } else {
                    "${h}时"
                }
                drawText(hourText, hourX, centerY, textPaint)
                restore()
            }
            restore()
        }
    }

    /**
     * 绘制分钟
     * @param canvas
     */
    private fun drawMinute(canvas: Canvas?) {
        canvas?.run {
            save()
            rotate(minutesDegree, centerX, centerY)
            for (min in 0 until 60) {
                save()
                rotate(360 / 60f * min, centerX, centerY)
                val minText = if (min < 10) {
                    "0${min}分"
                } else {
                    "${min}分"
                }
                drawText(minText, minuteX, centerY, textPaint)
                restore()
            }
            restore()
        }
    }

    /**
     * 绘制秒
     * @param canvas
     */
    private fun drawSeconds(canvas: Canvas?) {
        canvas?.run {
            save()
            rotate(secondsDegree, centerX, centerY)
            for (sec in 0 until 60) {
                save()
                rotate(360 / 60f * sec, centerX, centerY)
                val secText = if (sec < 10) {
                    "0${sec}秒"
                } else {
                    "${sec}秒"
                }
                drawText(secText, secondsX, centerY, textPaint)
                restore()
            }
            restore()
        }
    }

    /**
     * 绘制中间内容
     * @param canvas
     */
    private fun drawCenterText(canvas: Canvas?) {
        textPaint.apply {
            textSize = spToPx(16f)
            textAlign = Paint.Align.CENTER
        }
        canvas?.run {
            drawText(
                currentTime,
                centerX,
                centerY + textPaint.descent() + textPaint.ascent(),
                textPaint
            )
            textPaint.textSize = spToPx(14f)
            drawText(
                currentDate,
                centerX,
                centerY - textPaint.descent() - textPaint.ascent(),
                textPaint
            )
            textPaint.apply {
                textSize = spToPx(12f)
                textAlign = Paint.Align.LEFT
            }
        }
    }

    /**
     * sp to px
     */
    private fun spToPx(sp: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, this.resources.displayMetrics)

    /**
     * dp to px
     */
    private fun dpToPx(dp: Int): Float {
        val displayMetrics = this.resources.displayMetrics
        return if (dp < 0) dp.toFloat() else dp * displayMetrics.density
    }
}
