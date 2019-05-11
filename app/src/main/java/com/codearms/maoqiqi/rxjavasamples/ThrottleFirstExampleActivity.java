package com.codearms.maoqiqi.rxjavasamples;

import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * ThrottleFirst example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/11 15:00
 */
public class ThrottleFirstExampleActivity extends ExampleActivity {

    @Override
    protected String getTitleText() {
        return "ThrottleFirstExample";
    }

    // 如果观察对象自上次采样以来没有发出任何项,则该操作符产生的观察对象在该采样期间不会发出任何项。
    @Override
    protected void doSomeWork() {
        getObservable()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<Integer> getObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 发送模拟等待时间的事件
                Thread.sleep(0);
                emitter.onNext(1); // deliver
                emitter.onNext(2); // skip

                Thread.sleep(505);
                emitter.onNext(3); // deliver

                Thread.sleep(99);
                emitter.onNext(4); // skip

                Thread.sleep(100);
                emitter.onNext(5); // skip
                emitter.onNext(6); // skip

                Thread.sleep(305);
                emitter.onNext(7); // deliver

                Thread.sleep(510);
                emitter.onComplete();
            }
        });
    }

    private Observer<Integer> getObserver() {
        return new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext -> value -> " + integer);
                textView.append("onNext -> value -> " + integer);
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