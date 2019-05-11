package com.codearms.maoqiqi.rxjavasamples;

import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.subjects.PublishSubject;

/**
 * ReplayExample example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 21:24
 */
public class ReplayExampleActivity extends ExampleActivity {

    @Override
    protected String getTitleText() {
        return "ReplayExample";
    }

    // 确保所有观察者都能看到相同的已发射项序列,即使他们是在可观察对象开始发射项之后订阅的.
    @Override
    protected void doSomeWork() {
        PublishSubject<Integer> source = PublishSubject.create();
        // bufferSize = 3 保留要replay的3个值
        ConnectableObservable<Integer> observable = source.replay(3);
        observable.connect();

        observable.subscribe(getObserver1());

        source.onNext(1);
        source.onNext(2);
        source.onNext(3);
        source.onNext(4);
        source.onComplete();

        // 它将发出2、3、4(count = 3),保留3个值以便replay
        observable.subscribe(getObserver2());
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