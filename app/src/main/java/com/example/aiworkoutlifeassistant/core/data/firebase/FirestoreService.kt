package com.example.aiworkoutlifeassistant.core.data.firebase

import com.example.aiworkoutlifeassistant.feature.workout.data.model.ChatMessageEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    suspend fun saveChatMessage(uid: String, message: ChatMessageEntity){
        firestore.collection("users").document(uid)
            .collection("workoutChat").add(message).await()
    }

    fun getChatHistory(uid: String): Flow<List<ChatMessageEntity>> = callbackFlow {
        val listener = firestore.collection("users").document(uid)
            .collection("workoutChat")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null){
                    close(error)
                    return@addSnapshotListener
                }
                val message = snapshot?.toObjects(ChatMessageEntity::class.java) ?: emptyList()
                trySend(message)
            }
        awaitClose { listener.remove() }
    }
}