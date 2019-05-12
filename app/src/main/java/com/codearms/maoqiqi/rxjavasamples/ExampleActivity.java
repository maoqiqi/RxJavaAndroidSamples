package com.codearms.maoqiqi.rxjavasamples;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Base example activity
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 15:10
 */
public abstract class ExampleActivity extends BaseActivity {

    protected static final String TAG = SimpleExampleActivity.class.getSimpleName();

    protected TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle(getTitleText());
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSomeWork();
            }
        });
        textView = findViewById(R.id.textView);
    }

    protected abstract String getTitleText();

    protected abstract void doSomeWork();

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    protected String getThreadInfo() {
        return isMainThread() ? " --------> [Main thread]" : " --------> [Not main thread]";
    }

    protected void doSomeLongOperation() {
        Log.d(TAG, "Do Some Long Operation Start:" + getThreadInfo());
        try {
            Thread.sleep(3000);
            Log.d(TAG, "Do Some Long Operation End.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "Operation was interrupted:" + e.getMessage() + getThreadInfo());
        }
    }
}