package com.spectrotank.DataLayer.db


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
class Item {
    @PrimaryKey
    var IMEI: String = ""
    var name: String? = ""
    var height: String? = ""
    var width: String? = ""
    var length: String? = ""
    var diameter: String? = ""
    var volume: Double? = 0.0
    var type: String? = ""
    var latitude: String? = ""
    var longitude: String? = ""
    var imgUrl: String? = ""
    var imgUrlStr: String? = ""
    var isSynced: Boolean = false

    override fun toString(): String {
        return "{IMEI='$IMEI', name=$name, height=$height, width=$width, length=$length, diameter=$diameter, volume=$volume, type=$type, latitude=$latitude, longitude=$longitude, imgUrl=$imgUrl}"
    }
}