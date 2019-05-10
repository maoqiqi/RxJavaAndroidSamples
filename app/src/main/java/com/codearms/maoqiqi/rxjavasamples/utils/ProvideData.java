package com.codearms.maoqiqi.rxjavasamples.utils;

import com.codearms.maoqiqi.rxjavasamples.bean.UserBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * provide data
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 15:20
 */
public class ProvideData {

    public static List<UserBean> getUserBeanList() {
        List<UserBean> userBeanList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            userBeanList.add(new UserBean(i, "FirstName-" + i, "LastName-" + i, new Random().nextInt(100)));
        }

        return userBeanList;
    }

    public static List<UserBean> getUserBeanList2() {
        List<UserBean> userBeanList = new ArrayList<>();

        for (int i = 3; i < 8; i++) {
            userBeanList.add(new UserBean(i, "FirstName-" + i, "LastName-" + i, new Random().nextInt(100)));
        }

        return userBeanList;
    }

    public static List<String> convertUserListToStringList(List<UserBean> userBeanList) {
        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < userBeanList.size(); i++) {
            stringList.add(userBeanList.get(i).getFirstName());
        }

        return stringList;
    }

    public static List<String> filterUserBoth(List<UserBean> userBeanList, List<UserBean> userBeanList2) {
        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < userBeanList.size(); i++) {
            for (int j = 0; j < userBeanList2.size(); j++) {
                if (userBeanList.get(i).getId() == userBeanList2.get(j).getId()) {
                    stringList.add(userBeanList.get(i).getFirstName());
                }
            }
        }

        return stringList;
    }
}