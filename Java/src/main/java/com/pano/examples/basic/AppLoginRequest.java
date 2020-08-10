package com.pano.examples.basic;


import java.io.Serializable;

public class AppLoginRequest implements Serializable {

    private String appId;

    private String channelId="panoAppServerDemo";

    private String userId="20190822";

    private int privileges=0;

    private int duration=1234;

    private int channelDuration=60;

    public AppLoginRequest() {
    }

    public AppLoginRequest(String appId, String channelId, String userId, int privileges, int duration, int channelDuration) {
        this.appId = appId;
        this.channelId = channelId;
        this.userId = userId;
        this.privileges = privileges;
        this.duration = duration;
        this.channelDuration = channelDuration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPrivileges() {
        return privileges;
    }

    public void setPrivileges(int privileges) {
        this.privileges = privileges;
    }

    public int getChannelDuration() {
        return channelDuration;
    }

    public void setChannelDuration(int channelDuration) {
        this.channelDuration = channelDuration;
    }
}
