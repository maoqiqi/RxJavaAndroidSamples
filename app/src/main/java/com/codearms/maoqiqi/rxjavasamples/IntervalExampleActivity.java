package com.codearms.maoqiqi.rxjavasamples;

import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Interval example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 16:52
 */
public class IntervalExampleActivity extends ExampleActivity {

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected String getTitleText() {
        return "IntervalExample";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    // 以2秒的间隔运行任务,并立即启动
    @Override
    protected void doSomeWork() {
        disposables.add(getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    private Observable<Long> getObservable() {
        return Observable.interval(0, 2, TimeUnit.SECONDS);
    }

    private DisposableObserver<Long> getObserver() {
        return new DisposableObserver<Long>() {

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "onNext -> value -> " + aLong);
                textView.append("onNext -> value -> " + aLong);
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