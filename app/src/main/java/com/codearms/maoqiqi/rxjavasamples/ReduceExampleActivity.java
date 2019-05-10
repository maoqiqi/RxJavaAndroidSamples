package com.codearms.maoqiqi.rxjavasamples;

import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Reduce example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 17:21
 */
public class ReduceExampleActivity extends ExampleActivity {

    @Override
    protected String getTitleText() {
        return "ReduceExample";
    }

    // 使所有数字相加
    @Override
    protected void doSomeWork() {
        Observable.just(1, 2, 3, 4)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) {
                        return integer + integer2;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private MaybeObserver<Integer> getObserver() {
        return new MaybeObserver<Integer>() {

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

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
                textView.append("onComplete");
                textView.append(Constant.LINE_SEPARATOR);
            }
        };
    }
}