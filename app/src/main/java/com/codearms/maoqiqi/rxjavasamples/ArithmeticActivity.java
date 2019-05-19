package com.codearms.maoqiqi.rxjavasamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.codearms.maoqiqi.rxjavasamples.utils.BeforeClickListener;
import com.codearms.maoqiqi.rxjavasamples.utils.Constant;
import com.codearms.maoqiqi.rxjavasamples.utils.GridDividerItemDecoration;
import com.codearms.maoqiqi.rxjavasamples.utils.RecyclerViewAdapter;

import java.util.Arrays;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class ArithmeticActivity extends BaseActivity implements BeforeClickListener {

    private static final String TAG = ArithmeticActivity.class.getSimpleName();

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("算数操作");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.textView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridDividerItemDecoration(1, getResources().getColor(android.R.color.darker_gray)));
        String[] arr = getResources().getStringArray(R.array.arithmetic_array);
        recyclerView.setAdapter(new RecyclerViewAdapter(this, Arrays.asList(arr), this));
    }

    @Override
    public void onBefore() {
        Log.d(TAG, Constant.LINE_DIVIDER);
        textView.append(Constant.LINE_DIVIDER);
    }

    private void average() {
        getObservable().reduce(new BiFunction<Long, Long, Long>() {
            @Override
            public Long apply(Long aLong, Long aLong2) throws Exception {
                return null;
            }
        });
//        getObservable().sum
    }

    private void count() {
        getObservable()
                .count()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSingleObserver());
    }

    private void reduce() {
        getObservable()
                .reduce(new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long aLong, Long aLong2) {
                        return aLong + aLong2;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMaybeObserver());
    }

    private Observable<Long> getObservable() {
        return Observable.rangeLong(1, 5);
    }

    private SingleObserver<Long> getSingleObserver() {
        return new SingleObserver<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onSuccess(Long aLong) {
                Log.d(TAG, "onSuccess -> value -> " + aLong);
                textView.append("onSuccess -> value -> " + aLong);
                textView.append(Constant.LINE_SEPARATOR);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError -> " + e.getMessage());
                textView.append("onError -> " + e.getMessage());
                textView.append(Constant.LINE_SEPARATOR);
            }
        };
    }

    private MaybeObserver<Long> getMaybeObserver() {
        return new MaybeObserver<Long>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onSuccess(Long aLong) {
                Log.d(TAG, "onSuccess -> value -> " + aLong);
                textView.append("onSuccess -> value -> " + aLong);
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