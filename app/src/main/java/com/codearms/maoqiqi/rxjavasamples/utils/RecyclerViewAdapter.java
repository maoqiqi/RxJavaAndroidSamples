package com.codearms.maoqiqi.rxjavasamples.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codearms.maoqiqi.rxjavasamples.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * RecyclerViewAdapter
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/16 22:22
 */
public final class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private List<String> data;
    private BeforeClickListener listener;

    public RecyclerViewAdapter(Activity activity, List<String> data, BeforeClickListener listener) {
        this.activity = activity;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.btn.setText(data.get(i));
        final String methodName = toLowerCaseFirstOne(data.get(i));
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onBefore();
                try {
                    @SuppressLint("PrivateApi")
                    Method method = activity.getClass().getDeclaredMethod(methodName);
                    method.setAccessible(true);
                    method.invoke(activity);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // 首字母转小写
    private static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) return s;
        else return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        Button btn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn);
        }
    }
}