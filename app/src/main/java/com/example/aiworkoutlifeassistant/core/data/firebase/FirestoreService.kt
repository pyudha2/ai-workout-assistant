package com.example.aiworkoutlifeassistant.core.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore
){
    suspend fun saveUserProfile(uid: String, name: String, email: String){
        val userData = mapOf(
            "uid" to uid,
            "name" to name,
            "email" to email
        )
        firestore.collection("users").document(uid).set(userData).await()
    }
}