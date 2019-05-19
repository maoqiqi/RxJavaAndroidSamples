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
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

/**
 * 用于演示Observable的辅助操作符
 * Author:fengqi.mao.march@gmail.com
 * Date:2019/5/15 13:43
 */
public class AssistActivity extends BaseActivity implements BeforeClickListener {

    private static final String TAG = AssistActivity.class.getSimpleName();

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("辅助操作");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.textView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridDividerItemDecoration(1, getResources().getColor(android.R.color.darker_gray)));
        String[] arr = getResources().getStringArray(R.array.assist_array);
        recyclerView.setAdapter(new RecyclerViewAdapter(this, Arrays.asList(arr), this));
    }

    @Override
    public void onBefore() {
        Log.d(TAG, Constant.LINE_DIVIDER);
        textView.append(Constant.LINE_DIVIDER);
    }

    /**
     * Do:注册一个动作占用一些Observable的生命周期事件，相当于Mock某个操作
     * ObserveOn:指定观察者观察Observable的调度程序（工作线程）
     * Subscribe:收到Observable发射的数据和通知后执行的操作
     * SubscribeOn:指定Observable应该在哪个调度程序上执行
     * Timeout:添加超时机制，如果过了指定的一段时间没有发射数据，就发射一个错误通知
     */

    // Materialize/Dematerialize:将发射的数据和通知都当做数据发射，或者反过来
    private void materialize() {
        getObservable()
                .materialize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getNotificationObserver());
    }

    private void dematerialize() {
        getNotificationObservable()
                .dematerialize(new Function<Notification<Long>, Notification<Long>>() {
                    @Override
                    public Notification<Long> apply(Notification<Long> longNotification) throws Exception {
                        return longNotification;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // Timestamp:给Observable发射的每个数据项添加一个时间戳
    private void timestamp() {
        getObservable()
                .timestamp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getTimedObserver());
    }

    // Serialize:强制Observable按次序发射数据并且功能是有效的
    private void serialize() {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) {
                try {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(1L);
                        emitter.onNext(2L);
                        emitter.onNext(3L);
                        emitter.onNext(5L);
                        emitter.onNext(6L);
                        emitter.onNext(7L);
                        emitter.onComplete();
                        Log.d(TAG, "被观察者onComplete()之后是否还有输出");
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        })
                .serialize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // Delay:延迟一段时间发射结果数据
    private void delay() {
        getObservable()
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // TimeInterval:将一个Observable转换为发射两个数据之间所耗费时间的Observable
    private void timeInterval() {
        getObservable()
                .timeInterval()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getTimedObserver());
    }

    // Using:创建一个只在Observable的生命周期内存在的一次性资源
    private void using() {
        // Func0->Callable
        // Func1->Function
        // Action1->Consumer<
        Observable.using(
                // 一个用于创建一次性资源的工厂函数
                new Callable<Long>() {
                    @Override
                    public Long call() {
                        return -1000L;
                    }
                },
                // 一个用于创建Observable的工厂函数,这个函数返回的Observable就是最终被观察的Observable
                new Function<Long, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Long aLong) {
                        return Observable.just(aLong);
                    }
                },
                // 一个用于释放资源的函数
                new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<Long> getObservable() {
        // return Observable.rangeLong(1, 5);
        return Observable.intervalRange(1, 5, 0, 2, TimeUnit.SECONDS);
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

    private Observable<Notification<Long>> getNotificationObservable() {
        return Observable.create(new ObservableOnSubscribe<Notification<Long>>() {
            @Override
            public void subscribe(ObservableEmitter<Notification<Long>> emitter) {
                emitter.onNext(Notification.createOnNext(1L));
                emitter.onNext(Notification.createOnNext(2L));
                emitter.onNext(Notification.<Long>createOnComplete());
                // emitter.onComplete();
            }
        });
    }

    private Observer<Notification<Long>> getNotificationObserver() {
        return new Observer<Notification<Long>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onNext(Notification<Long> longNotification) {
                if (longNotification.isOnNext()) {
                    Long value = longNotification.getValue();
                    Log.d(TAG, "onNext -> value -> " + value);
                    textView.append("onNext -> value -> " + value);
                    textView.append(Constant.LINE_SEPARATOR);
                }
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

    private Observer<Timed<Long>> getTimedObserver() {
        return new Observer<Timed<Long>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onNext(Timed<Long> longTimed) {
                Log.d(TAG, "onNext -> value -> " + longTimed.value() + " time ->" + (System.currentTimeMillis() - longTimed.time()));
                textView.append("onNext -> value -> " + longTimed.value() + " time ->" + longTimed.time());
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