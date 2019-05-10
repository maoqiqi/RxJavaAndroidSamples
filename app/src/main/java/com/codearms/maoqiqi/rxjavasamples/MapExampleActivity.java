package com.codearms.maoqiqi.rxjavasamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codearms.maoqiqi.rxjavasamples.bean.UserBean;
import com.codearms.maoqiqi.rxjavasamples.utils.Constant;
import com.codearms.maoqiqi.rxjavasamples.utils.ProvideData;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Map operator
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 15:10
 */
public class MapExampleActivity extends AppCompatActivity {

    private static final String TAG = MapExampleActivity.class.getSimpleName();

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("MapExample");
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSomeWork();
            }
        });
        textView = findViewById(R.id.textView);
    }

    // 将UserBean对象转换为String显示,我们只需要FirstName
    private void doSomeWork() {
        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<UserBean>, List<String>>() {
                    @Override
                    public List<String> apply(List<UserBean> userBeans) {
                        return ProvideData.convertUserListToStringList(userBeans);
                    }
                })
                .subscribe(getObserver());
    }

    private Observable<List<UserBean>> getObservable() {
        return Observable.create(new ObservableOnSubscribe<List<UserBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<UserBean>> emitter) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(ProvideData.getUserBeanList());
                    emitter.onComplete();
                }
            }
        });
    }

    private Observer<List<String>> getObserver() {

        return new Observer<List<String>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe -> " + d.isDisposed());
            }

            @Override
            public void onNext(List<String> strings) {
                Log.d(TAG, "onNext -> value.size() -> " + strings.size());
                textView.append("onNext : ");
                textView.append(Constant.LINE_SEPARATOR);
                for (int i = 0; i < strings.size(); i++) {
                    textView.append(" -> " + strings.get(i));
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