package com.example.recipeapp.data.database

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Returns the data when loaded
 */
class DatabaseViewModel : ViewModel() {
    fun readDataOnce(child: String?, listener: OnGetDataListener) {
        FirebaseDatabase.getInstance().reference.child(child!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    listener.onFailed(databaseError)
                }

                override fun onDataChange(databaseSnapshot: DataSnapshot) {
                    listener.onSuccess(databaseSnapshot)
                }
            })
    }
}