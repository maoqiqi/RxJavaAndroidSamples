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
 * Debounce example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/11 15:27
 */
public class DebounceExampleActivity extends ExampleActivity {

    @Override
    protected String getTitleText() {
        return "DebounceExample";
    }

    // TODO: 2019-05-12 使用EditText每次输入变化做为例子
    // 只在一个特定的时间跨度已经过去而没有发出另一个项目的情况下,从一个可观察对象发出一个项目,所以它将发出我们已经模拟过的2,4,5。
    @Override
    protected void doSomeWork() {
        getObservable()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<Integer> getObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 发送模拟等待时间的事件
                emitter.onNext(1); // skip
                Thread.sleep(400);

                emitter.onNext(2); // deliver
                Thread.sleep(505);

                emitter.onNext(3); // skip
                Thread.sleep(100);

                emitter.onNext(4); // deliver
                Thread.sleep(605);

                emitter.onNext(5); // deliver
                Thread.sleep(510);
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