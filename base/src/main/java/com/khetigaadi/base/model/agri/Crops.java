package com.khetigaadi.base.model.agri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import java.util.List;
public class Crops extends MyResponse implements Parcelable {
    @SerializedName("data")
    @Expose
    private List<Crop> data = null;
    @SerializedName("others")
    @Expose
    private Others others;
    protected Crops(Parcel in) {
        data = in.createTypedArrayList(Crop.CREATOR);
        others = in.readParcelable(Others.class.getClassLoader());
    }
    public static final Creator<Crops> CREATOR = new Creator<Crops>() {
        @Override
        public Crops createFromParcel(Parcel in) {
            return new Crops(in);
        }

        @Override
        public Crops[] newArray(int size) {
            return new Crops[size];
        }
    };
    public List<Crop> getData() {
        return data;
    }
    public void setData(List<Crop> data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
    @NonNull
    @Override
    public String toString() {
        return "Crops{" +
                "data=" + data +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest,int flags) {
        dest.writeTypedList(data);
        dest.writeParcelable(others, flags);
    }
}



//package com.khetigaadi.base.model.agri;
//import java.util.List;
//
//
//public class Crops {
//    public List<Crop> getData() {
//        return data;
//    }
//    public void setData(List<Crop> data) {
//        this.data = data;
//    }
//    private List<Crop> data;
//    public boolean isError() {
//        return false;
//    }
//
////    public io.reactivex.Completable subscribeOn(Scheduler io) {
////    }
//
////    public io.reactivex.Completable subscribeOn(io.reactivex.rxjava3.core.Scheduler io) {
////    }
//}
