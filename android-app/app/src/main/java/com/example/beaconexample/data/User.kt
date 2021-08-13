package com.example.beaconexample.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var firstName: String,

    var lastName: String,

    var email: String
)
