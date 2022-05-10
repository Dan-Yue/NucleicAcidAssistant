package com.ybs.nucleicacidassistant

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout

/**
 * Created by DanYue on 2022/5/9 15:22.
 */
internal class MyWindowView(context: Context) :
    LinearLayout(context) {
    /**
     * 用于更新悬浮窗的位置
     */
    private val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    /**
     * 悬浮窗的参数
     */
    private var mParams: WindowManager.LayoutParams? = null

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private var xInScreen = 0f

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private var yInScreen = 0f

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private var xDownInScreen = 0f

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private var yDownInScreen = 0f

    /**
     * 记录手指按下时在悬浮窗的View上的横坐标的值
     */
    private var xInView = 0f

    /**
     * 记录手指按下时在悬浮窗的View上的纵坐标的值
     */
    private var yInView = 0f

    /**
     * 背景是否是透明
     */
    private var isTransparent = false

    /**
     * 是否可移动
     */
    private var isAction = true
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.x
                yInView = event.y
                xDownInScreen = event.rawX
                yDownInScreen = event.rawY - getStatusBarHeight()
                xInScreen = event.rawX
                yInScreen = event.rawY - getStatusBarHeight()
            }
            MotionEvent.ACTION_MOVE -> {
                xInScreen = event.rawX
                yInScreen = event.rawY - getStatusBarHeight()
                // 手指移动的时候更新悬浮窗的位置
                updateViewPosition()
            }
            MotionEvent.ACTION_UP ->                 // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    changeWindow()
                }
            else -> {}
        }
        return true
    }

    /**
     * 将悬浮窗的参数传入，用于更新悬浮窗的位置。
     *
     * @param params 悬浮窗的参数
     */
    fun setParams(params: WindowManager.LayoutParams?) {
        mParams = params
    }

    /**
     * 更新悬浮窗在屏幕中的位置。
     */
    private fun updateViewPosition() {
        if (isAction) {
            mParams!!.x = (xInScreen - xInView).toInt()
            mParams!!.y = (yInScreen - yInView).toInt()
            windowManager.updateViewLayout(this, mParams)
        }
    }

    /**
     * 悬浮窗在屏幕中的X坐标位置
     *
     * @return X坐标
     */
    fun getFixedX(): Int {
        return (xInScreen - xInView).toInt()
    }


    /**
     * 悬浮窗在屏幕中的Y坐标位置
     *
     * @return Y坐标
     */
    fun getFixedY(): Int {
        return (yInScreen - yInView).toInt()
    }

    /**
     * 设置悬浮窗在屏幕中的位置
     *
     * @param x 横坐标
     * @param y 纵坐标
     */
    fun setViewPosition(x: Int, y: Int) {
        mParams!!.x = x
        mParams!!.y = y
        windowManager.updateViewLayout(this, mParams)
    }

    /**
     * 改变悬浮窗
     */
    private fun changeWindow() {
        if (isTransparent) {
            setImageBack()
        } else {
            setTransparent()
        }
    }

    /**
     * 背景设置为透明
     */
    fun setTransparent() {
        isTransparent = true
        val view = findViewById<View>(R.id.logo)
        view.setBackgroundResource(R.mipmap.transparent)
    }

    /**
     * 背景设置为图片
     */
    fun setImageBack() {
        isTransparent = false
        val view = findViewById<View>(R.id.logo)
        view.setBackgroundResource(R.mipmap.img)
    }

    /**
     * 设置是否允许移动
     *
     * @param isActionSet 是否允许移动
     */
    fun setAction(isActionSet: Boolean) {
        isAction = isActionSet
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private fun getStatusBarHeight(): Int {
        if (statusBarHeight == 0) {
            try {
                val c = Class.forName("com.android.internal.R\$dimen")
                val o = c.newInstance()
                val field = c.getField("status_bar_height")
                val x = field[o] as Int
                statusBarHeight = resources.getDimensionPixelSize(x)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return statusBarHeight
    }

    companion object {
        /**
         * 记录悬浮窗的宽度
         */
        var viewWidth: Int = 0

        /**
         * 记录悬浮窗的高度
         */
        var viewHeight: Int = 0

        /**
         * 记录系统状态栏的高度
         */
        private var statusBarHeight = 0
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.window_view, this)
        val view = findViewById<View>(R.id.window_layout)
        viewWidth = view.layoutParams.width
        viewHeight = view.layoutParams.height
    }
}