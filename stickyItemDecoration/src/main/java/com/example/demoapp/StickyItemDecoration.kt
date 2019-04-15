package com.example.demoapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class StickyItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    /**
     * 分割线颜色
     */
    var dividerColor = Color.parseColor("#E8E8E8")
    /**
     * 分割线高度
     */
    var dividerHeight = context.dpToPixel(1)
    /**
     * sticky item高度
     */
    var stickyItemHeight = context.dpToPixel(30)
    /**
     * 绘制sticky
     */
    private val stickyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFE8E8E8.toInt()
    }
    /**
     * 绘制sticky文本
     */
    private val stickyTextPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).also {
        it.color = 0xFF999999.toInt()
        it.textSize = context.spToPx(16F)
    }
    /**
     * sticky文本的rect
     */
    private val stickyTextRect = Rect()
    /**
     * 分割线画笔
     */
    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = dividerColor
    }
    /**
     * 获取sticky文本
     */
    private var textCallback: DividerTextCallback? = null


    init {

    }


    /**
     * 这个方法会在RecyclerView绘制itemView之前调用，并且绘制的内容会在itemView下方。
     * @param c Canvas to draw into
     * @param parent RecyclerView this ItemDecoration is drawing into
     * @param state The current state of RecyclerView
     */
    override fun onDraw(c: Canvas?, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val itemView = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(itemView)
            if (isFirstItemInGroup(position)) {
                val top = itemView.top - stickyItemHeight
                val bottom = itemView.top
                c?.drawRect(
                    0F, top.toFloat(), itemView.right.toFloat(),
                    bottom.toFloat(), stickyPaint
                )
                drawStickyText(textCallback?.getDividerText(position), c, top, bottom)
            } else {
                c?.drawRect(
                    0F, (itemView.top - dividerHeight).toFloat(), itemView.right.toFloat(),
                    itemView.top.toFloat(), dividerPaint
                )
            }
        }

    }

    /**
     * 这个方法会在RecyclerView绘制itemView之后调用，并且绘制的内容会覆盖在itemView上。
     * @param c Canvas to draw into
     * @param parent RecyclerView this ItemDecoration is drawing into
     * @param state The current state of RecyclerView.
     */
    override fun onDrawOver(c: Canvas?, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)
        val childCount = parent.childCount

        if (childCount > 0) {
            //sticky效果其实就是处理第一个itemView，然后让悬浮内容置于第一个itemView之上。
            val firstView = parent.getChildAt(0)

            val position = parent.getChildAdapterPosition(firstView)
            val text = textCallback?.getDividerText(position)

            if (firstView.bottom <= stickyItemHeight && isFirstItemInGroup(position + 1)) {
                c?.drawRect(0F, 0F, firstView.width.toFloat(), firstView.bottom.toFloat(), stickyPaint)
                drawStickyText(text, c, firstView.bottom - stickyItemHeight, firstView.bottom)
            } else {
                c?.drawRect(0F, 0F, firstView.width.toFloat(), stickyItemHeight.toFloat(), stickyPaint)
                drawStickyText(text, c, 0, stickyItemHeight)
            }
        }
    }

    /**
     * 获取每个itemView的偏移量
     * @param outRect Rect to receive the output.
     * @param view    The child view to decorate
     * @param parent  RecyclerView this ItemDecoration is decorating
     * @param state   The current state of RecyclerView.
     */
    override fun getItemOffsets(outRect: Rect?, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (isFirstItemInGroup(position)) {
            outRect?.top = stickyItemHeight
        } else {
            outRect?.top = dividerHeight
        }
    }

    /**
     * 设置分组内容回调
     */
    fun setDividerTextCallback(action: (position: Int) -> String) {
        textCallback = object : DividerTextCallback {
            override fun getDividerText(position: Int): String = action(position)
        }
    }

    /**
     * 是否是分组的第一个item
     */
    private fun isFirstItemInGroup(position: Int): Boolean {
        return if (position == 0) {
            true
        } else {
            val currentText = textCallback?.getDividerText(position)
            val previewText = textCallback?.getDividerText(position - 1)
            currentText != previewText
        }
    }

    /**
     * 绘制sticky内容
     */
    private fun drawStickyText(text: String?, c: Canvas?, top: Int, bottom: Int) {
        stickyTextRect.left = context.dpToPixel(10)
        stickyTextRect.top = top
        stickyTextRect.right = stickyTextPaint.measureText(text).toInt()
        stickyTextRect.bottom = bottom
        val fontMetrics = stickyTextPaint.fontMetricsInt
        val baseline = (stickyTextRect.bottom + stickyTextRect.top - fontMetrics.bottom - fontMetrics.top) / 2
        c?.drawText(text, stickyTextRect.left.toFloat(), baseline.toFloat(), stickyTextPaint)
    }

    /**
     * 分组text
     */
    interface DividerTextCallback {
        /**
         * 获取分组text
         */
        fun getDividerText(position: Int): String
    }
}
