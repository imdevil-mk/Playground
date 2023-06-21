package com.imdevil.playground.base

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

abstract class LogActivity : AppCompatActivity() {

    open fun getLogTag(): String {
        return this.javaClass.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(getLogTag(), "onCreate: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(getLogTag(), "onRestart: ")
    }

    override fun onStart() {
        super.onStart()
        Log.d(getLogTag(), "onStart: ")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(getLogTag(), "onRestoreInstanceState: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(getLogTag(), "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(getLogTag(), "onPause: ")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(getLogTag(), "onSaveInstanceState: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(getLogTag(), "onStop: ")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(getLogTag(), "onConfigurationChanged: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(getLogTag(), "onDestroy: ")
    }
}