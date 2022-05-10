package com.ybs.nucleicacidassistant

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

const val REQUEST_CODE = 11

class MainActivity : AppCompatActivity() {
    private var type = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button1).setOnClickListener {
            type = 0
            checkSetting()
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            type = 1
            checkSetting()
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            MyWindowManager.removeWindowView(this)
        }
        findViewById<Button>(R.id.button4).setOnClickListener {
            val xy: SharedPreferences = this.getPreferences(Activity.MODE_PRIVATE)
            MyWindowManager.setFixed(xy)
        }
        findViewById<Button>(R.id.button5).setOnClickListener {
            val xy: SharedPreferences = this.getPreferences(Activity.MODE_PRIVATE)
            MyWindowManager.setAction(xy)
        }
        findViewById<Button>(R.id.button6).setOnClickListener {
            type = 2
            checkSetting()
        }
    }

    private fun checkSetting() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show()
            startActivityForResult(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                ), REQUEST_CODE
            )
        } else {
            when (type) {
                0 -> showTransparent()
                1 -> showView()
                2 -> showViewXY()
                else -> showTransparent()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show()
            } else {
                when (type) {
                    0 -> showTransparent()
                    1 -> showView()
                    2 -> showViewXY()
                    else -> showTransparent()
                }
            }
        }
    }

    private fun showTransparent() {
        val xy: SharedPreferences = this.getPreferences(Activity.MODE_PRIVATE)
        MyWindowManager.createWindowView(this)
        MyWindowManager.transparentWindowView(xy)
    }

    private fun showView() {
        val xy: SharedPreferences = this.getPreferences(Activity.MODE_PRIVATE)
        MyWindowManager.createWindowView(this)
        MyWindowManager.showWindowView(xy)
    }

    private fun showViewXY() {
        val xy: SharedPreferences = this.getPreferences(Activity.MODE_PRIVATE)
        MyWindowManager.createWindowView(this)
        MyWindowManager.setXY(0, 1200)
        MyWindowManager.transparentWindowView(xy)
    }
}