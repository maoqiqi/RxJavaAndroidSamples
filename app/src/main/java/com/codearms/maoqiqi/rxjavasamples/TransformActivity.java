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
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * 用于演示对Observable发射的数据执行变换操作的各种操作符
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/14 10:10
 */
public class TransformActivity extends BaseActivity {

    private static final String TAG = TransformActivity.class.getSimpleName();

    protected TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("变换操作");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.textView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        String[] arr = getResources().getStringArray(R.array.transform_array);
        recyclerView.setAdapter(new RecyclerViewAdapter(this, Arrays.asList(arr)));
    }

    // Map:映射,通过对序列的每一项都应用一个函数变换Observable发射的数据,实质是对序列中的每一项执行一个函数,函数的参数就是这个数据项。
    // Cast:将原始Observable发射的每一项数据都强制转换为一个指定的类型,然后再发射数据,它是map的一个特殊版本。
    private void map() {
        Log.d(TAG, Constant.LINE_DIVIDER);
        textView.append(Constant.LINE_DIVIDER);
        getObservable().
                map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) {
                        return aLong.toString();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // FlatMap:扁平映射,将Observable发射的数据变换为Observables集合,然后将这些Observable发射的数据平坦化的放进一个单独的Observable,可以认为是一个将嵌套的数据结构展开的过程。
    // 注意:FlatMap对这些Observables发射的数据做的是合并(merge)操作,因此它们可能是交错的。
    // ConcatMap:它类似于最简单版本的flatMap,但是它按次序连接而不是合并那些生成的Observables,然后产生自己的数据序列。
    // 注意:如果任何一个通过这个flatMap操作产生的单独的Observable调用onError异常终止了,这个Observable自身会立即调用onError并终止。
    // 这个操作符有一个接受额外的int参数的一个变体。这个参数设置flatMap从原来的Observable映射Observables的最大同时订阅数。当达到这个限制时,它会等待其中一个终止然后再订阅另一个。
    // 还有一个版本的flatMap会使用原始Observable的数据触发的Observable组合这些数据,然后发射这些数据组合。它也有一个接受额外int参数的版本。
    // Javadoc:flatMap(Func1,Func2))
    // Javadoc:flatMap(Func1,Func2,int))
    // 还有一个版本的flatMap为原始Observable的每一项数据和每一个通知创建一个新的Observable（并对数据平坦化）。它也有一个接受额外int参数的变体。
    // Javadoc:flatMap(Func1,Func1,Func0))
    // Javadoc:flatMap(Func1,Func1,Func0,int))
    // flatMapIterable:这个变体成对的打包数据,然后生成Iterable而不是原始数据和生成的Observables,但是处理方式是相同的。
    // Javadoc:flatMapIterable(Func1))
    // Javadoc:flatMapIterable(Func1,Func2))
    // switchMap:它和flatMap很像,除了一点：当原始Observable发射一个新的数据（Observable）时,它将取消订阅并停止监视产生执之前那个数据的Observable,只监视当前这一个。
    private void flatMap() {
        getObservable()
                .buffer(3, 1)
                .flatMap(new Function<List<Long>, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(List<Long> longs) {
                        longs.add(-10000L);
                        return Observable.fromIterable(longs);
                    }
                })
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) {
                        return aLong.toString();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private void switchMap() {
        getObservable()
                .switchMap(new Function<Long, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Long aLong) {
                        int delay = new Random().nextInt(2);
                        return Observable.just(aLong.toString() + "x")
                                .delay(delay, TimeUnit.SECONDS, Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // Scan:扫描,对Observable发射的每一项数据应用一个函数,然后按顺序依次发射这些值。
    // 它将函数的结果同第二项数据一起填充给这个函数来产生它自己的第二项数据。它持续进行这个过程来产生剩余的数据序列。
    private void scan() {
        getObservable()
                .scan(new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long aLong, Long aLong2) {
                        return aLong + aLong2;
                    }
                })
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) {
                        return aLong.toString();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    // GroupBy:分组,将原始Observable分拆为一些Observables集合,将原始Observable发射的数据按Key分组,每一个Observable发射一组不同的数据。
    // 哪个数据项由哪一个Observable发射是由一个函数判定的,这个函数给每一项指定一个Key,Key相同的数据会被同一个Observable发射。
    private void groupBy() {
        getObservable()
                .groupBy(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) {
                        return (aLong % 2 == 0) ? "偶数组" : "奇数组";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver1());
    }

    // Buffer:缓存,可以简单的理解为缓存,它定期从Observable收集数据到一个集合,然后把这些数据集合打包发射,而不是一次发射一个。
    // Buffer操作符将一个Observable变换为另一个,原来的Observable正常发射数据,变换产生的Observable发射这些数据的缓存集合。
    // 注意：如果原来的Observable发射了一个onError通知,Buffer会立即传递这个通知,而不是首先发射缓存的数据,即使在这之前缓存中包含了原始Observable发射的数据。
    // buffer(count) 以列表(List)的形式发射非重叠的缓存,每一个缓存至多包含来自原始Observable的count项数据（最后发射的列表数据可能少于count项）
    // buffer(count, skip)从原始Observable的第一项数据开始创建新的缓存,此后每当收到skip项数据,用count项数据填充缓存。这些缓存可能会有重叠部分（比如skip<count时）,也可能会有间隙（比如skip>count时）。
    // buffer(bufferClosingSelector)
    // buffer(boundary)监视一个名叫boundary的Observable,每当这个Observable发射了一个值,它就创建一个新的List开始收集来自原始Observable的数据并发射原来的List。
    // Javadoc:buffer(Observable))
    // Javadoc:buffer(Observable,int))
    // buffer(bufferOpenings, bufferClosingSelector)监视这个叫bufferOpenings的Observable（它发射BufferOpening对象）,每当bufferOpenings发射了一个数据时,
    // 它就创建一个新的List开始收集原始Observable的数据,并将bufferOpenings传递给closingSelector函数。
    // 这个函数返回一个Observable。buffer监视这个Observable,当它检测到一个来自这个Observable的数据时,就关闭List并且发射它自己的数据（之前的那个List）。
    // buffer(timespan, unit[, scheduler])buffer(timespan, unit)定期以List的形式发射新的数据,每个时间段,
    // 收集来自原始Observable的数据（从前面一个数据包裹之后,或者如果是第一个数据包裹,从有观察者订阅原来的Observable之后开始）。
    // 还有另一个版本的buffer接受一个Scheduler参数,默认情况下会使用computation调度器。
    // Javadoc:buffer(long,TimeUnit))
    // Javadoc:buffer(long,TimeUnit,Scheduler))
    // buffer(timespan, unit, count[, scheduler])每当收到来自原始Observable的count项数据,或者每过了一段指定的时间后,buffer(timespan, unit, count)就以List的形式发射这期间的数据,
    // 即使数据项少于count项。还有另一个版本的buffer接受一个Scheduler参数,默认情况下会使用computation调度器。
    // Javadoc: buffer(long,TimeUnit,int))
    // Javadoc: buffer(long,TimeUnit,int,Scheduler))
    // buffer(timespan, timeshift, unit[, scheduler])在每一个timeshift时期内都创建一个新的List,然后用原始Observable发射的每一项数据填充这个列表（在把这个List当做自己的数据发射前,
    // 从创建时开始,直到过了timespan这么长的时间）。如果timespan长于timeshift,它发射的数据包将会重叠,因此可能包含重复的数据项。
    // 还有另一个版本的buffer接受一个Scheduler参数,默认情况下会使用computation调度器。
    // Javadoc:buffer(long,long,TimeUnit))
    // Javadoc:buffer(long,long,TimeUnit,Scheduler))
    private void buffer() {
        // 将所有发出的值捆绑到一个列表中
        // 3:它从它的开始索引和创建列表中最多取三个 2:每次跳2步
        getObservable()
                .buffer(3, 2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver2());
    }

    // Window:窗口,定期将来自Observable的数据分拆成一些Observable窗口,然后发射这些窗口,而不是每次发射一项。
    // 类似于Buffer,但Buffer发射的是数据,Window发射的是Observable,每一个Observable发射原始Observable的数据的一个子集。
    // Window操作符与Buffer类似,但是它在发射之前把收集到的数据放进单独的Observable,而不是放进一个数据结构。
    private void window() {
        getObservable()
                .window(3, 2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver1());
    }

    private Observable<Long> getObservable() {
        // return Observable.intervalRange(1, 5, 5, 2, TimeUnit.SECONDS);
        return Observable.rangeLong(1, 5);
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

    private Observer<Observable<Long>> getObserver1() {
        return new Observer<Observable<Long>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "First onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onNext(Observable<Long> longObservable) {
                if (longObservable instanceof GroupedObservable) {
                    String key = (String) ((GroupedObservable) longObservable).getKey();
                    Log.d(TAG, "First onNext -> value -> " + key);
                    textView.append("First onNext -> value -> " + key);
                } else {
                    Log.d(TAG, "First onNext -> longObservable");
                    textView.append("First onNext -> longObservable");
                }
                textView.append(Constant.LINE_SEPARATOR);
                longObservable
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) {
                                return aLong.toString();
                            }
                        })
                        .subscribe(getObserver());
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

    private Observer<List<Long>> getObserver2() {
        return new Observer<List<Long>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onNext(List<Long> longs) {
                Log.d(TAG, "onNext -> value.size() -> " + longs.size());
                textView.append("onNext -> value.size() -> " + longs.size());
                textView.append(Constant.LINE_SEPARATOR);
                for (int i = 0; i < longs.size(); i++) {
                    textView.append("value -> " + longs.get(i));
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
}