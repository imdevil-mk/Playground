package com.imdevil.playground.sdk.biz.seat

import android.os.Parcel
import android.os.Parcelable

class DriverSeatMemoryBean() : Parcelable {

    var id: Int = 0
    var name: String = ""

    companion object CREATOR : Parcelable.Creator<DriverSeatMemoryBean> {
        override fun createFromParcel(parcel: Parcel): DriverSeatMemoryBean {
            return DriverSeatMemoryBean(parcel)
        }

        override fun newArray(size: Int): Array<DriverSeatMemoryBean> {
            return Array(size) { DriverSeatMemoryBean() }
        }
    }

    private constructor(parcel: Parcel) : this() {
        readFromParcel(parcel)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    fun readFromParcel(inParcel: Parcel) {
        id = inParcel.readInt()
        name = inParcel.readString() ?: ""
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "DriverSeatMemoryBean(id=$id, name=$name)"
    }
}