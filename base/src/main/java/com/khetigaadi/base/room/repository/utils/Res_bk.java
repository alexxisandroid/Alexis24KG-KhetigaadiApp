package com.khetigaadi.base.room.repository.utils;
public class Res_bk<T> {
    public Res_bk(T bgdfg) {
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    private String message;
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    private T data;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    private String status;
}

