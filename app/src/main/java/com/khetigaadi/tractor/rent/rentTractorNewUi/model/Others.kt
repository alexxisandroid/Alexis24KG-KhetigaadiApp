package com.khetigaadi.tractor.rent.rentTractorNewUi.model
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
data class Others(
    @SerializedName("brandLogoPath")
    val brandLogoPath: String,
    @SerializedName("rentTractorImplementTypeImagePath")
    val rentTractorImplementTypeImagePath: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(brandLogoPath)
        parcel.writeString(rentTractorImplementTypeImagePath)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<Others> {
        override fun createFromParcel(parcel: Parcel): Others {
            return Others(parcel)
        }
        override fun newArray(size: Int): Array<Others?> {
            return arrayOfNulls(size)
        }
    }
}