package com.codearms.maoqiqi.rxjavasamples;

import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * BehaviorSubject example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 22:42
 */
public class BehaviorSubjectExampleActivity extends ExampleActivity {

    @Override
    protected String getTitleText() {
        return "BehaviorSubjectExample";
    }

    // 当观察者订阅行为主体时，它首先发出源可观察对象最近发出的项(或者种子/默认值,如果还没有发出),然后继续发出源可观察对象稍后发出的任何其他项。
    // 它不同于AsyncSubject,因为Async会发出最后一个值(而且只有最后一个值)，但是BehaviorSubject会发出最后一个值和随后的值。
    @Override
    protected void doSomeWork() {
        BehaviorSubject<Integer> source = BehaviorSubject.create();

        source.subscribe(getObserver1());

        source.onNext(1);
        source.onNext(2);
        source.onNext(3);

        // 它将发出3个(最后发出的)、4个和onComplete给第二个观察者。
        source.subscribe(getObserver2());

        source.onNext(4);
        source.onComplete();
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