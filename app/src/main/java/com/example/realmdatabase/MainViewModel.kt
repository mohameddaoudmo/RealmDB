package com.example.realmdatabase

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmdb.model.Addresse
import com.example.realmdb.model.Courses
import com.example.realmdb.model.Student
import com.example.realmdb.model.Teacher
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val realm = MyApp.realm
    val courses = realm.query<Courses>(
//        "enrolledStudent.@count >= 2",
//        "teacher.addresse.fullName CONTAINS $0", "Abd elrazek"
    ).asFlow().map { res ->
        res.list.toList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    var courseDetails: Courses? by mutableStateOf(null)
        private set

    init {
        createEntries()
        Log.d("MainsViewModel", "ViewModel initialized")

    }


    fun showDetails(course: Courses) {
        courseDetails = course

    }

    fun hideDetails() {
        courseDetails = null
    }

    private fun createEntries() {
        viewModelScope.launch {
            realm.write {
                var addresse1 = Addresse().apply {
                    fullName = "ahmed"
                    street = "93 abdeen "
                    houseName = "el muktar"
                    zipCode = "544547"
                    city = "Cario"

                }
                var addresse2 = Addresse().apply {
                    fullName = "mohamed"
                    street = "55 el morease "
                    houseName = "el Daoud"
                    zipCode = "54557"
                    city = "Cario"

                }
                val course1 = Courses().apply {
                    name = "Fuzzy logic"
                }
                val course2 = Courses().apply {
                    name = "neural network"
                }
                val course3 = Courses().apply {
                    name = "Gis"
                }
                val teacher = Teacher().apply {
                    addresse = addresse1
                    courses = realmListOf(course1, course2)
                }
                val teacher2 = Teacher().apply {
                    addresse = addresse2
                    courses = realmListOf(course3)
                }
                course1.teacher = teacher
                course2.teacher = teacher2
                course3.teacher = teacher2
                addresse1.teacher = teacher
                addresse2.teacher = teacher2
                val student1 = Student().apply {
                    name = "abd El monem"
                }
                val student = Student().apply {
                    name = "Abd elrazek"
                }
                course1.enrolledStudent.add(student)
                course2.enrolledStudent.add(student1)
                course3.enrolledStudent.addAll(listOf(student, student1))

                copyToRealm(teacher, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(teacher2, updatePolicy = UpdatePolicy.ALL)


                copyToRealm(course1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course2, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course3, updatePolicy = UpdatePolicy.ALL)


                copyToRealm(student, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(student1, updatePolicy = UpdatePolicy.ALL)


            }
        }
    }
    fun deletecourse() {
        viewModelScope.launch {
            realm.write {
                val course = courseDetails ?: return@write
                val latestcourse = findLatest(course) ?: return@write
                delete(latestcourse)
                courseDetails = null


            }
        }
    }

}