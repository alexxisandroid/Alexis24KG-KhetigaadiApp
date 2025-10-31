package com.khetigaadi.tractor.rent.rentTractorNewUi.model
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.khetigaadi.implement.newImplement.util.Type
import com.khetigaadi.implement.newImplement.util.conv.RentUnitTypeConverter
import com.khetigaadi.implement.newImplement.util.conv.TypeTypeConverter
import javax.inject.Inject

@Entity
class RentImplement():Parcelable {
    @SerializedName("id")
    @Expose
    @ColumnInfo(defaultValue = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(defaultValue = "rentId")
    @SerializedName(value = "rentId", alternate = ["rentId"])
    var rentId: Int = 0
   // @ColumnInfo(defaultValue = "type")
    @SerializedName(value = "type", alternate = ["type"])
    @Embedded
    @TypeConverters(TypeTypeConverter::class)
    lateinit var type: Type
    @ColumnInfo(defaultValue = "rentAmt")
    @SerializedName(value = "rentAmt", alternate = ["rentAmt"])
    var rentAmt: String = "0"
    //@ColumnInfo(defaultValue = "unit")
    @SerializedName(value = "unit", alternate = ["unit"])
    @Embedded
    @TypeConverters(RentUnitTypeConverter::class)
    lateinit var unit: RentUnit
    @ColumnInfo(defaultValue = "image")
    @SerializedName(value = "image", alternate = ["image"])
    var image: String? = null
    @Inject constructor(context:Context):this() {
        
    }
    constructor(parcel:Parcel):this() {
        id = parcel.readInt()
        rentId = parcel.readInt()
        type = parcel.readParcelable(Type::class.java.classLoader)!!
        rentAmt = parcel.readString().toString()
        unit = parcel.readParcelable(RentUnit::class.java.classLoader)!!
        image = parcel.readString()
    }
    override fun writeToParcel(parcel:Parcel,flags:Int) {
        parcel.writeInt(id)
        parcel.writeInt(rentId)
        parcel.writeParcelable(type,flags)
        parcel.writeString(rentAmt)
        parcel.writeParcelable(unit,flags)
        parcel.writeString(image)
    }
    override fun describeContents():Int {
        return 0
    }
    companion object CREATOR:Parcelable.Creator<RentImplement> {
        override fun createFromParcel(parcel:Parcel):RentImplement {
            return RentImplement(parcel)
        }
        override fun newArray(size: Int): Array<RentImplement?> {
            return arrayOfNulls(size)
        }
    }
}