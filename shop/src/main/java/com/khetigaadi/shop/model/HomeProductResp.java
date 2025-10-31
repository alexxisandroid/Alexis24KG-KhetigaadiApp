package com.khetigaadi.shop.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import java.util.List;
public class HomeProductResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<CategoryProducts> data = null;
    @SerializedName("others")
    @Expose
    private TrendingProduct trendingProduct;

    public List<CategoryProducts> getData() {
        return data;
    }
    public void setData(List<CategoryProducts> data) {
        this.data = data;
    }
    public TrendingProduct getTrendingProduct() {
        return trendingProduct;
    }
    public void setTrendingProduct(TrendingProduct trendingProduct) {
        this.trendingProduct = trendingProduct;
    }
}




//import android.os.Parcel;
//import android.os.Parcelable;
//
//import androidx.annotation.NonNull;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//import com.khetigaadi.base.model.MyResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//public class HomeProductResp<T extends List<T>> extends MyResponse implements Parcelable {
//    @SerializedName("data")
//    @Expose
//    private T data = null;
//    @SerializedName("others")
//    @Expose
//    private TrendingProduct trendingProduct;
////    @SerializedName("error")
////    private boolean error;
//    protected HomeProductResp(Parcel in) {
//        trendingProduct = in.readParcelable(TrendingProduct.class.getClassLoader());
//       // error = in.readByte() != 0;
//       // msg = in.createStringArrayList();
//    }
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeParcelable(trendingProduct, flags);
//       // dest.writeByte((byte) (error ? 1 : 0));
//       // dest.writeStringList(msg);
//    }
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//    public static final Creator<HomeProductResp> CREATOR = new Creator<HomeProductResp>() {
//        @Override
//        public HomeProductResp createFromParcel(Parcel in) {
//            return new HomeProductResp(in);
//        }
//        @Override
//        public HomeProductResp[] newArray(int size) {
//            return new HomeProductResp[size];
//        }
//    };
//    public T getData() {
//        return data;
//    }
//    public void setData(T data) {
//        this.data = data;
//    }
//    public TrendingProduct getTrendingProduct() {
//        return trendingProduct;
//    }
//    public void setTrendingProduct(TrendingProduct trendingProduct) {
//        this.trendingProduct = trendingProduct;
//    }
////    public boolean isError() {
////        return error;
////    }
////    public void setError(boolean error) {
////        this.error = error;
////    }
////    public List<String> getMsg() {
////        return msg;
////    }
////    public void setMsg(List<String> msg) {
////        this.msg = msg;
////    }
//  // @SerializedName("msg")
//   //private List<String> msg;
//}