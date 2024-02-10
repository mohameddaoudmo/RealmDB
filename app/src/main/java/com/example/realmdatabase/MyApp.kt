package com.example.realmdatabase

import android.app.Application
import com.example.realmdb.model.Addresse
import com.example.realmdb.model.Courses
import com.example.realmdb.model.Student
import com.example.realmdb.model.Teacher
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApp : Application() {
    companion object {
        val realm: Realm by lazy{
            Realm.open(
                configuration = RealmConfiguration.create(
                    schema = setOf(
                        Addresse::class, Teacher::class, Student::class,
                        Courses::class

                    )
                )
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}