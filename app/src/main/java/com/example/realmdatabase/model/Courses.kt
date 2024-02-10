package com.example.realmdb.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Courses:RealmObject {
    @PrimaryKey var id :ObjectId = ObjectId()
    var name :String= ""
    var enrolledStudent :RealmList<Student> = realmListOf()
    var teacher :Teacher? = null
}