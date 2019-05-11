package com.codearms.maoqiqi.rxjavasamples;

import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.ReplaySubject;

/**
 * ReplaySubjectExample example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 22:30
 */
public class ReplaySubjectExampleActivity extends ExampleActivity {

    @Override
    protected String getTitleText() {
        return "ReplaySubjectExample";
    }

    // ReplaySubject向任何观察者发出可观察对象发出的所有项,而不考虑观察者订阅的时间。
    @Override
    protected void doSomeWork() {
        ReplaySubject<Integer> source = ReplaySubject.create();

        source.subscribe(getObserver1());

        source.onNext(1);
        source.onNext(2);
        source.onNext(3);
        source.onNext(4);
        source.onComplete();

        // 它将发射1,2,3,4给第二个观察者,就像我们使用replay一样
        source.subscribe(getObserver2());
    }

    private Observer<Integer> getObserver1() {
        return new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "First onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "First onNext -> value -> " + integer);
                textView.append("First onNext -> value -> " + integer);
                textView.append(Constant.LINE_SEPARATOR);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "First onError -> " + e.getMessage());
                textView.append("First onError -> " + e.getMessage());
                textView.append(Constant.LINE_SEPARATOR);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "First onComplete");
                textView.append("First onComplete");
                textView.append(Constant.LINE_SEPARATOR);
            }
        };
    }

    private Observer<Integer> getObserver2() {
        return new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "Second onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "Second onNext -> value -> " + integer);
                textView.append("Second onNext -> value -> " + integer);
                textView.append(Constant.LINE_SEPARATOR);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Second onError -> " + e.getMessage());
                textView.append("Second onError -> " + e.getMessage());
                textView.append(Constant.LINE_SEPARATOR);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Second onComplete");
                textView.append("Second onComplete");
                textView.append(Constant.LINE_SEPARATOR);
            }
        };
    }
}