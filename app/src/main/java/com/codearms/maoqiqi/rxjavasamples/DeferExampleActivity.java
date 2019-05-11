package com.codearms.maoqiqi.rxjavasamples;

import android.util.Log;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Defer example
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 22:11
 */
public class DeferExampleActivity extends ExampleActivity {

    @Override
    protected String getTitleText() {
        return "DeferExample";
    }

    // 将可观察代码延迟到RxJava中的订阅
    @Override
    protected void doSomeWork() {
        Car car = new Car();

        car.brandDeferObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());

        // 即使我们在创建Observable后设置了品牌,我们也会得到BMW这个品牌。
        // 如果我们没有使用defer,我们将得到null作为品牌。
        car.setBrand("BMW");
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

    public class Car {

        private String brand;

        void setBrand(String brand) {
            this.brand = brand;
        }

        Observable<String> brandDeferObservable() {
            return Observable.defer(new Callable<ObservableSource<String>>() {
                @Override
                public ObservableSource<String> call() {
                    return Observable.just(brand);
                }
            });
        }
    }
}