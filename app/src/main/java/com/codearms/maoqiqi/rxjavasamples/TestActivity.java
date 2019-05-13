package com.codearms.maoqiqi.rxjavasamples;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends ExampleActivity {

    private static final String ERROR = "故意让程序出错";

    @Override
    protected String getTitleText() {
        return "Test";
    }

    @Override
    protected void doSomeWork() {
        fun2();
    }

    /**
     * 故意让程序出现异常,可以用来测试
     */
    private void getException() {
        int errorCode = Integer.valueOf(ERROR);
    }

    // 概念解释
    // 1. 被观察者,事件源:它决定什么时候触发事件以及触发怎样的事件.
    // 2. 观察者:它决定事件触发的时候将有怎样的行为.
    // 3. 订阅:被观察者被观察者订阅
    private void fun1() {
        // 1. 被观察者,事件源
        // RxJava使用Observable.create()方法来创建一个Observable,并为它定义事件触发规则.
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                emitter.onNext("Hello");
                emitter.onNext("World");
                emitter.onNext("!");
                emitter.onComplete();
                Log.d(TAG, "被观察者onComplete()之后是否还有输出");
            }
        });

        // 2. 观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "观察者 onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "观察者 onNext:" + s);
                // 故意让程序出现异常,用于测试onError()方法的执行
                // getException();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "观察者 onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "观察者 onComplete");
            }
        };

        // 3. 订阅
        observable.subscribe(observer);
    }


    // 快捷创建事件队列`Observable.just(T...)`
    // create()方法是RxJava最基本的创造事件序列的方法。
    // 基于这个方法,RxJava 还提供了一些方法用来快捷创建事件队列,例如just(T...),将传入的参数依次发送出来.
    private void fun2() {
        // 1. 被观察者,事件源
        Observable<String> observable = Observable.just("Hello", "World", "!");
        // 将会依次调用：
        // emitter.onNext("Hello");
        // emitter.onNext("World");
        // emitter.onNext("!");
        // emitter.onComplete();

        // 2. 观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "观察者 onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "观察者 onNext:" + s);
                // 故意让程序出现异常,用于测试onError()方法的执行
                // getException();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "观察者 onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "观察者 onComplete");
            }
        };

        // 3. 订阅
        observable.subscribe(observer);

        // 快捷创建事件队列`Observable.from(T[])/from(Iterable<? extends T>`
        // 将传入的数组或Iterable拆分成具体对象后,依次发送出来
        String[] array = new String[]{"Hello", "World", "!"};
        List<String> list = Arrays.asList(array);
        Observable<String> observable1 = Observable.fromArray(array);
        Observable<String> observable2 = Observable.fromIterable(list);
    }

    // subscribe()支持不完整定义的回调

    // subscribe一个参数的不完整定义的回调
    // subscribe(final Action1<? super T> onNext)

    // subscribe两个参数的不完整定义的回调
    // subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError)

    // subscribe三个参数的不完整定义的回调
    // subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onComplete)
    private void fun3() {
        Observable<String> observable = Observable.just("Hello", "World", "!");

        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        };

        observable.subscribe(consumer);
    }

    // 线程控制Scheduler【加载图片案例】
    private void fun4() {
        // 加载图片将会发生在IO线程,而设置图片则被设定在了主线程。这就意味着,即使加载图片耗费了几十甚至几百毫秒的时间,也不会造成丝毫界面的卡顿。
        Observable.create(
                new ObservableOnSubscribe<Drawable>() {
                    @Override
                    public void subscribe(ObservableEmitter<Drawable> emitter) {
                        Log.d(TAG, "被观察者");
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_logo);
                        emitter.onNext(drawable);
                        emitter.onComplete();
                    }
                })
                // 事件产生的线程。指定subscribe()发生在IO线程
                .subscribeOn(Schedulers.io())
                // doOnSubscribe()之后有observeOn()的话,它将执行在离它最近的observeOn()所指定的线程。这里将执行在主线程中。
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        Log.d(TAG, "doOnSubscribe()");
                    }
                })
                // 指定Subscriber所运行在的线程。或者叫做事件消费的线程。
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    // 变换map()【加载图片案例】


    // 引入flatmap()

    /**
     * RxBinding
     * RxBinding 是 Jake Wharton 的一个开源库，它提供了一套在 Android 平台上的基于 RxJava 的 Binding API。
     * 所谓 Binding，就是类似设置 OnClickListener 、设置 TextWatcher 这样的注册绑定对象的 API。
     * 举个设置点击监听的例子。使用 RxBinding ，可以把事件监听用这样的方法来设置：
     * throttleFirst() ，用于去抖动，也就是消除手抖导致的快速连环点击：
     */

    private void method16() {
//        RxView.clicks(mImageView)
//                .throttleFirst(500, TimeUnit.MILLISECONDS)//500ms,第一次点击后,500ms内点击无效,500ms后点击才会响应
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();
//        new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}