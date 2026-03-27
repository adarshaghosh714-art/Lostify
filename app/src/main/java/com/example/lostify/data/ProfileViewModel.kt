package com.example.lostify.data

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {


    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()


    fun fetchUserData() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            _error.value = "User not logged in"
            return
        }

        _isLoading.value = true

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val userData = document.toObject(User::class.java)
                _user.value = userData ?: User()
                _isLoading.value = false
            }
            .addOnFailureListener {
                _error.value = it.message
                _isLoading.value = false
            }
    }


    fun saveUserData(name: String, email: String, phone: String) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            _error.value = "User not logged in"
            return
        }

        val userMap = hashMapOf(
            "name" to name,
            "email" to email,
            "phone" to phone
        )

        _isLoading.value = true

        firestore.collection("users")
            .document(userId)
            .set(userMap)
            .addOnSuccessListener {
                // Update local state after saving
                _user.value = User(name, email, phone)
                _isLoading.value = false
            }
            .addOnFailureListener {
                _error.value = it.message
                _isLoading.value = false
            }
    }


    fun setUserForPreview(user: User) {
        _user.value = user
    }
}