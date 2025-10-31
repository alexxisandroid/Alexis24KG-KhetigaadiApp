package com.khetigaadi.base.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.Ignore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Ids implements Parcelable {
    protected Ids(Parcel in) {
    }
    public static final Creator<Ids> CREATOR = new Creator<Ids>() {
        @Override
        public Ids createFromParcel(Parcel in) {
            return new Ids(in);
        }
        @Override
        public Ids[] newArray(int size) {
            return new Ids[size];
        }
    };
    public Ids() {

    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }
  @Expose
  // @SerializedName("id") //tractor_model_id
   @SerializedName(value = "tractor_model_id",alternate = {"id","tractor_brand_id","brand_id","brandId"})
    @Ignore
   private int id;
}
