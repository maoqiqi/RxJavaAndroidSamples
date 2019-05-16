package com.codearms.maoqiqi.rxjavasamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.codearms.maoqiqi.rxjavasamples.utils.Constant;
import com.codearms.maoqiqi.rxjavasamples.utils.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 用于演示过滤和选择Observable发射的数据序列
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/14 15:25
 */
public class FilterActivity extends BaseActivity {

    private static final String TAG = FilterActivity.class.getSimpleName();

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("过滤操作");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.textView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        String[] arr = getResources().getStringArray(R.array.filter_array);
        recyclerView.setAdapter(new RecyclerViewAdapter(this, Arrays.asList(arr)));
    }

    // Filter:过滤,过滤掉没有通过谓词测试的数据项,只发射通过测试的
    private void filter() {
        getObservable()
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) {
                        return aLong % 2 == 0;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // Last:末项,只发射最后一条数据
    private void last() {
        getObservable()
                // 如果ObservableSource为空,则发出默认项(-1000L)
                .last(-1000L)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSingleObserver());
    }

    // Skip:跳过前面的若干项数据
    private void skip() {
        getObservable()
                .skip(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // SkipLast:跳过后面的若干项数据
    private void skipLast() {
        getObservable()
                .skipLast(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // Take:只保留前面的若干项数据
    private void take() {
        getObservable()
                .take(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // TakeLast:只保留后面的若干项数据
    private void takeLast() {
        getObservable()
                .takeLast(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // First:首项,只发射满足条件的第一条数据
    private void first() {
        getObservable()
                .first(-1000L)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSingleObserver());
    }

    // ElementAt:取值,取特定位置的数据项
    private void elementAt() {
        getObservable()
                .elementAt(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMaybeObserver());
    }

    // Sample:取样,定期发射最新的数据,等于是数据抽样,有的实现里叫ThrottleFirst
    private void sample() {
        getObservable()
                .sample(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // Debounce:只有在空闲了一段时间后才发射数据,通俗的说,就是如果一段时间没有操作,就执行一次操作
    private void debounce() {
        // TODO: 2019-05-14 使用EditText每次输入变化做为例子
        getObservable()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // Distinct:去重,过滤掉重复数据项
    private void distinct() {
        List<Long> list = new ArrayList<>();
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int value = new Random().nextInt(10);
            stringBuffer.append(value);
            if (i < 9) stringBuffer.append(", ");
            list.add((long) value);
        }
        textView.append("源数据:{ " + stringBuffer.toString() + " }");
        textView.append(Constant.LINE_SEPARATOR);

        Observable.fromIterable(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .distinct()
                .subscribe(getObserver());
    }

    // IgnoreElements:忽略所有的数据,只保留终止通知(onError或onCompleted)
    private void ignoreElements() {
        getObservable()
                .ignoreElements()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getCompletableObserver());
    }

    private Observable<Long> getObservable() {
        // return Observable.intervalRange(1, 5, 5, 2, TimeUnit.SECONDS);
        // return Observable.rangeLong(1, 5);
        return Observable.intervalRange(1, 5, 0, 400, TimeUnit.MILLISECONDS);
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

    private CompletableObserver getCompletableObserver() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
                textView.append("onComplete");
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
}