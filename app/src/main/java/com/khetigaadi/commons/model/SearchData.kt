package com.khetigaadi.commons.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.khetigaadi.harvester.model.Harvester
import com.khetigaadi.implement.newImplement.util.NewImp
import com.khetigaadi.shop.model.Product
import com.khetigaadi.tractor.newTractor.model.NewTractor
import com.khetigaadi.tractor.usedTractor.util.UsedTractor

class SearchData() : Parcelable {
    @SerializedName("tractor")
    @Expose
    lateinit var tractor: List<NewTractor>

    @SerializedName("implement")
    @Expose
    lateinit var implement: List<NewImp>

    @SerializedName("harvester")
    @Expose
    lateinit var harvester: List<Harvester>

    @SerializedName("product")
    @Expose
    lateinit var products: List<Product>

    @SerializedName("used_tractor")
    @Expose
    lateinit var usedTractor: List<UsedTractor>

    constructor(parcel: Parcel) : this() {
        tractor = parcel.createTypedArrayList(NewTractor.CREATOR)!!
        products = parcel.createTypedArrayList(Product.CREATOR)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(tractor)
        parcel.writeTypedList(products)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchData> {
        override fun createFromParcel(parcel: Parcel): SearchData {
            return SearchData(parcel)
        }

        override fun newArray(size: Int): Array<SearchData?> {
            return arrayOfNulls(size)
        }
    }
}