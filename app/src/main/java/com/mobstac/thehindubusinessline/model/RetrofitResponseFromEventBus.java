package com.mobstac.thehindubusinessline.model;

/**
 * Created by arvind on 19/9/16.
 */
public class RetrofitResponseFromEventBus {
    private String id;
    private String type;
    private boolean isSuccess;

    public RetrofitResponseFromEventBus(String id, String type, boolean isSuccess) {
        this.id = id;
        this.type = type;
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
