package com.khetigaadi.base.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class NewTractorShortSpec implements Parcelable {
    @SerializedName("data_id")
    @Expose
    private int dataId;
    @SerializedName("tractor_spec_id")
    @Expose
    private int tractorSpecId;
    @SerializedName("spec_data")
    @Expose
    private String specData;
    @SerializedName("tractor_id")
    @Expose
    private int tractorId;
    @SerializedName("tractor_spec")
    @Expose
    private TractorSpec tractorSpec;
    protected NewTractorShortSpec(Parcel in) {
        dataId = in.readInt();
        tractorSpecId = in.readInt();
        specData = in.readString();
        tractorId = in.readInt();
    }
    public static final Creator<NewTractorShortSpec> CREATOR = new Creator<NewTractorShortSpec>() {
        @Override
        public NewTractorShortSpec createFromParcel(Parcel in) {
            return new NewTractorShortSpec(in);
        }

        @Override
        public NewTractorShortSpec[] newArray(int size) {
            return new NewTractorShortSpec[size];
        }
    };
    public int getDataId() {
        return dataId;
    }
    public void setDataId(int dataId) {
        this.dataId = dataId;
    }
    public int getTractorSpecId() {
        return tractorSpecId;
    }
    public void setTractorSpecId(int tractorSpecId) {
        this.tractorSpecId = tractorSpecId;
    }
    public String getSpecData() {
        return specData;
    }
    public void setSpecData(String specData) {
        this.specData = specData;
    }
    public int getTractorId() {
        return tractorId;
    }
    public void setTractorId(int tractorId) {
        this.tractorId = tractorId;
    }
    public TractorSpec getTractorSpec() {
        return tractorSpec;
    }
    public void setTractorSpec(TractorSpec tractorSpec) {
        this.tractorSpec = tractorSpec;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(dataId);
        dest.writeInt(tractorSpecId);
        dest.writeString(specData);
        dest.writeInt(tractorId);
    }
}
