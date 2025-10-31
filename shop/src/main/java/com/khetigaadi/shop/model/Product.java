package com.khetigaadi.shop.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.shop.dataConveter.ProductImageDataTypeConverter;
import java.util.List;
@Entity
public class Product implements Parcelable {
    @SerializedName("id")
    @Expose
    @ColumnInfo(defaultValue = "product_id")
    @PrimaryKey
    private int product_id;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("stock")
    @Expose
    private int stock;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("discount_per")
    @Expose
    private String discountPer;
    @SerializedName("final_price")
    @Expose
    private int finalPrice;
    @SerializedName("ambassador_price")
    @Expose
    private int ambassadorPrice;
    @SerializedName("category")
    @Expose
    @Ignore
    private Category category;
    @SerializedName("images")
    @Expose
    @TypeConverters(ProductImageDataTypeConverter.class)
    private List<ProductImage> images = null;
    @SerializedName("image")
    @Expose
    @Embedded
    private ProductImage image = null;
    protected Product(Parcel in) {
        product_id = in.readInt();
        categoryId = in.readInt();
        name = in.readString();
        description = in.readString();
        stock = in.readInt();
        price = in.readInt();
        discountPer = in.readString();
        finalPrice = in.readInt();
        ambassadorPrice = in.readInt();
        category = in.readParcelable(Category.class.getClassLoader());
        images = in.createTypedArrayList(ProductImage.CREATOR);
        image = in.readParcelable(ProductImage.class.getClassLoader());
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(product_id);
        dest.writeInt(categoryId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(stock);
        dest.writeInt(price);
        dest.writeString(discountPer);
        dest.writeInt(finalPrice);
        dest.writeInt(ambassadorPrice);
        dest.writeParcelable(category, flags);
        dest.writeTypedList(images);
        dest.writeParcelable(image, flags);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }
        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    public int getProductId() {
        return product_id;
    }
    public void setProductId(int productId) {
        this.product_id = productId;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public String getDiscountPer() {
        return discountPer;
    }
    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }
    public int getFinalPrice() {
        return finalPrice;
    }
    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }
    public int getAmbassadorPrice() {
        return ambassadorPrice;
    }
    public void setAmbassadorPrice(int ambassadorPrice) {
        this.ambassadorPrice = ambassadorPrice;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public List<ProductImage> getImages() {
        return images;
    }
    public void setImages(List<ProductImage> images) {
        this.images = images;
    }
    public ProductImage getImage() {
        return image;
    }
    public void setImage(ProductImage image) {
        this.image = image;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
    public Product() {
    }
}
