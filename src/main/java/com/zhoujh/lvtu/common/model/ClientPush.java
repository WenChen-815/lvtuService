package com.zhoujh.lvtu.common.model;

import lombok.Data;

import java.util.Map;

@Data
public class ClientPush {
    private String pusherId;
    private String pusherName;
    private String targetId;
    private String title;
    private String body;
    private Map<String,String> dataMap;

    public ClientPush(String pusherId, String pusherName, String targetId, String title, String body, Map<String, String> dataMap) {
        this.pusherId = pusherId;
        this.pusherName = pusherName;
        this.targetId = targetId;
        this.title = title;
        this.body = body;
        this.dataMap = dataMap;
    }

    public ClientPush() {
    }

    public String getPusherId() {
        return pusherId;
    }

    public void setPusherId(String pusherId) {
        this.pusherId = pusherId;
    }

    public String getPusherName() {
        return pusherName;
    }

    public void setPusherName(String pusherName) {
        this.pusherName = pusherName;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }
}
