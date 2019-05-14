package com.codearms.maoqiqi.rxjavasamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

public class CombineActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = TransformActivity.class.getSimpleName();

    private int[] ids = {R.id.btn_start_with, R.id.btn_merge, R.id.btn_zip, R.id.btn_combine_latest,
            R.id.btn_join, R.id.btn_switch_on_next};

    protected TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("组合操作");
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
            case R.id.btn_start_with:
                break;
            case R.id.btn_merge:
                break;
            case R.id.btn_zip:
                break;
            case R.id.btn_combine_latest:
                break;
            case R.id.btn_join:
                break;
            case R.id.btn_switch_on_next:
                break;
        }
    }
}