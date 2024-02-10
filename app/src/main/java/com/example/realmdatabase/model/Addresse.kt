package com.example.realmdb.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Addresse : RealmObject {
    @PrimaryKey var id :ObjectId = ObjectId()
    var fullName: String = ""
    var zipCode: String = ""
    var street: String = ""
    var houseName: String = ""
    var city :String = ""
    var teacher :Teacher ?=null
}