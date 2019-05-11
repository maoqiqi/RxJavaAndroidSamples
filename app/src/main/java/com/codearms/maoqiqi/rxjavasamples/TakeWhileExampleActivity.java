package com.codearms.maoqiqi.rxjavasamples;

import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * TakeWhile example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/11 16:00
 */
public class TakeWhileExampleActivity extends ExampleActivity {

    @Override
    protected String getTitleText() {
        return "TakeWhileExample";
    }

    @Override
    protected void doSomeWork() {
        getObservable()
                // 将项目发射延迟一秒
                .zipWith(Observable.interval(0, 1, TimeUnit.SECONDS),
                        new BiFunction<String, Long, String>() {
                            @Override
                            public String apply(String s, Long aLong) {
                                return s;
                            }
                        })
                // 拿着这些东西,直到条件满足为止。
                .takeWhile(new Predicate<String>() {
                    @Override
                    public boolean test(String s) {
                        return !s.toLowerCase().contains("honey");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<String> getObservable() {
        return Observable.just("Alpha", "Beta", "Cupcake", "Doughnut", "Eclair", "Froyo", "GingerBread",
                "Honeycomb", "Ice cream sandwich");
    }

    private Observer<String> getObserver() {
        return new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

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
