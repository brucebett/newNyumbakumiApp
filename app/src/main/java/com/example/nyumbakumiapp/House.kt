package com.example.nyumbakumiapp

import android.media.Image

class House {
    var houseNumber:String = ""
    var houseSize:String = ""
    var housePrice:String = ""
    var userId:String = ""
    var houseId:String = ""
    var houseImage:String = ""

    constructor(
        houseNumber: String,
        houseSize: String,
        housePrice: String,
        userId: String,
        houseId: String,
        houseImage: String
    ) {
        this.houseNumber = houseNumber
        this.houseSize = houseSize
        this.housePrice = housePrice
        this.userId = userId
        this.houseId = houseId
        this.houseImage = houseImage
    }
    constructor()
}