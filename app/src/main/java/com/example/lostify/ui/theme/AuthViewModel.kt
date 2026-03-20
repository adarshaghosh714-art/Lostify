package com.example.lostify.ui.theme

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState


    fun login(email: String, password: String) {

        _loginState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    _loginState.value = AuthState.Success
                } else {
                    _loginState.value =
                        AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }
    }



    fun signUp(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }

            }

    }

}



sealed class AuthState {

    object Idle : AuthState()

    object Loading : AuthState()

    object Success : AuthState()

    data class Error(val message: String) : AuthState()
}