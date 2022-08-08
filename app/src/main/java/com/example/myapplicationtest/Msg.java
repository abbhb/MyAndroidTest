package com.example.myapplicationtest;

import java.io.Serializable;

public class Msg implements Serializable {
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT =1;
    private boolean needtimetype = false;
    private String content;//消息内容
    private String time;//发送的时间

    public Msg(String msg, int type) {
        //不允许这种Msg
    }

    public String getWhocreate() {
        return whocreate;
    }

    private String whocreate;//谁发的消息
    public String getDatetime() {
        return datetime;
    }

    private String datetime;//发送的日期
    private long lastdate;
    private long date;

    public void setType(int type) {
        this.type = type;
    }

    private int type;
    public String getContent(){
        return content;
    }
    public String getTime(){
        return datetime;
    }
    public int getType(){
        return type;
    }
    public String getTimefortime(){
        return time;
    }


    public long getLastdate() {
        return lastdate;
    }

    public long getDate() {
        return date;
    }

    public boolean isNeedtimetype() {
        return needtimetype;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "needtimetype=" + needtimetype +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", whocreate='" + whocreate + '\'' +
                ", datetime='" + datetime + '\'' +
                ", lastdate=" + lastdate +
                ", date=" + date +
                ", type=" + type +
                '}';
    }

    public Msg(long lastdate, long date, String whocreate, String content, String datetime, String time, int type, boolean needtimetype){
        this.content=content;
        this.type=type;
        this.time = time;
        this.date = date;
        this.lastdate = lastdate;
        this.needtimetype = needtimetype;
        this.datetime = datetime;
        this.whocreate = whocreate;
    }
}
