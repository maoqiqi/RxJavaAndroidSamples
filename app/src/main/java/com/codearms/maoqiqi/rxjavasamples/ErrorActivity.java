package com.codearms.maoqiqi.rxjavasamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

/**
 * 用于对Observable发射的onError通知做出响应或者从错误中恢复
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/15 13:43
 */
public class ErrorActivity extends BaseActivity {

    private static final String TAG = ErrorActivity.class.getSimpleName();

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("错误处理");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.textView);
    }
}