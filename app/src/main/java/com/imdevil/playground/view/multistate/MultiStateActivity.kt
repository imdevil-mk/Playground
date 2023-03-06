package com.imdevil.playground.view.multistate

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.imdevil.playground.R

class MultiStateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_state)


        val container = findViewById<MultiStateContainer>(R.id.state_container)

        val states = listOf(0, 1, 2, 3, 4)

        states.forEach { state ->
            container.addState(state, R.layout.state_text) {
                val textView = findViewById<TextView>(R.id.state_text)
                textView.text = "$state"
            }
        }

        val size = states.size
        var index = 0
        findViewById<Button>(R.id.change_state).setOnClickListener {
            val state = index % size
            index++
            container.moveToState(state)
        }
    }
}