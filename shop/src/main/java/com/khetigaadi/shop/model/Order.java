package com.khetigaadi.shop.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class Order implements Parcelable,ParentOrder {
    @SerializedName("id")
    @Expose
    private int orderId;
    @SerializedName(value ="ambassador_user_id", alternate = "customerUserId")
    @Expose
    private String customerUserId;
    @SerializedName("address")
    @Expose
    private Address address;
    @Expose
    @SerializedName(value = "products",alternate = "order_products")
    private List<OrderProduct> products;
    @SerializedName("total_bill")
    @Expose
    private String totalBill;
    @SerializedName("isOnline")
    @Expose
    private boolean isOnline;
    @SerializedName("payUResp")
    @Expose
    private String payuResp;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("commission")
    @Expose
    private int commission;
    protected Order(Parcel in) {
        orderId = in.readInt();
        customerUserId = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());
        products = in.createTypedArrayList(OrderProduct.CREATOR);
        totalBill = in.readString();
        isOnline = in.readByte() != 0;
        payuResp = in.readString();
        createdAt = in.readString();
        commission = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeInt(orderId);
        dest.writeString(customerUserId);
        dest.writeParcelable(address, flags);
        dest.writeTypedList(products);
        dest.writeString(totalBill);
        dest.writeByte((byte) (isOnline ? 1 : 0));
        dest.writeString(payuResp);
        dest.writeString(createdAt);
        dest.writeInt(commission);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }
        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public String getCustomerUserId() {
        return customerUserId;
    }
    public void setCustomerUserId(String customerUserId) {
        this.customerUserId = customerUserId;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public Address getAddress() {
        return address;
    }
    public List<OrderProduct> getProducts() {
        return products;
    }
    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }
    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }
    public String getTotalBill() {
        return totalBill;
    }
    public boolean isOnline() {
        return isOnline;
    }
    public void setOnline(boolean online) {
        isOnline = online;
    }
    public String getPayuResp() {
        return payuResp;
    }
    public void setPayuResp(String payuResp) {
        this.payuResp = payuResp;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    @NonNull
    @Override
    public String toString() {
        return "Order{" +
                "address=" + address +
                ", products=" + products +
                ", totalBill='" + totalBill + '\'' +
                ", isOnline=" + isOnline +
                ", payuResp='" + payuResp + '\'' +
                '}';
    }
    public Order() {
    }
    public void setCommission(int commission) {
        this.commission = commission;
    }
    public int getCommission() {
        return commission;
    }
}
