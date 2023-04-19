package com.imdevil.playground.service.aidl

import android.os.Parcel
import android.os.Parcelable

class Book() : Parcelable {

    var id: Int = 0
    var name: String = ""

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book> {
            return Array(size) { Book() }
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
        return "Book(id=$id, name=$name)"
    }
}