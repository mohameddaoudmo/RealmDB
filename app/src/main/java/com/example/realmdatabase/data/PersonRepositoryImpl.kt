package com.example.realmdatabase.data

import android.util.Log
import com.example.realmdatabase.model.Person
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class PersonRepositoryImpl(val realm: Realm) : PersonRepositiory {
    override fun getData(): Flow<List<Person>> {
        return realm.query<Person>().asFlow().map {
            it.list
        }
    }

    override fun filterData(name: String): Flow<List<Person>> {
        return realm.query<Person>(query = "name CONTAINS[c] $0", name).asFlow().map { it.list }
    }

    override suspend fun insertPerson(person: Person) {
        realm.write { copyToRealm(person) }
    }

    override suspend fun updatePerson(person: Person) {
        realm.write {
            val queriedPerson = query<Person>(query = "_id == $0", person._id).first().find()
            queriedPerson?.name = person.name
        }

    }

    override suspend fun deletePerson(id: ObjectId) {
        realm.write {
            val person = query<Person>(query = "_id == $0", id).first().find()
            person?.let {
                try {
                    delete(it)
                } catch (e: Exception) {
                    Log.d("MongoRepositoryImpl", "${e.message}")
                }
            }
        }
    }
}