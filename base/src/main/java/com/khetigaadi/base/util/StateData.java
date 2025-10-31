package com.khetigaadi.base.util;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StateData<T> implements Parcelable {

    protected StateData(Parcel in) {
        error = in.readString();
    }

    public static final Creator<StateData> CREATOR = new Creator<StateData>() {
        @Override
        public StateData createFromParcel(Parcel in) {
            return new StateData(in);
        }

        @Override
        public StateData[] newArray(int size) {
            return new StateData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(error);
    }
    public enum Status {
        CREATED,
        LOADING,
        SUCCESS,
        ERROR
    }

    @NonNull
    private Status status;
    @Nullable
    private T data;
    @Nullable
    private String error;

    public StateData() {
        this.status = Status.CREATED;
        this.data = null;
        this.error = null;
    }

    public StateData<T> loading() {
        this.status = Status.LOADING;
        this.data = null;
        this.error = null;
        return this;
    }

    public StateData<T> success(@NonNull T data) {
        this.status = Status.SUCCESS;
        this.data = data;
        this.error = null;
        return this;
    }

    public StateData<T> error(@NonNull String error) {
        this.status = Status.ERROR;
        this.data = null;
        this.error = error;
        return this;
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    public void setStatus(@NonNull Status status) {
        this.status = status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }

    @Nullable
    public String getError() {
        return error;
    }

    public void setError(@Nullable String error) {
        this.error = error;
    }
}
