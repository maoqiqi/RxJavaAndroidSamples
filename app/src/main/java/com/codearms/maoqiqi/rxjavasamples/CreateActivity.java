package com.codearms.maoqiqi.rxjavasamples;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.codearms.maoqiqi.rxjavasamples.utils.Car;
import com.codearms.maoqiqi.rxjavasamples.utils.Constant;
import com.codearms.maoqiqi.rxjavasamples.utils.RecyclerViewAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 用于展示了创建Observable的各种方法
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/13 12:12
 */
public class CreateActivity extends BaseActivity {

    private static final String TAG = SimpleExampleActivity.class.getSimpleName();
    private static final String ERROR = "故意让程序出错";

    private TextView textView;

    private Function<Long, Long> function;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("创建操作");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.textView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        String[] arr = getResources().getStringArray(R.array.create_array);
        recyclerView.setAdapter(new RecyclerViewAdapter(this, Arrays.asList(arr)));

        function = new Function<Long, Long>() {
            @Override
            public Long apply(Long aLong) {
                Log.d(TAG, isMainThread() ? "Main线程执行中...." : "IO线程执行中....");
                return aLong;
            }
        };
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        Log.d(TAG, Constant.LINE_DIVIDER);
        super.onDestroy();
    }

    public void create() {
        disposables.add(createObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // .subscribe(getObserver())
                .subscribeWith(getObserver()));
    }

    public void just() {
        disposables.add(justObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    public void from() {
        disposables.add(fromObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    public void start() {
        disposables.add(startObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    public void defer() {
        Car car = new Car();

        disposables.add(car.brandDeferObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));

        // 即使我们在创建Observable后调用setBrand(),我们也会得到"BMW"。如果我们没有使用defer,我们将得到null。
        car.setBrand("BMW");
    }

    public void empty() {
        disposables.add(emptyObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    public void never() {
        disposables.add(neverObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    public void error() {
        disposables.add(errorObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    public void range() {
        disposables.add(rangeObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    public void interval() {
        disposables.add(intervalObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    public void interval_range() {
        disposables.add(intervalRangeObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    public void timer() {
        disposables.add(timerObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    public void repeat() {
        disposables.add(repeatObservable()
                .map(function)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    // Create:通过调用观察者的方法从头创建一个Observable
    private Observable<Long> createObservable() {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) {
                try {
                    // 建议你在传递给create方法的函数中检查观察者的isDisposed状态,以便在没有观察者的时候,让你的Observable停止发射数据或者做昂贵的运算.
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
        });
    }

    /**
     * 快捷创建事件队列
     */
    // Just:将对象或者对象集合转换为一个会发射这些对象的Observable.
    // Just类似于From,但是From会将数组或Iterable的数据取出然后逐个发射,而Just只是简单的原样发射,将数组或Iterable当做单个数据.
    // 注意:如果你传递null给Just,它会返回一个发射null值的Observable。不要误认为它会返回一个空Observable(完全不发射任何数据的Observable),如果需要空Observable你应该使用Empty操作符。
    private Observable<Long> justObservable() {
        // 将会依次调用:
        // emitter.onNext(1L);
        // emitter.onNext(2L);
        // emitter.onNext(3L);
        // emitter.onNext(5L);
        // emitter.onNext(6L);
        // emitter.onNext(7L);
        // emitter.onComplete();
        // 接受一至九个参数,返回一个按参数列表顺序发射这些数据的Observable
        return Observable.just(1L, 2L, 3L, 5L, 6L, 7L);
    }

    // From:将其它的对象或数据结构转换为Observable
    private Observable<Long> fromObservable() {
        // 对于Iterable和数组,产生的Observable会发射Iterable或数组的每一项数据。
        // 对于Future,它会发射Future.get()方法返回的单个数据
        // fromFuture(Future<? extends T> future, long timeout, TimeUnit unit, Scheduler scheduler)
        // 单项数据,超时时长,时间单位,调度器

        Long[] array = new Long[]{1L, 2L, 3L, 5L, 6L, 7L};
        // return Observable.fromArray(array);
        List<Long> list = Arrays.asList(array);
        return Observable.fromIterable(list);
    }

    // Start:创建发射一个函数的返回值的Observable
    // 注意:这个函数只会被执行一次,即使多个观察者订阅这个返回的Observable
    // RxJava 2.0 已经没有start,取而代之的是formCallable
    // fromCallable操作符,它接受一个Callable作为参数,返回一个发射这个Callable的结果的Observable
    private Observable<Long> startObservable() {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() {
                return 111L;
            }
        });
    }

    // Empty:创建一个不发射任何数据但是正常终止的Observable
    private Observable<Long> emptyObservable() {
        return Observable.empty();
    }

    // Never:创建一个不发射数据也不终止的Observable
    private Observable<Long> neverObservable() {
        return Observable.never();
    }

    // Error:创建一个不发射数据以一个错误终止的Observable
    private Observable<Long> errorObservable() {
        return Observable.error(new Throwable(ERROR));
    }

    // Range:创建发射指定范围的整数[int或者long]序列的Observable。它接受两个参数,一个是范围的起始值,一个是范围的数据的数目。
    private Observable<Long> rangeObservable() {
        // 发射[1,5]
        return Observable.rangeLong(1, 5);
    }

    // Interval:创建一个定时发射整数序列的Observable
    // Interval操作符返回一个Observable,它按固定的时间间隔发射一个无限递增的整数序列。
    private Observable<Long> intervalObservable() {
        // 5s后以2秒的间隔运行任务
        // Observable.intervalRange()
        return Observable.interval(5, 2, TimeUnit.SECONDS);
    }

    // 创建一个定时发射指定范围的整数序列的Observable
    private Observable<Long> intervalRangeObservable() {
        return Observable.intervalRange(1, 5, 5, 2, TimeUnit.SECONDS);
    }

    // Timer:创建在一个指定的延迟之后发射单个数据的Observable。它在延迟一段给定的时间后发射一个简单的数字0。
    private Observable<Long> timerObservable() {
        // 延迟两秒
        return Observable.timer(2, TimeUnit.SECONDS);
    }

    // Repeat:创建重复发射特定的数据或数据序列的Observable。
    // 它不是创建一个Observable,而是重复发射原始Observable的数据序列,这个序列或者是无限的,或者通过repeat(n)指定重复次数。
    private Observable<Long> repeatObservable() {
        // Observable.rangeLong(1, 5).repeatWhen();
        return Observable.rangeLong(1, 5).repeat(3);
    }

//    private Observer<Long> getObserver() {
//        return new Observer<Long>() {
//
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
//            }
//
//            @Override
//            public void onNext(Long aLong) {
//                Log.d(TAG, "onNext -> value -> " + aLong);
//                textView.append("onNext -> value -> " + aLong);
//                textView.append(Constant.LINE_SEPARATOR);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "onError -> " + e.getMessage());
//                textView.append("onError -> " + e.getMessage());
//                textView.append(Constant.LINE_SEPARATOR);
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete");
//                textView.append("onComplete");
//                textView.append(Constant.LINE_SEPARATOR);
//            }
//        };
//    }

    private DisposableObserver<Long> getObserver() {
        return new DisposableObserver<Long>() {

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