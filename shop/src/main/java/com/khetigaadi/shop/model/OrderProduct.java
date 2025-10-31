package com.khetigaadi.shop.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(indices = {@Index(value = {"ProductId"},unique = true)})
public class OrderProduct implements Parcelable {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    @Expose
    @ColumnInfo(defaultValue = "order_id")
    private int id;
    private int userId;
    private int ProductId;
    @SerializedName(value = "product", alternate = "products")
    @Expose
    @Embedded
   // @TypeConverters(ProductTypeConverter.class)
    private Product product;
    @SerializedName("quantity")
    @Expose
    private int quantity = 1;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getProductId() {
        return ProductId;
    }
    public void setProductId(int productId) {
        ProductId = productId;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.product, flags);
        dest.writeInt(this.quantity);
    }
    public OrderProduct() {
    }
    protected OrderProduct(Parcel in) {
        this.id = in.readInt();
        this.product = in.readParcelable(Product.class.getClassLoader());
        this.quantity = in.readInt();
    }
    public static final Parcelable.Creator<OrderProduct> CREATOR = new Parcelable.Creator<OrderProduct>() {
        @Override
        public OrderProduct createFromParcel(Parcel source) {
            return new OrderProduct(source);
        }
        @Override
        public OrderProduct[] newArray(int size) {
            return new OrderProduct[size];
        }
    };
}
