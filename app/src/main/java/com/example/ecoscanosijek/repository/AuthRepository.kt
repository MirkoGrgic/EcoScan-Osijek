package com.example.ecoscanosijek.repository

import com.example.ecoscanosijek.model.User
import com.example.ecoscanosijek.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun login(
        email: String,
        password: String
    ): User? {
        val result =
            auth.signInWithEmailAndPassword(email, password).await()

        return result.user?.let {
            getUserFromFirestore(it.uid)
        }
    }

    suspend fun register(
        email: String,
        password: String,
        username: String
    ): User? {

        val result =
            auth.createUserWithEmailAndPassword(email, password).await()

        return result.user?.let {

            val newUser = User(
                id = it.uid,
                username = username,
                email = email,
                points = 0,
                reportCount = 0,
                resolvedCount = 0,
                role = UserRole.CITIZEN
            )

            saveUserToFirestore(newUser)

            newUser
        }
    }

    suspend fun loginAnonymously(): User? {

        val result = auth.signInAnonymously().await()

        return result.user?.let {
            User(
                id = it.uid,
                username = "Gost",
                email = "",
                points = 0,
                reportCount = 0,
                resolvedCount = 0,
                role = UserRole.CITIZEN
            )
        }
    }

    suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return getUserFromFirestore(firebaseUser.uid)
    }

    /**
     * Real-time listener za trenutno prijavljenog korisnika.
     */
    fun observeCurrentUser(): Flow<User?> = callbackFlow {
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val listener = db.collection("users")
            .document(firebaseUser.uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    trySend(snapshot.toObject(User::class.java))
                } else {
                    trySend(null)
                }
            }

        awaitClose {
            listener.remove()
        }
    }

    suspend fun logout() {
        val firebaseUser = auth.currentUser

        try {
            if (firebaseUser?.isAnonymous == true) {
                firebaseUser.delete().await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            auth.signOut()
        }
    }

    private suspend fun getUserFromFirestore(
        uid: String
    ): User? {
        return try {

            val document = db.collection("users")
                .document(uid)
                .get()
                .await()

            document.toObject(User::class.java)

        } catch (e: Exception) {
            null
        }
    }

    private suspend fun saveUserToFirestore(
        user: User
    ) {
        db.collection("users")
            .document(user.id)
            .set(user)
            .await()
    }
}