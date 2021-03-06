package com.codearms.maoqiqi.rxjavasamples;

import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Flowable example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 17:12
 */
public class FlowableExampleActivity extends ExampleActivity {

    @Override
    protected String getTitleText() {
        return "FlowableExample";
    }

    // 使用Flowable
    @Override
    protected void doSomeWork() {
        Flowable.just(1, 2, 3, 4)
                .reduce(50, new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) {
                        return integer + integer2;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSingleObserver());
    }

    private SingleObserver<Integer> getSingleObserver() {
        return new SingleObserver<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onSuccess(Integer integer) {
                Log.d(TAG, "onSuccess -> value -> " + integer);
                textView.append("onSuccess -> value -> " + integer);
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