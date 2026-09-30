package com.tribalscholar.app.ui.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.tribalscholar.app.data.auth.FirebaseAuthManager
import com.tribalscholar.app.data.auth.PhoneAuthListener
import com.tribalscholar.app.data.model.UserSession
import com.tribalscholar.app.data.repository.MockScholarshipRepository
import com.tribalscholar.app.data.repository.ScholarshipRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class AuthUiState(
    val studentId: String = "",
    val passcodeOrOtp: String = "",
    val isOtpMode: Boolean = true,
    val captchaCode: String = "",
    val captchaInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDocumentSourceLoading: Boolean = false,
    val isDemoLoading: Boolean = false,

    // Real Firebase OTP States
    val mobileNumber: String = "",
    val isSendingOtp: Boolean = false,
    val isOtpSent: Boolean = false,
    val otpDigits: String = "",
    val isVerifyingOtp: Boolean = false,
    val isOtpVerified: Boolean = false,
    val otpFeedbackMessage: String? = null,
    val resendCountdown: Int = 0,
    val canResend: Boolean = false,
    val isDemoVerificationMode: Boolean = true,

    // Consent State
    val isConsentAgreed: Boolean = false
)

class AuthViewModel(
    private val repository: ScholarshipRepository = MockScholarshipRepository.getInstance(),
    private val firebaseAuthManager: FirebaseAuthManager = FirebaseAuthManager.getInstance()
) : ViewModel() {

    companion object {
        const val DEMO_OTP = "123456"
    }

    val sessionState: StateFlow<UserSession> = repository.getSession()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSession(isLoggedIn = false)
        )

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private var countdownJob: Job? = null

    init {
        generateNewCaptcha()
    }

    fun onStudentIdChanged(value: String) {
        if (_uiState.value.isOtpMode) {
            onMobileNumberChanged(value)
        } else {
            _uiState.value = _uiState.value.copy(studentId = value, errorMessage = null)
        }
    }

    fun onMobileNumberChanged(value: String) {
        val digitsOnly = value.filter { it.isDigit() }.take(10)
        _uiState.value = _uiState.value.copy(
            mobileNumber = digitsOnly,
            studentId = digitsOnly,
            errorMessage = null,
            otpFeedbackMessage = null
        )
    }

    fun onPasscodeChanged(value: String) {
        _uiState.value = _uiState.value.copy(passcodeOrOtp = value, errorMessage = null)
    }

    fun onOtpDigitChanged(newOtp: String) {
        val digitsOnly = newOtp.filter { it.isDigit() }.take(6)
        _uiState.value = _uiState.value.copy(
            otpDigits = digitsOnly,
            passcodeOrOtp = digitsOnly,
            errorMessage = null
        )
    }

    fun onCaptchaInputChanged(value: String) {
        _uiState.value = _uiState.value.copy(captchaInput = value, errorMessage = null)
    }

    fun setOtpMode(isOtp: Boolean) {
        countdownJob?.cancel()
        _uiState.value = _uiState.value.copy(
            isOtpMode = isOtp,
            studentId = "",
            mobileNumber = "",
            passcodeOrOtp = "",
            captchaInput = "",
            isOtpSent = false,
            isSendingOtp = false,
            otpDigits = "",
            isVerifyingOtp = false,
            isOtpVerified = false,
            resendCountdown = 0,
            canResend = false,
            errorMessage = null,
            otpFeedbackMessage = null
        )
        generateNewCaptcha()
    }

    fun generateNewCaptcha() {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        val code = (1..5).map { chars[Random.nextInt(chars.length)] }.joinToString("")
        _uiState.value = _uiState.value.copy(captchaCode = code, captchaInput = "")
    }

    fun sendOtp(activity: Activity? = null) {
        val phone = _uiState.value.mobileNumber.ifEmpty { _uiState.value.studentId }.filter { it.isDigit() }
        if (phone.length != 10) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please enter a valid 10-digit mobile number."
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isSendingOtp = true,
            errorMessage = null,
            otpFeedbackMessage = null
        )

        // Temporarily replace blocking real-SMS dependency with Demo Verification fallback
        // Firebase Phone Auth remains preserved in FirebaseAuthManager for future backend activation
        viewModelScope.launch {
            delay(200)
            _uiState.value = _uiState.value.copy(
                isSendingOtp = false,
                isOtpSent = true,
                isDemoVerificationMode = true,
                otpDigits = "",
                passcodeOrOtp = "",
                errorMessage = null
            )
            startResendCountdown()
        }
    }

    /**
     * Preserved modular method for real Firebase Phone Auth.
     * Can be invoked directly once Firebase project SMS authentication is fully provisioned.
     */
    fun sendFirebaseRealSmsOtp(activity: Activity) {
        val phone = _uiState.value.mobileNumber.ifEmpty { _uiState.value.studentId }.filter { it.isDigit() }
        firebaseAuthManager.sendVerificationCode(
            activity = activity,
            raw10DigitNumber = phone,
            listener = object : PhoneAuthListener {
                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    _uiState.value = _uiState.value.copy(
                        isSendingOtp = false,
                        isOtpSent = true,
                        isDemoVerificationMode = false,
                        otpDigits = "",
                        errorMessage = null
                    )
                    startResendCountdown()
                }

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    _uiState.value = _uiState.value.copy(
                        isSendingOtp = false,
                        isVerifyingOtp = true,
                        isOtpVerified = true
                    )
                    viewModelScope.launch {
                        repository.login(phone, "VERIFIED")
                        _uiState.value = _uiState.value.copy(
                            isVerifyingOtp = false,
                            otpFeedbackMessage = "Phone number verified"
                        )
                    }
                }

                override fun onVerificationFailed(errorMessage: String) {
                    _uiState.value = _uiState.value.copy(
                        isSendingOtp = false,
                        isOtpSent = true,
                        isDemoVerificationMode = true,
                        otpDigits = "",
                        errorMessage = null
                    )
                    startResendCountdown()
                }
            }
        )
    }

    private fun startResendCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(resendCountdown = 45, canResend = false)
            for (secondsLeft in 44 downTo 0) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    resendCountdown = secondsLeft,
                    canResend = (secondsLeft == 0)
                )
            }
        }
    }

    fun changePhoneNumber() {
        countdownJob?.cancel()
        _uiState.value = _uiState.value.copy(
            isOtpSent = false,
            isSendingOtp = false,
            otpDigits = "",
            passcodeOrOtp = "",
            resendCountdown = 0,
            canResend = false,
            errorMessage = null,
            otpFeedbackMessage = null
        )
    }

    fun resendOtp(activity: Activity? = null) {
        if (!_uiState.value.canResend && _uiState.value.resendCountdown > 0) {
            return
        }

        _uiState.value = _uiState.value.copy(
            isSendingOtp = true,
            errorMessage = null,
            otpDigits = "",
            passcodeOrOtp = ""
        )

        sendOtp(activity)
    }

    fun verifyOtp(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.otpDigits.length != 6) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "That code doesn't look right. Please try again."
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isVerifyingOtp = true,
            errorMessage = null
        )

        // Check if demo verification code matches
        if (state.otpDigits == DEMO_OTP) {
            viewModelScope.launch {
                delay(200)
                val sessionResult = repository.login(
                    state.mobileNumber.ifEmpty { "ST2026-1042" },
                    "VERIFIED"
                )
                _uiState.value = _uiState.value.copy(
                    isVerifyingOtp = false,
                    isOtpVerified = true,
                    otpFeedbackMessage = "Demo verification verified"
                )
                sessionResult.onSuccess {
                    delay(200)
                    onSuccess()
                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = it.localizedMessage ?: "Verification failed"
                    )
                }
            }
            return
        }

        if (firebaseAuthManager.storedVerificationId != null) {
            firebaseAuthManager.verifySmsCode(state.otpDigits) { result ->
                result.onSuccess {
                    viewModelScope.launch {
                        val sessionResult = repository.login(
                            state.mobileNumber.ifEmpty { "ST2026-1042" },
                            "VERIFIED"
                        )
                        _uiState.value = _uiState.value.copy(
                            isVerifyingOtp = false,
                            isOtpVerified = true,
                            otpFeedbackMessage = "Phone number verified"
                        )
                        sessionResult.onSuccess {
                            delay(300)
                            onSuccess()
                        }.onFailure {
                            _uiState.value = _uiState.value.copy(
                                errorMessage = it.localizedMessage ?: "Verification failed"
                            )
                        }
                    }
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isVerifyingOtp = false,
                        errorMessage = error.localizedMessage ?: "That code doesn't look right. Please try again."
                    )
                }
            }
        } else {
            // When verificationId is not present or in test environment with non-demo OTP
            viewModelScope.launch {
                delay(100)
                _uiState.value = _uiState.value.copy(
                    isVerifyingOtp = false,
                    errorMessage = "That code doesn't look right. Please try again."
                )
            }
        }
    }

    fun useDemoVerification(onSuccess: () -> Unit) {
        _uiState.value = _uiState.value.copy(
            otpDigits = DEMO_OTP,
            passcodeOrOtp = DEMO_OTP,
            isVerifyingOtp = true,
            errorMessage = null
        )
        viewModelScope.launch {
            delay(200)
            val sessionResult = repository.login(
                _uiState.value.mobileNumber.ifEmpty { "ST2026-1042" },
                "VERIFIED"
            )
            _uiState.value = _uiState.value.copy(
                isVerifyingOtp = false,
                isOtpVerified = true,
                otpFeedbackMessage = "Demo verification verified"
            )
            sessionResult.onSuccess {
                delay(200)
                onSuccess()
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    errorMessage = it.localizedMessage ?: "Verification failed"
                )
            }
        }
    }

    fun verifyOtpDirectlyForTesting(validCode: Boolean, onSuccess: () -> Unit) {
        if (validCode) {
            viewModelScope.launch {
                repository.login(_uiState.value.mobileNumber.ifEmpty { "ST2026-1042" }, "VERIFIED")
                _uiState.value = _uiState.value.copy(
                    isVerifyingOtp = false,
                    isOtpVerified = true,
                    otpFeedbackMessage = "Phone number verified"
                )
                onSuccess()
            }
        } else {
            _uiState.value = _uiState.value.copy(
                isVerifyingOtp = false,
                errorMessage = "That code doesn't look right. Please try again."
            )
        }
    }

    fun setConsentAgreed(agreed: Boolean) {
        _uiState.value = _uiState.value.copy(isConsentAgreed = agreed)
    }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.isOtpMode) {
            if (!state.isOtpSent) {
                sendOtp()
            } else {
                verifyOtp(onSuccess)
            }
            return
        }

        // Student ID & Password Mode
        if (state.studentId.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter your Application ID")
            return
        }

        if (state.passcodeOrOtp.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter your password")
            return
        }

        if (state.captchaInput.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter the CAPTCHA code shown")
            return
        }

        if (!state.captchaInput.trim().equals(state.captchaCode.trim(), ignoreCase = true)) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Incorrect CAPTCHA code. Please check and try again.",
                captchaInput = ""
            )
            generateNewCaptcha()
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.login(state.studentId, state.passcodeOrOtp)
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess {
                onSuccess()
            }.onFailure {
                _uiState.value = _uiState.value.copy(errorMessage = it.localizedMessage ?: "Authentication failed")
            }
        }
    }

    fun loginWithDemoAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDemoLoading = true, errorMessage = null)
            delay(500)
            val result = repository.loginWithDemoAccount()
            _uiState.value = _uiState.value.copy(isDemoLoading = false)
            result.onSuccess {
                onSuccess()
            }.onFailure {
                _uiState.value = _uiState.value.copy(errorMessage = "Failed to load account")
            }
        }
    }

    fun loginWithDigitalDocumentSource(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDocumentSourceLoading = true, errorMessage = null)
            delay(800)
            val result = repository.loginWithDemoAccount()
            _uiState.value = _uiState.value.copy(isDocumentSourceLoading = false)
            result.onSuccess {
                onSuccess()
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        countdownJob?.cancel()
        viewModelScope.launch {
            repository.logout()
            _uiState.value = AuthUiState()
            generateNewCaptcha()
            onLoggedOut()
        }
    }
}
