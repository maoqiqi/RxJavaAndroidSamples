package com.codearms.maoqiqi.rxjavasamples;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Buffer example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 17:30
 */
public class BufferExampleActivity extends BaseActivity {

    private static final String TAG = BufferExampleActivity.class.getSimpleName();

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("BufferExample");
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSomeWork();
            }
        });
        textView = findViewById(R.id.textView);
    }

    // 将所有发出的值捆绑到一个列表中
    private void doSomeWork() {
        // 3:它从它的开始索引和创建列表中最多取三个
        // 1:每次跳一步
        // 结果：
        // 第1次:one, two, three
        // 第2次:two, three, four
        // 第3次:three, four, five
        // 第4次:four, five
        // 第5次:five
        getObservable()
                .buffer(3, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<String> getObservable() {
        return Observable.just("one", "two", "three", "four", "five");
    }

    private Observer<List<String>> getObserver() {
        return new Observer<List<String>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onNext(List<String> strings) {
                Log.d(TAG, "onNext -> value.size() -> " + strings.size());
                textView.append("onNext -> value.size() -> " + strings.size());
                textView.append(Constant.LINE_SEPARATOR);
                for (int i = 0; i < strings.size(); i++) {
                    textView.append(" -> " + strings.get(i));
                    textView.append(Constant.LINE_SEPARATOR);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError -> " + e.getMessage());
                textView.append("onError -> " + e.getMessage());
                textView.append(Constant.LINE_SEPARATOR);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
                textView.append("onComplete");
                textView.append(Constant.LINE_SEPARATOR);
            }
        };
    }
}