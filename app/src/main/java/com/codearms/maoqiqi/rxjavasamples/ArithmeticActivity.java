package com.codearms.maoqiqi.rxjavasamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

public class ArithmeticActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ArithmeticActivity.class.getSimpleName();

    private int[] ids = {};

    protected TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("错误处理");
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
        textView = findViewById(R.id.textView);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, Constant.LINE_DIVIDER);
        textView.append(Constant.LINE_DIVIDER);
        switch (v.getId()) {

        }
    }
}