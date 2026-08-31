package com.example.aiworkoutlifeassistant.feature.auth.data.repository

import com.example.aiworkoutlifeassistant.core.data.firebase.FirebaseAuthService
import com.example.aiworkoutlifeassistant.core.data.firebase.FirestoreService
import com.example.aiworkoutlifeassistant.core.utils.Resource
import com.example.aiworkoutlifeassistant.feature.auth.domain.model.User
import com.example.aiworkoutlifeassistant.feature.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthService: FirebaseAuthService,
    private val firebaseAuth: FirebaseAuth,
    private val firestoreService: FirestoreService
) : AuthRepository {
    override fun register(name: String, email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val uid = firebaseAuthService.register(email, password)
            firestoreService.saveUserProfile(uid, name, email)
            emit(Resource.Success(User(uid, name, email)))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Terjadi kesalahan saat register"))
        }
    }

    override fun login(email: String, password: String): Flow<Resource<User>> = flow{
        emit(Resource.Loading())
        try{
            val uid = firebaseAuthService.login(email, password)
            emit(Resource.Success(User(uid, "", email)))
        } catch (e: Exception){
            emit(Resource.Error(e.message ?: "Email atau Password Salah"))
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}