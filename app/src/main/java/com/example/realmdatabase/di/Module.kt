package com.example.realmdatabase.di

import com.example.realmdatabase.data.PersonRepositiory
import com.example.realmdatabase.data.PersonRepositoryImpl
import com.example.realmdatabase.model.Address
import com.example.realmdatabase.model.Person
import com.example.realmdatabase.model.Pet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                Person::class, Address::class, Pet::class
            )
        )
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideMongoRepository(realm: Realm): PersonRepositiory {
        return PersonRepositoryImpl(realm = realm)
    }

}