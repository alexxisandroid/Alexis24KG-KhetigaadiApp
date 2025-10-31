package com.khetigaadi.base.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class MyResponse implements Parcelable {
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName(value = "msg", alternate = "message")
    @Expose
    //private String msg;
    private List<String> msg = null;
    public MyResponse(){}
    protected MyResponse(Parcel in) {
        error = in.readByte() != 0;
        msg = in.createStringArrayList();
    }

    public static final Creator<MyResponse> CREATOR = new Creator<MyResponse>() {
        @Override
        public MyResponse createFromParcel(Parcel in) {
            return new MyResponse(in);
        }

        @Override
        public MyResponse[] newArray(int size) {
            return new MyResponse[size];
        }
    };

    public boolean isError() {
        return error;
    }
    public void setError(boolean error) {
        this.error = error;
    }
    public List<String> getMsg() {
        return msg;
    }
    public void setMsg(List<String> msg) {
        this.msg = msg;
    }


    @Override
    public String toString() {
        return "MyResponse{" +
                "error=" + error +
                ", msg='" + msg + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeStringList(msg);
    }
}
