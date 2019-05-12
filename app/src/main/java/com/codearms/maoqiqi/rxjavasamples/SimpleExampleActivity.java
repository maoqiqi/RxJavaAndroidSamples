package com.codearms.maoqiqi.rxjavasamples;

import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Simple example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 14:45
 */
public class SimpleExampleActivity extends ExampleActivity {

    @Override
    protected String getTitleText() {
        return "SimpleExample";
    }

    @Override
    protected void doSomeWork() {
        getObservable()
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        doSomeLongOperation();
                        return s;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<String> getObservable() {
        return Observable.just("one", "two", "three", "four", "five");
    }

    private Observer<String> getObserver() {
        return new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed() + getThreadInfo());
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext -> value -> " + s + getThreadInfo());
                textView.append("onNext -> value -> " + s);
                textView.append(Constant.LINE_SEPARATOR);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError -> " + e.getMessage() + getThreadInfo());
                textView.append("onError -> " + e.getMessage());
                textView.append(Constant.LINE_SEPARATOR);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete" + getThreadInfo());
                textView.append("onComplete");
                textView.append(Constant.LINE_SEPARATOR);
            }
        };
    }
}