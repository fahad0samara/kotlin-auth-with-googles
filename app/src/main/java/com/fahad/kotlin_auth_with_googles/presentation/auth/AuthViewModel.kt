package com.fahad.kotlin_auth_with_googles.presentation.auth

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahad.kotlin_auth_with_googles.domain.model.Response
import com.fahad.kotlin_auth_with_googles.domain.repository.AuthRepository
import com.fahad.kotlin_auth_with_googles.domain.repository.OneTapSignInResponse
import com.fahad.kotlin_auth_with_googles.domain.repository.SignInWithGoogleResponse
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider.getCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val repo: AuthRepository,
  val oneTapClient: SignInClient
): ViewModel() {
  // Expose whether the user is authenticated in Firebase
  val isUserAuthenticated get() = repo.isUserAuthenticatedInFirebase

  // Mutable state variables for one-tap sign-in and sign-in with Google responses
  var oneTapSignInResponse by mutableStateOf<OneTapSignInResponse>(Response.Success(null))
    private set
  var signInWithGoogleResponse by mutableStateOf<SignInWithGoogleResponse>(Response.Success(false))
    private set

  // Coroutine function to perform one-tap sign-in
  fun oneTapSignIn() = viewModelScope.launch {
    oneTapSignInResponse = Response.Loading
    oneTapSignInResponse = repo.oneTapSignInWithGoogle()
  }

  // Coroutine function to perform sign-in with Google
  fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
    signInWithGoogleResponse = Response.Loading
    signInWithGoogleResponse = repo.firebaseSignInWithGoogle(googleCredential)
  }
}