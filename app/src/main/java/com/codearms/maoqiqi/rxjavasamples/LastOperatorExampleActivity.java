package com.codearms.maoqiqi.rxjavasamples;

import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * LastOperator example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 22:25
 */
public class LastOperatorExampleActivity extends ExampleActivity {

    @Override
    protected String getTitleText() {
        return "LastOperatorExample";
    }

    // 只发出可观察对象发出的最后一项
    @Override
    protected void doSomeWork() {
        Observable.just("A1", "A2", "A3", "A4", "A5", "A6")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // 如果ObservableSource为空,则发出默认项("A1")
                .last("A1")
                .subscribe(getSingleObserver());
    }

    private SingleObserver<String> getSingleObserver() {
        return new SingleObserver<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "onSuccess -> value -> " + s);
                textView.append("onSuccess -> value -> " + s);
                textView.append(Constant.LINE_SEPARATOR);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError -> " + e.getMessage());
                textView.append("onError -> " + e.getMessage());
                textView.append(Constant.LINE_SEPARATOR);
            }
        };
    }
}