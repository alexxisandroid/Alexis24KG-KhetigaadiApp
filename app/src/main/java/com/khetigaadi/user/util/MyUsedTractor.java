package com.khetigaadi.user.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;
import com.khetigaadi.user.myUsedTractor.model.InterestedBuyersData;

import java.util.List;
public class MyUsedTractor extends UsedTractor {
    @SerializedName("intersted_buyers_count")
    @Expose
    private String buyersCount;
    private boolean isBuyersOpen;
    private List<InterestedBuyersData> mInterestedBuyers;
    public List<InterestedBuyersData> getInterestedBuyers() {
        return mInterestedBuyers;
    }

    public void setInterestedBuyers(List<InterestedBuyersData> list) {
        this.mInterestedBuyers = list;
    }

    public String getBuyersCount() {
        return buyersCount;
    }

    public void setBuyersCount(String buyersCount) {
        this.buyersCount = buyersCount;
    }

    public boolean isBuyersOpen() {
        return isBuyersOpen;
    }

    public void setBuyersOpen(boolean buyersOpen) {
        isBuyersOpen = buyersOpen;
    }


}
