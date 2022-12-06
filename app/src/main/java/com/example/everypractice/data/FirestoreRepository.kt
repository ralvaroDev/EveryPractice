package com.example.everypractice.data

import com.google.firebase.auth.*
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.*
import timber.log.*
import javax.inject.*

class FirestoreRepository @Inject constructor(
    firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val collection = firestore.collection("users")

    fun createUserData(userData: UserData) {
        collection.document(firebaseAuth.currentUser!!.uid)
            .set(userData)
            .addOnSuccessListener {
                Timber.d("The user data has been created!")
            }
    }

    fun addMovieSaved(idMovie: Int) {
        collection
            .document(firebaseAuth.currentUser!!.uid)
            .update("savedMovies", FieldValue.arrayUnion(idMovie))
    }

    fun deleteMovieSaved(idMovie: Int) {
        collection
            .document(firebaseAuth.currentUser!!.uid)
            .update("savedMovies", FieldValue.arrayRemove(idMovie))
    }

    fun obtainInitialDataFromUser() {
        collection.document(firebaseAuth.currentUser!!.uid)
            .get().addOnSuccessListener {
                val user = it.toObject<UserData>()
            }
    }

}

class FirestoreUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
){
    fun setNewUser(userData: UserData) = firestoreRepository.createUserData(userData)

    fun addNewMovie(idMovie: Int){
        firestoreRepository.addMovieSaved(idMovie)
    }

    fun deleteNewMovie(idMovie: Int){
        firestoreRepository.deleteMovieSaved(idMovie)
    }

}

data class UserData(
    val alias: String,
    val savedMovies: List<Int>,
    val seenMovies: List<Int>,
    val iudFriends: List<Int>,
    @field:JvmField
    val isVerified: Boolean,
    @field:JvmField
    val isVIP: Boolean,
)