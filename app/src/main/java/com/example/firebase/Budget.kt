package com.example.firebase

import com.google.firebase.firestore.Exclude

data class Budget(
    @set:Exclude @get:Exclude @Exclude var id : String = "",
    var nominal : String = "",
    var description : String = "",
    var date: String = "",
    var alamat: String = "",
    var nama: String = "",
)
