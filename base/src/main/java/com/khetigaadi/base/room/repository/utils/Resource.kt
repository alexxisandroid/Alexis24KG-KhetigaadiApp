package com.khetigaadi.base.room.repository.utils;
//import java.lang.reflect.Type;
import com.khetigaadi.base.model.Brand;
import java.util.List;
//public class Resource<T> {
//    public Resource(T bgdfg) {
//    }
//    public String getMessage() {
//        return message;
//    }
//    public void setMessage(String message) {
//        this.message = message;
//    }
//    private String message;
//    public T getData() {
//        return data;
//    }
//    public void setData(T data) {
//        this.data = data;
//    }
//    private T data;
//    public String getStatus() {
//        return status;
//    }
//    public void setStatus(String status) {
//        this.status = status;
//    }
//    private String status;
//}
//package com.khetigaadi.base.room.repository.utils

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
        data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}
