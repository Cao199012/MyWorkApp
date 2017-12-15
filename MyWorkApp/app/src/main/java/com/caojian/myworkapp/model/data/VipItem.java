package com.caojian.myworkapp.model.data;

/**
 * Created by caojian on 2017/9/19.
 */

public class VipItem {
    private String name = "";
    private int price = 0;
    private int type; //会员有效期类型 1:一周,2:一个月,3:一季度,4:一年

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
