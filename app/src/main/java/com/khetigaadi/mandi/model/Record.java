package com.khetigaadi.mandi.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Record {
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("market")
    @Expose
    private String market;
    @SerializedName("commodity")
    @Expose
    private String commodity;
    @SerializedName("variety")
    @Expose
    private String variety;
    @SerializedName("arrival_date")
    @Expose
    private String arrivalDate;
    @SerializedName("min_price")
    @Expose
    private String minPrice;
    @SerializedName("max_price")
    @Expose
    private String maxPrice;
    @SerializedName("modal_price")
    @Expose
    private String modalPrice;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getModalPrice() {
        return modalPrice;
    }

    public void setModalPrice(String modalPrice) {
        this.modalPrice = modalPrice;
    }

    public static DiffUtil.ItemCallback<Record> DIFF_CALLBACK = new DiffUtil.ItemCallback<Record>() {
        @Override
        public boolean areItemsTheSame(@NonNull Record record, @NonNull Record t1) {
            return Objects.equals(record.timestamp, t1.timestamp) &&
                    Objects.equals(record.state, t1.state) &&
                    Objects.equals(record.district, t1.district) &&
                    Objects.equals(record.market, t1.market) &&
                    Objects.equals(record.commodity, t1.commodity) &&
                    Objects.equals(record.variety, t1.variety) &&
                    Objects.equals(record.arrivalDate, t1.arrivalDate) &&
                    Objects.equals(record.minPrice, t1.minPrice) &&
                    Objects.equals(record.maxPrice, t1.maxPrice) &&
                    Objects.equals(record.modalPrice, t1.modalPrice);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Record record, @NonNull Record t1) {
            return record.equals(t1);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(timestamp, record.timestamp) &&
                Objects.equals(state, record.state) &&
                Objects.equals(district, record.district) &&
                Objects.equals(market, record.market) &&
                Objects.equals(commodity, record.commodity) &&
                Objects.equals(variety, record.variety) &&
                Objects.equals(arrivalDate, record.arrivalDate) &&
                Objects.equals(minPrice, record.minPrice) &&
                Objects.equals(maxPrice, record.maxPrice) &&
                Objects.equals(modalPrice, record.modalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, state, district, market, commodity, variety, arrivalDate, minPrice, maxPrice, modalPrice);
    }
}
