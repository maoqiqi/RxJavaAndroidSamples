package com.codearms.maoqiqi.rxjavasamples.utils;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class Car {

    private String brand;

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Defer：在观察者订阅之前不创建这个Observable,为每一个观察者创建一个新的Observable
    // Defer操作符会一直等待直到有观察者订阅它,然后它使用Observable工厂方法生成一个Observable。
    // 它对每个观察者都这样做,因此尽管每个订阅者都以为自己订阅的是同一个Observable,事实上每个订阅者获取的是它们自己的单独的数据序列.

    public Observable<Long> brandDeferObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends Long>>() {
            @Override
            public ObservableSource<? extends Long> call() throws Exception {
                return Observable.just(111L);
            }
        });
    }
}