package com.darwinvtomy.showcities.clickhome.model;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class Slave {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private int id;
    private String mid;
    private String sid;
    private String sname;
    private String type;
    private String status;
    private String value;
    private String enable;
    private String time;
    private String of_time;
    private String alarmstatus;
    private String img;
    private Bitmap bitmap;

    public String getOf_time() {
        return of_time;
    }

    public void setOf_time(String of_time) {
        this.of_time = of_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return sname;
    }

    public void setName(String name) {
        this.sname = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlarmstatus() {
        return alarmstatus;
    }

    public void setAlarmstatus(String alarmstatus) {
        this.alarmstatus = alarmstatus;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String image) {
        this.img = image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
