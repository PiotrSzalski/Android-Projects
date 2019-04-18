package com.example.l3z1

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.RequiresApi
import java.time.LocalDateTime

class Task (var name: String, var date: LocalDateTime, var priority: Int, var picture: Int ) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readSerializable() as java.time.LocalDateTime,
        parcel.readInt(),
        parcel.readInt()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeSerializable(date)
        parcel.writeInt(priority)
        parcel.writeInt(picture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}