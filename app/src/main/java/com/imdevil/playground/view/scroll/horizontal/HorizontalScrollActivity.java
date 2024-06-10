package com.imdevil.playground.view.scroll.horizontal;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.imdevil.playground.R;

public class HorizontalScrollActivity extends AppCompatActivity {

    private MyHorizontalScrollView mHsv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_scroll);

        mHsv = findViewById(R.id.my_horizontal_scroll_view);

        findViewById(R.id.smooth_scroll).setOnClickListener(v -> {
            mHsv.smoothScrollBy(200, 0);
        });
    }
}
