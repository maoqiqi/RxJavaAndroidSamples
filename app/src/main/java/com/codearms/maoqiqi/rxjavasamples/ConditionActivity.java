package com.codearms.maoqiqi.rxjavasamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class ConditionActivity extends BaseActivity {

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