package com.khetigaadi.tractor.rent.rentTractorNewUi.model
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.khetigaadi.base.model.MyResponse
data class RentImpImageResp(
    @SerializedName("data")
    val rentImpImageData: ImplementType,
    @SerializedName("others")
    val others: Others,
) : MyResponse(), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(ImplementType::class.java.classLoader)!!,
        parcel.readParcelable(Others::class.java.classLoader)!!
    ) {
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(rentImpImageData, flags)
        parcel.writeParcelable(others, flags)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<RentImpImageResp> {
        override fun createFromParcel(parcel: Parcel): RentImpImageResp {
            return RentImpImageResp(parcel)
        }
        override fun newArray(size: Int): Array<RentImpImageResp?> {
            return arrayOfNulls(size)
        }
    }
}