package com.tribalscholar.app.data.auth

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

interface PhoneAuthListener {
    fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken)
    fun onVerificationCompleted(credential: PhoneAuthCredential)
    fun onVerificationFailed(errorMessage: String)
}

class FirebaseAuthManager private constructor() {

    companion object {
        @Volatile
        private var INSTANCE: FirebaseAuthManager? = null

        fun getInstance(): FirebaseAuthManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FirebaseAuthManager().also { INSTANCE = it }
            }
        }
    }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    var storedVerificationId: String? = null
    var storedResendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun sendVerificationCode(
        activity: Activity,
        raw10DigitNumber: String,
        listener: PhoneAuthListener
    ) {
        val fullPhoneNumber = if (raw10DigitNumber.startsWith("+")) {
            raw10DigitNumber
        } else {
            "+91$raw10DigitNumber"
        }

        try {
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    listener.onVerificationCompleted(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    val message = mapFirebaseError(e)
                    listener.onVerificationFailed(message)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    storedVerificationId = verificationId
                    storedResendToken = token
                    listener.onCodeSent(verificationId, token)
                }
            }

            val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(fullPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)

            storedResendToken?.let {
                optionsBuilder.setForceResendingToken(it)
            }

            PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
        } catch (e: Exception) {
            listener.onVerificationFailed(mapFirebaseError(e))
        }
    }

    fun verifySmsCode(
        otpCode: String,
        onResult: (Result<FirebaseUser>) -> Unit
    ) {
        val verificationId = storedVerificationId
        if (verificationId.isNullOrBlank()) {
            onResult(Result.failure(Exception("Your code has expired. Request a new one.")))
            return
        }

        try {
            val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        if (user != null) {
                            onResult(Result.success(user))
                        } else {
                            onResult(Result.failure(Exception("Phone number verified but profile is unavailable.")))
                        }
                    } else {
                        val exception = task.exception
                        val userMessage = mapFirebaseError(exception)
                        onResult(Result.failure(Exception(userMessage)))
                    }
                }
        } catch (e: Exception) {
            onResult(Result.failure(Exception(mapFirebaseError(e))))
        }
    }

    private fun mapFirebaseError(e: Throwable?): String {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> {
                "That code doesn't look right. Please try again."
            }
            is FirebaseTooManyRequestsException -> {
                "Too many attempts. Please try again later."
            }
            is FirebaseNetworkException -> {
                "Couldn't verify right now. Check your connection and try again."
            }
            null -> "Unable to send the code. Please try again."
            else -> {
                val msg = e.localizedMessage ?: e.message ?: ""
                if (msg.contains("quota", ignoreCase = true) || msg.contains("billing", ignoreCase = true)) {
                    "Too many attempts. Please try again later."
                } else if (msg.contains("network", ignoreCase = true) || msg.contains("connection", ignoreCase = true)) {
                    "Couldn't verify right now. Check your connection and try again."
                } else if (msg.contains("format", ignoreCase = true) || msg.contains("invalid phone", ignoreCase = true)) {
                    "Please enter a valid 10-digit mobile number."
                } else {
                    "Unable to verify right now. Please check your connection and try again."
                }
            }
        }
    }
}
