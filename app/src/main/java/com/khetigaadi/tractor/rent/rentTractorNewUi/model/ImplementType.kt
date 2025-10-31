package com.khetigaadi.tractor.rent.rentTractorNewUi.model
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
data class ImplementType(
    @SerializedName("image")
    val image: String?,
    @SerializedName("implement_type_id")
    val implementTypeId: Int,
    @SerializedName("implement_type_name")
    val implementTypeName: String?,
    @SerializedName("rate")
    val rate: String?,
    @SerializedName("unit")
    val unit: RentUnit?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(RentUnit::class.java.classLoader)
    ) {
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeInt(implementTypeId)
        parcel.writeString(implementTypeName)
        parcel.writeString(rate)
        parcel.writeParcelable(unit, flags)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR:Parcelable.Creator<ImplementType> {
        override fun createFromParcel(parcel:Parcel):ImplementType {
            return ImplementType(parcel)
        }
        override fun newArray(size:Int):Array<ImplementType?> {
            return arrayOfNulls(size)
        }
    }
}