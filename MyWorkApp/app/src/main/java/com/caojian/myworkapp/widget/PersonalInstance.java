package com.caojian.myworkapp.widget;

import com.caojian.myworkapp.model.response.PersonalMsg;

/**
 * Created by CJ on 2017/10/31.
 * 内部静态类实现单例模式
 */

public class PersonalInstance {

    private PersonalMsg personalMsg;
    private PersonalInstance(){}
    public static class PersonalInstanceImpl{
        private static final PersonalInstance INSTANCE = new PersonalInstance();
    }

    public static PersonalInstance getInstance(){
        return PersonalInstanceImpl.INSTANCE;
    }
    public PersonalMsg getPersonalMsg() {
        return personalMsg;
    }

    public void setPersonalMsg(PersonalMsg personalMsg) {
        this.personalMsg = personalMsg;
    }

}
