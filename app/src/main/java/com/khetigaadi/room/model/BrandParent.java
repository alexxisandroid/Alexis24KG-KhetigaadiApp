package com.khetigaadi.room.model;
import androidx.room.Ignore;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class BrandParent implements Parcelable {
    /* @SerializedName("mf_products")
     @Expose
     private String mfProducts;
     @SerializedName("mf_production_output")
     @Expose
     private String mfProductionOutput;
     @SerializedName("mf_headquarters")
     @Expose
     private String mfHeadquarters;
     @SerializedName("mf_founded_year")
     @Expose
     private String mfFoundedYear;*/
    @SerializedName("brand_info")
    @Expose
    private String mfCompanyInfo;
    /*  @SerializedName("mf_no_of_location")
      @Expose
      private String mfNoOfLocation;*/
    @SerializedName("brand_type")
    @Expose
    private String manufacturerType;
    @SerializedName("brand_position")
    @Expose
    private Integer possition;
    @SerializedName("implement_position")
    @Expose
    private Integer implement_possition;
    /* @SerializedName("implement_category")
    @Expose
    private String implementCategory;*/
    @SerializedName("brand_status")
    private String status;
    public static final Creator<BrandParent> CREATOR = new Creator<BrandParent>() {
        @Override
        public BrandParent createFromParcel(Parcel in) {
            return new BrandParent(in);
        }
        @Override
        public BrandParent[] newArray(int size) {
            return new BrandParent[size];
        }
    };
  /*  public String getMfProducts() {
        return mfProducts;
    }

    public void setMfProducts(String mfProducts) {
        this.mfProducts = mfProducts;
    }


    public String getMfProductionOutput() {
        return mfProductionOutput;
    }

    public void setMfProductionOutput(String mfProductionOutput) {
        this.mfProductionOutput = mfProductionOutput;
    }


    public String getMfHeadquarters() {
        return mfHeadquarters;
    }

    public void setMfHeadquarters(String mfHeadquarters) {
        this.mfHeadquarters = mfHeadquarters;
    }

    public String getMfFoundedYear() {
        return mfFoundedYear;
    }

    public void setMfFoundedYear(String mfFoundedYear) {
        this.mfFoundedYear = mfFoundedYear;
    }*/
    public String getMfCompanyInfo() {
        return mfCompanyInfo;
    }
    public void setMfCompanyInfo(String mfCompanyInfo) {
        this.mfCompanyInfo = mfCompanyInfo;
    }
   /* public String getMfNoOfLocation() {
        return mfNoOfLocation;
    }
    public void setMfNoOfLocation(String mfNoOfLocation) {
        this.mfNoOfLocation = mfNoOfLocation;
    }*/
    public Integer getImplement_possition() {
        return implement_possition;
    }
    public void setImplement_possition(Integer implement_possition) {
        this.implement_possition = implement_possition;
    }
    public String getManufacturerType() {
        return manufacturerType;
    }
    public void setManufacturerType(String manufacturerType) {
        this.manufacturerType = manufacturerType;
    }
    public Integer getPossition() {
        return possition;
    }
    public void setPossition(Integer possition) {
        this.possition = possition;
    }
   /* public String getImplementCategory() {
        return implementCategory;
    }
    public void setImplementCategory(String implementCategory) {
        this.implementCategory = implementCategory;
    }*/
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "BrandParent{" +
                //  "mfProducts='" + mfProducts + '\'' +
                // ", mfProductionOutput='" + mfProductionOutput + '\'' +
                //   ", mfHeadquarters='" + mfHeadquarters + '\'' +
                //   ", mfFoundedYear='" + mfFoundedYear + '\'' +
                ", mfCompanyInfo='" + mfCompanyInfo + '\'' +
                //  ", mfNoOfLocation='" + mfNoOfLocation + '\'' +
                ", manufacturerType='" + manufacturerType + '\'' +
                ", possition=" + possition +
                ", imple_possition=" + implement_possition +
                //  ", implementCategory='" + implementCategory + '\'' +
                ", status=" + status +
                '}';
    }
    @Override
    public int describeContents()
    {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags)
    {
        // dest.writeString(this.mfProducts);
        // dest.writeString(this.mfProductionOutput);
        // dest.writeString(this.mfHeadquarters);
        //  dest.writeString(this.mfFoundedYear);
        dest.writeString(this.mfCompanyInfo);
        //dest.writeString(this.mfNoOfLocation);
        dest.writeString(this.manufacturerType);
        dest.writeValue(this.possition);
        dest.writeValue(this.implement_possition);
        dest.writeString(this.status);
    }
    @Ignore
    public BrandParent() {
    }
    @Ignore
    protected BrandParent(Parcel in)
    {
        //  this.mfProducts = in.readString();
        //  this.mfProductionOutput = in.readString();
        //  this.mfHeadquarters = in.readString();
        //  this.mfFoundedYear = in.readString();
        this.mfCompanyInfo = in.readString();
        //  this.mfNoOfLocation = in.readString();
        this.manufacturerType = in.readString();
        this.possition = (Integer) in.readValue(Integer.class.getClassLoader());
        this.implement_possition = (Integer) in.readValue(Integer.class.getClassLoader());
        //  this.implementCategory = in.readString();
        this.status = in.readString();
    }
}
