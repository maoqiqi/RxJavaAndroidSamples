package com.codearms.maoqiqi.rxjavasamples.bean;

/**
 * User bean
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/5/10 15:15
 */
public class UserBean {

    private long id;

    private String firstName;

    private String lastName;

    private int age;

    public UserBean() {
    }

    public UserBean(long id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}