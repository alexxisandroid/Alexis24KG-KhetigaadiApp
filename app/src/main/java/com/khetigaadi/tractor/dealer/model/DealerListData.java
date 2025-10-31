package com.khetigaadi.tractor.dealer.model;
import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;

import java.util.List;
public class DealerListData extends ListData {
    @SerializedName("data")
    @Expose
    private List<Dealer> dealers = null;

    protected DealerListData(Parcel in) {
        super(in);
    }

    public List<Dealer> getDealers() {
        return dealers;
    }
    public void setDealers(List<Dealer> dealers) {
        this.dealers = dealers;
    }
}
