package com.khetigaadi.shop.model.category;
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*
@Entity
open class Category : Parcelable {
        @PrimaryKey
        @SerializedName("id")
        @Expose
        var id = 0
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("image")
        @Expose
        var image: String? = null
        @SerializedName("priority")
        @Expose
        var priority = 0
        @SerializedName("home_priority")
        @Expose
        @Transient
        var homePriority = 0
        @SerializedName("color_code")
        @Expose
        @Transient
        open var colorCode: String? = null
       // var isSelected = false
        constructor() {}
        override fun describeContents(): Int {
        return 0
        }
        override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(image)
        dest.writeInt(priority)
        dest.writeInt(homePriority)
        dest.writeString(colorCode)
       // dest.writeByte(if (isSelected) 1.toByte() else 0.toByte())
        }
        protected constructor(`in`: Parcel) {
        id = `in`.readInt()
        name = `in`.readString()
        image = `in`.readString()
        priority = `in`.readInt()
        homePriority = `in`.readInt()
        colorCode = `in`.readString()
       // isSelected = `in`.readByte().toInt() != 0
        }
        override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val category = o as Category
        return id == category.id
        }
        override fun hashCode(): Int {
        return Objects.hash(id)
        }
        companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
        override fun createFromParcel(source: Parcel): Category {
        return Category(source)
        }
        override fun newArray(size: Int): Array<Category?> {
        return arrayOfNulls(size)
        }
        }
        }
        }