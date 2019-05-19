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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class ConnectActivity extends BaseActivity implements BeforeClickListener {

    private static final String TAG = ConnectActivity.class.getSimpleName();

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("连接操作");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.textView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridDividerItemDecoration(1, getResources().getColor(android.R.color.darker_gray)));
        String[] arr = getResources().getStringArray(R.array.connect_array);
        recyclerView.setAdapter(new RecyclerViewAdapter(this, Arrays.asList(arr), this));
    }

    @Override
    public void onBefore() {
        Log.d(TAG, Constant.LINE_DIVIDER);
        textView.append(Constant.LINE_DIVIDER);
    }

    // Connect:指示一个可连接的Observable开始发射数据给订阅者
    private void connect() {
        Observable<Long> observable1 = getObservable();

        observable1
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver1());

        observable1
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver2());
    }

    // Publish:将一个普通的Observable转换为可连接的
    private void publish() {
        ConnectableObservable<Long> observable2 = getObservable().publish();

        observable2
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver1());

        observable2
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver2());

        observable2.connect();
    }

    // Replay:确保所有的观察者收到同样的数据序列，即使他们在Observable开始发射数据之后才订阅
    private void replay() {
        PublishSubject<Long> source = PublishSubject.create();
        // bufferSize = 3 保留要replay的3个值
        ConnectableObservable<Long> observable = source.replay(3);
        observable.connect();

        observable.subscribe(getObserver1());

        source.onNext(1L);
        source.onNext(2L);
        source.onNext(3L);
        source.onNext(4L);
        source.onComplete();

        // 它将发出2、3、4(count = 3),保留3个值以便replay
        observable.subscribe(getObserver2());
    }

    // RefCount:使一个可连接的Observable表现得像一个普通的Observable
    private void refCount() {
        getObservable()
                .publish()
                .refCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver2());
    }

    private Observable<Long> getObservable() {
//        return Observable.intervalRange(1, 5, 5, 2, TimeUnit.SECONDS);
//         return Observable.rangeLong(1, 5);
        return Observable.rangeLong(1, 1000000).sample(7, TimeUnit.MILLISECONDS);
    }

    private Observer<Long> getObserver1() {
        return new Observer<Long>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "First onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "First onNext -> value -> " + aLong);
                textView.append("First onNext -> value -> " + aLong);
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

    private Observer<Long> getObserver2() {
        return new Observer<Long>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "Second onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "Second onNext -> value -> " + aLong);
                textView.append("Second onNext -> value -> " + aLong);
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