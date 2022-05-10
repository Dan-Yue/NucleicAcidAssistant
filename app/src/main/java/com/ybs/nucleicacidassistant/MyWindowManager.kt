package com.ybs.nucleicacidassistant

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.util.Log
import android.view.Gravity
import android.view.WindowManager


/**
 * Created by DanYue on 2022/5/9 15:23.
 */
internal object MyWindowManager {
    /**
     * 悬浮窗View的实例
     */
    private var windowView: MyWindowView? = null

    /**
     * 悬浮窗View的参数
     */
    private var windowViewParams: WindowManager.LayoutParams? = null

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private var mWindowManager: WindowManager? = null

    /**
     * 创建一个悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    @SuppressLint("RtlHardcoded")
    fun createWindowView(context: Context) {
        val windowManager = getWindowManager(context)
        if (windowView == null) {
            windowView = MyWindowView(context)
            if (windowViewParams == null) {
                val flags =
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                windowViewParams = WindowManager.LayoutParams()
                windowViewParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                windowViewParams!!.format = PixelFormat.RGBA_8888
                windowViewParams!!.flags = flags
                windowViewParams!!.gravity = Gravity.LEFT or Gravity.TOP
                windowViewParams!!.width = MyWindowView.viewWidth
                windowViewParams!!.height = MyWindowView.viewHeight
                windowViewParams!!.x = 0
                windowViewParams!!.y = 0
            }
            windowView!!.setParams(windowViewParams)
            windowManager!!.addView(windowView, windowViewParams)
        }
    }

    /**
     * 将悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    fun removeWindowView(context: Context) {
        if (windowView != null) {
            val windowManager = getWindowManager(context)
            windowManager!!.removeView(windowView)
            windowView = null
        }
    }

    /**
     * 悬浮框显示为透明
     *
     * @param xy 存储SharedPreferences
     */
    fun transparentWindowView(xy: SharedPreferences) {
        if (windowView != null) {
            windowView!!.setTransparent()
            val x = xy.getInt("x", 0)
            val y = xy.getInt("y", 0)
            windowView!!.setViewPosition(x, y)
        }
    }

    /**
     * 显示有背景的悬浮框
     *
     * @param xy 存储SharedPreferences
     */
    fun showWindowView(xy: SharedPreferences) {
        if (windowView != null) {
            windowView!!.setImageBack()
            val x = xy.getInt("x", 0)
            val y = xy.getInt("y", 0)
            windowView!!.setViewPosition(x, y)
        }
    }

    /**
     * 设置悬浮框可以移动
     * @param xy 存储SharedPreferences
     */
    fun setAction(xy: SharedPreferences) {
        if (windowView != null) {
            windowView!!.setAction(true)
            xy.edit().putInt("x", 0).putInt("y", 0).apply()
        }
    }

    /**
     * 设置悬浮框固定
     *
     * @param xy 存储SharedPreferences
     */
    fun setFixed(xy: SharedPreferences) {
        if (windowView != null) {
            windowView!!.setAction(false)
            val x = windowView!!.getFixedX()
            val y = windowView!!.getFixedY()
            Log.d("-windowView-", "x = $x, y = $y")
            xy.edit().putInt("x", x).putInt("y", y).apply()
        }
    }

    /**
     * 设置坐标
     *
     * @param x 横坐标
     * @param y 纵坐标
     */
    fun setXY(x: Int, y: Int) {
        if (windowView != null) {
            windowView!!.setViewPosition(x, y)
            windowView!!.setAction(false)
        }
    }

    /**
     * 是否有悬浮窗显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    fun isWindowShowing(): Boolean {
        return windowView != null
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private fun getWindowManager(context: Context): WindowManager? {
        if (mWindowManager == null) {
            mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
        return mWindowManager
    }
}
