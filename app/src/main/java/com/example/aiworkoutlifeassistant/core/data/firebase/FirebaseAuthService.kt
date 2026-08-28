package com.example.aiworkoutlifeassistant.core.data.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
){
    suspend fun register(email: String, password: String): String {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password)
        return result.await().user?.uid ?: throw Exception("Register Gagal, UID tidak ditemukan")
    }

    suspend fun login(email: String, password: String): String{
        val result = firebaseAuth.signInWithEmailAndPassword(email, password)
        return result.await().user?.uid ?: throw Exception("Login Gagal, Email tidak ditemukan")
    }

    fun logout(){
        val result = firebaseAuth.signOut()
        return result
    }
}