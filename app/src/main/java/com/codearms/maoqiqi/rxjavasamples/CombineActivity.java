package com.codearms.maoqiqi.rxjavasamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;
import com.codearms.maoqiqi.rxjavasamples.utils.RecyclerViewAdapter;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 用于演示组合多个Observables
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/15 10:50
 */
public class CombineActivity extends BaseActivity {

    private static final String TAG = CombineActivity.class.getSimpleName();

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("组合操作");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.textView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        String[] arr = getResources().getStringArray(R.array.combine_array);
        recyclerView.setAdapter(new RecyclerViewAdapter(this, Arrays.asList(arr)));
    }

    // StartWith:在发射原来的Observable的数据序列之前,先发射一个指定的数据序列或数据项
    private void startWith() {
        getObservable1().startWith(getObservable2())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // Merge:将两个Observable发射的数据组合并成一个
    // Merge可能会让合并的Observables发射的数据交错（有一个类似的操作符Concat不会让数据交错,它会按顺序一个接着一个发射多个Observables的发射物）。
    private void merge() {
        Observable.merge(getObservable1(), getObservable2())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // 组合观察值并维护观察值的顺序,首先是第一个观察到的,然后是第二个观察到的
    private void concat() {
        Observable.concat(getObservable1(), getObservable2())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // Zip:打包,使用一个指定的函数将多个Observable发射的数据组合在一起,然后将这个函数的结果作为单项数据发射
    // Zip操作符返回一个Observable,它使用这个函数按顺序结合两个或多个Observables发射的数据项,然后它发射这个函数返回的结果。
    // 它按照严格的顺序应用这个函数。它只发射与发射数据项最少的那个Observable一样多的数据。
    private void zip() {
        Observable
                .zip(getObservable1(), getObservable2(), new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long aLong, Long aLong2) {
                        return aLong + aLong2;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // CombineLatest:当两个Observables中的任何一个发射了一个数据时,通过一个指定的函数组合每个Observable发射的最新数据（一共两个数据）,然后发射这个函数的结果
    // CombineLatest操作符行为类似于zip,但是只有当原始的Observable中的每一个都发射了一条数据时zip才发射数据。
    // CombineLatest则在原始的Observable中任意一个发射了数据时发射一条数据。当原始Observables的任何一个发射了一条数据时,CombineLatest使用一个函数结合它们最近发射的数据,然后发射这个函数的返回值。
    private void combineLatest() {
        Observable
                .combineLatest(getObservable1(), getObservable2(), new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long aLong, Long aLong2) {
                        return aLong + aLong2;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // Join:无论何时,如果一个Observable发射了一个数据项,只要在另一个Observable发射的数据项定义的时间窗口内,就将两个Observable发射的数据合并发射
    private void join() {
        getObservable1().join(getObservable2(),
                new Function<Long, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Long aLong) {
                        return Observable.just(aLong).delay(1, TimeUnit.SECONDS);
                    }
                }, new Function<Long, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Long aLong) {
                        return Observable.just(aLong);
                    }
                }, new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long aLong, Long aLong2) {
                        return aLong + aLong2;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // Switch:将一个发射Observable序列的Observable转换为这样一个Observable:它逐个发射那些Observable最近发射的数据
    private void switchOnNext() {
        Observable
                .switchOnNext(Observable.just(getObservable1(), getObservable2()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<Long> getObservable1() {
        return Observable.rangeLong(1, 5);
    }

    private Observable<Long> getObservable2() {
        return Observable.rangeLong(8, 2);
    }

    private Observer<Long> getObserver() {
        return new Observer<Long>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

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