package com.codearms.maoqiqi.rxjavasamples;

import android.os.SystemClock;
import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Disposable example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 16:10
 */
public class DisposableExampleActivity extends ExampleActivity {

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected String getTitleText() {
        return "DisposableExample";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 活动被销毁后不要发送事件
        disposables.clear();
    }

    @Override
    protected void doSomeWork() {
        disposables.add(getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    private Observable<String> getObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() {
                Log.e(TAG, "sleep 2000");
                SystemClock.sleep(2000);
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }

    private DisposableObserver<String> getObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext -> value -> " + s);
                textView.append("onNext -> value -> " + s);
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