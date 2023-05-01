package com.imdevil.playground.base

import android.app.Activity
import android.content.Intent
import android.widget.Button


fun Activity.setupButtonClick(id: Int, target: Class<*>) {
    findViewById<Button>(id).setOnClickListener {
        startActivity(Intent(this, target))
    }
}