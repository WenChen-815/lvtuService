package com.zhoujh.lvtu.common.model;

import lombok.Data;

@Data
public class LocationMessage {
    private String groupId;
    private String userId;
    private double longitude;
    private double latitude;
}
