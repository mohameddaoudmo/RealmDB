package com.example.realmdb.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Teacher :RealmObject {
    @PrimaryKey  var id :ObjectId = ObjectId()
    var addresse :Addresse? = null
    var courses :RealmList<Courses> = realmListOf()


}