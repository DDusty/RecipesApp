package com.example.recipeapp.data.database

import androidx.annotation.NonNull
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

interface OnGetDataListener {
    fun onSuccess(@NonNull data: DataSnapshot)
    fun onFailed(@NonNull databaseError: DatabaseError)
}