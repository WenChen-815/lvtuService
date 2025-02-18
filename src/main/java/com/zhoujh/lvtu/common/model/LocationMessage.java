package com.zhoujh.lvtu.common.model;

import lombok.Data;

@Data
public class LocationMessage {
    public final static int SINGLE_TYPE = 1;
    public final static int GROUP_TYPE = 2;

    private String groupId;
    private String userId;
    private int type;
    private double longitude;
    private double latitude;
    private String senderId;
}
