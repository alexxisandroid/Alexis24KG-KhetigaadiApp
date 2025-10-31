package com.khetigaadi.commons.model
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.khetigaadi.base.model.MyResponse
import com.khetigaadi.shop.model.search.Others;
//import com.khetigaadi.commons.model.search.Others
class SearchResp() : MyResponse(),Parcelable {
    @SerializedName("data")
    @Expose
    lateinit var data: SearchData
    @SerializedName("others")
    @Expose
    lateinit var others: Others
    @SerializedName("othersv")
    @Expose
     var othersv: com.khetigaadi.shop.model.Others? =null
    constructor(parcel: Parcel) : this() {
        data = parcel.readParcelable(SearchData::class.java.classLoader)!!
        othersv = parcel.readParcelable(com.khetigaadi.shop.model.Others::class.java.classLoader)
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(data, flags)
        parcel.writeParcelable(othersv, flags)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<SearchResp> {
        override fun createFromParcel(parcel: Parcel): SearchResp {
            return SearchResp(parcel)
        }
        override fun newArray(size: Int): Array<SearchResp?> {
            return arrayOfNulls(size)
        }
    }
}