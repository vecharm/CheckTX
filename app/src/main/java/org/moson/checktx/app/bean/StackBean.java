package org.moson.checktx.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Copyright (C), 2016-2021
 * Author: 超人迪加
 * Date: 6/20/21 7:03 PM
 */
@Entity
public class StackBean {

    @Id(autoincrement = true)
    public Long _id;
    public String packagename;
    public String method;
    public String stackInfo;
    public boolean front;
    public long createtime;
    @Generated(hash = 1178679272)
    public StackBean(Long _id, String packagename, String method, String stackInfo,
            boolean front, long createtime) {
        this._id = _id;
        this.packagename = packagename;
        this.method = method;
        this.stackInfo = stackInfo;
        this.front = front;
        this.createtime = createtime;
    }
    @Generated(hash = 1475712625)
    public StackBean() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getPackagename() {
        return this.packagename;
    }
    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }
    public String getMethod() {
        return this.method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getStackInfo() {
        return this.stackInfo;
    }
    public void setStackInfo(String stackInfo) {
        this.stackInfo = stackInfo;
    }
    public boolean getFront() {
        return this.front;
    }
    public void setFront(boolean front) {
        this.front = front;
    }
    public long getCreatetime() {
        return this.createtime;
    }
    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

   
}
