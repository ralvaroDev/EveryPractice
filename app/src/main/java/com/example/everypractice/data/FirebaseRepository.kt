package com.example.everypractice.data

import com.example.everypractice.data.models.login.*
import com.google.android.gms.tasks.*
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.*
import javax.inject.*

class FirebaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun loginWithCredential(loginCredentials: LoginCredentials): Task<AuthResult> {
        val (email, password) = loginCredentials
        return firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    fun signUpWithCredential(loginCredentials: LoginCredentials): Task<AuthResult> {
        val (email, password) = loginCredentials
        //Timber.d("Flow in creating User")
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    fun updateUser(name: String): Task<Void>? {
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
        //Timber.d("Flow in updating user")
        return firebaseAuth.currentUser?.updateProfile(profileUpdates)
    }

}

