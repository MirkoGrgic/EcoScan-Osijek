package com.example.ecoscanosijek.repository

import com.example.ecoscanosijek.model.User
import com.example.ecoscanosijek.model.UserRole
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getLeaderboard(): Flow<List<User>> = callbackFlow {
        val subscription = db.collection("users")
            .whereEqualTo("role", UserRole.CITIZEN.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val users = snapshot
                    ?.toObjects(User::class.java)
                    ?.sortedByDescending { it.points }
                    ?: emptyList()

                trySend(users)
            }

        awaitClose { subscription.remove() }
    }
}
