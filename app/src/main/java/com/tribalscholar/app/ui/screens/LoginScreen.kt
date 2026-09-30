package com.tribalscholar.app.ui.screens

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tribalscholar.app.R
import com.tribalscholar.app.ui.components.AssistedCentreDialog
import com.tribalscholar.app.ui.components.LanguageSelectorDialog
import com.tribalscholar.app.ui.components.TopScholarBridgeBar
import com.tribalscholar.app.ui.theme.AccentCyan
import com.tribalscholar.app.ui.theme.PrimaryNavy
import com.tribalscholar.app.ui.theme.StatusErrorRed
import com.tribalscholar.app.ui.theme.StatusErrorRedBg
import com.tribalscholar.app.ui.theme.SurfaceBorder
import com.tribalscholar.app.ui.theme.SurfaceCard
import com.tribalscholar.app.ui.theme.SurfaceLight
import com.tribalscholar.app.ui.theme.TextPrimary
import com.tribalscholar.app.ui.theme.TextSecondary
import com.tribalscholar.app.ui.viewmodel.AuthViewModel
import com.tribalscholar.app.util.LocaleHelper

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    currentLanguageCode: String = "en",
    onLanguageSelected: (String) -> Unit = {},
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val activity = context as? Activity

    var showHelpDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showTrackDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
    ) {
        // Top ScholarBridge AI Bar with Language Selector
        TopScholarBridgeBar(
            title = stringResource(R.string.app_name),
            subtitle = stringResource(R.string.app_tagline),
            showActions = false,
            onNotificationClick = {},
            onVoiceClick = {},
            onHelpClick = {}
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Language Selection Quick Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, SurfaceBorder),
                    modifier = Modifier.clickable { showLanguageDialog = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Change Language",
                            tint = PrimaryNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val currentLang = LocaleHelper.languages.find { it.code == currentLanguageCode }
                        Text(
                            text = currentLang?.nativeName ?: "English",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Welcome Text
            Text(
                text = stringResource(R.string.welcome_back),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    fontSize = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Main Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, SurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    if (uiState.isOtpMode) {
                        // MOBILE NUMBER / OTP MODE
                        if (!uiState.isOtpSent) {
                            // Step 1: Enter Mobile Number
                            Text(
                                text = stringResource(R.string.mobile_number),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy,
                                    fontSize = 13.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = uiState.mobileNumber,
                                onValueChange = { viewModel.onMobileNumberChanged(it) },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.enter_mobile_number),
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                },
                                leadingIcon = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(start = 12.dp, end = 6.dp)
                                    ) {
                                        Text(
                                            text = "🇮🇳 +91",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = PrimaryNavy,
                                                fontSize = 14.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(1.dp)
                                                .height(20.dp)
                                                .background(SurfaceBorder)
                                        )
                                    }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryNavy,
                                    unfocusedBorderColor = SurfaceBorder,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Send OTP Button
                            Button(
                                onClick = { viewModel.sendOtp(activity) },
                                enabled = !uiState.isSendingOtp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.primary()
                            ) {
                                if (uiState.isSendingOtp) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircularProgressIndicator(
                                            color = Color.White,
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = stringResource(R.string.sending_otp),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                } else {
                                    Text(
                                        text = stringResource(R.string.send_otp),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        } else {
                            // Step 2: OTP Verification Screen
                            Text(
                                text = stringResource(R.string.verify_your_number),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy,
                                    fontSize = 17.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            val maskedPhone = if (uiState.mobileNumber.length >= 10) {
                                "+91 ${uiState.mobileNumber.take(5)} XXXXX"
                            } else {
                                "+91 XXXXX XXXXX"
                            }

                            val subtitleText = if (uiState.isDemoVerificationMode) {
                                "Verification for $maskedPhone"
                            } else {
                                "${stringResource(R.string.otp_subtitle)} $maskedPhone"
                            }

                            Text(
                                text = subtitleText,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Demo Verification Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF0F7FF)
                                ),
                                border = BorderStroke(1.dp, PrimaryNavy.copy(alpha = 0.2f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = PrimaryNavy,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.demo_verification_badge),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    fontSize = 10.sp,
                                                    letterSpacing = 0.5.sp
                                                ),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = stringResource(R.string.demo_verification_mode),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = PrimaryNavy,
                                                fontSize = 13.sp
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = stringResource(R.string.demo_verification_unavailable),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 12.sp
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = { viewModel.useDemoVerification(onLoginSuccess) },
                                        enabled = !uiState.isVerifyingOtp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(42.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.primary()
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = stringResource(R.string.use_demo_verification),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Enter 6-digit code",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryNavy,
                                        fontSize = 12.sp
                                    )
                                )
                                Text(
                                    text = stringResource(R.string.demo_otp_hint),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryNavy,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // 6 Separate OTP Input Boxes
                            SixDigitOtpInput(
                                otpValue = uiState.otpDigits,
                                onOtpChange = { viewModel.onOtpDigitChanged(it) },
                                onComplete = { viewModel.verifyOtp(onLoginSuccess) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Verify & Continue Button
                            Button(
                                onClick = { viewModel.verifyOtp(onLoginSuccess) },
                                enabled = !uiState.isVerifyingOtp && uiState.otpDigits.length == 6,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.primary()
                            ) {
                                if (uiState.isVerifyingOtp) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircularProgressIndicator(
                                            color = Color.White,
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = stringResource(R.string.verifying),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                } else {
                                    Text(
                                        text = stringResource(R.string.verify_and_continue),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Resend OTP & Change Number Controls
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { viewModel.changePhoneNumber() }) {
                                    Text(
                                        text = stringResource(R.string.change_number),
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                if (uiState.resendCountdown > 0) {
                                    Text(
                                        text = stringResource(R.string.resend_code_in, uiState.resendCountdown),
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                } else {
                                    TextButton(
                                        onClick = { viewModel.resendOtp(activity) }
                                    ) {
                                        Text(
                                            text = stringResource(R.string.resend_otp),
                                            color = AccentCyan,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // STUDENT ID & PASSWORD MODE
                        Text(
                            text = stringResource(R.string.student_id_label),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy,
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = uiState.studentId,
                            onValueChange = { viewModel.onStudentIdChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.enter_student_id), fontSize = 13.sp, color = TextSecondary) },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryNavy, modifier = Modifier.size(18.dp))
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryNavy,
                                unfocusedBorderColor = SurfaceBorder,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = stringResource(R.string.password_label),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy,
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = uiState.passcodeOrOtp,
                            onValueChange = { viewModel.onPasscodeChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.enter_password), fontSize = 13.sp, color = TextSecondary) },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryNavy, modifier = Modifier.size(18.dp))
                            },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryNavy,
                                unfocusedBorderColor = SurfaceBorder,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Security CAPTCHA
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.captcha_label),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy,
                                    fontSize = 13.sp
                                )
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEFF6FF))
                                    .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = uiState.captchaCode,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 3.sp,
                                            color = PrimaryNavy
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh CAPTCHA",
                                        tint = AccentCyan,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable { viewModel.generateNewCaptcha() }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = uiState.captchaInput,
                            onValueChange = { viewModel.onCaptchaInputChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.enter_captcha), fontSize = 13.sp, color = TextSecondary) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryNavy,
                                unfocusedBorderColor = SurfaceBorder,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = { viewModel.login(onLoginSuccess) },
                            enabled = !uiState.isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.primary()
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text(
                                    text = stringResource(R.string.sign_in_button),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Error Message Display
                    if (uiState.errorMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(StatusErrorRedBg)
                                .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = StatusErrorRed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = uiState.errorMessage!!,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = StatusErrorRed,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }

                    // Success Feedback Display
                    if (uiState.otpFeedbackMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF0FDF4))
                                .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = uiState.otpFeedbackMessage!!,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF15803D),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Mode Switch: Use Student ID instead / Use Mobile Number
            TextButton(
                onClick = { viewModel.setOtpMode(!uiState.isOtpMode) }
            ) {
                Text(
                    text = if (uiState.isOtpMode) {
                        stringResource(R.string.use_student_id_instead)
                    } else {
                        stringResource(R.string.mobile_number)
                    },
                    color = AccentCyan,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }

            // Continue with Digital Document Source
            OutlinedButton(
                onClick = { viewModel.loginWithDigitalDocumentSource(onLoginSuccess) },
                enabled = !uiState.isDocumentSourceLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SurfaceBorder),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                if (uiState.isDocumentSourceLoading) {
                    CircularProgressIndicator(color = PrimaryNavy, modifier = Modifier.size(18.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = PrimaryNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.continue_digital_doc),
                            color = PrimaryNavy,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer Links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.new_registration),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.clickable { viewModel.loginWithDemoAccount(onLoginSuccess) }
                )

                Text(
                    text = "•",
                    color = SurfaceBorder
                )

                Text(
                    text = stringResource(R.string.track_application),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.clickable { showTrackDialog = true }
                )

                Text(
                    text = "•",
                    color = SurfaceBorder
                )

                Text(
                    text = stringResource(R.string.need_help),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = AccentCyan,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.clickable { showHelpDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Language Selector Dialog
    if (showLanguageDialog) {
        LanguageSelectorDialog(
            currentLanguageCode = currentLanguageCode,
            onLanguageSelected = onLanguageSelected,
            onDismiss = { showLanguageDialog = false }
        )
    }

    // Help Dialog
    if (showHelpDialog) {
        AssistedCentreDialog(onDismiss = { showHelpDialog = false })
    }

    // Track Application Dialog
    if (showTrackDialog) {
        TrackApplicationQuickDialog(
            onDismiss = { showTrackDialog = false },
            onViewSample = {
                showTrackDialog = false
                viewModel.loginWithDemoAccount(onLoginSuccess)
            }
        )
    }
}

/**
 * 6-Digit OTP Box component with auto-focus movement, backspace, and paste support
 */
@Composable
private fun SixDigitOtpInput(
    otpValue: String,
    onOtpChange: (String) -> Unit,
    onComplete: () -> Unit
) {
    val focusRequesters = remember { List(6) { FocusRequester() } }

    LaunchedEffect(Unit) {
        if (otpValue.isEmpty()) {
            focusRequesters.firstOrNull()?.requestFocus()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        for (i in 0 until 6) {
            val digit = otpValue.getOrNull(i)?.toString() ?: ""
            val isFocused = otpValue.length == i

            Box(
                modifier = Modifier
                    .size(width = 46.dp, height = 54.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(
                        width = if (isFocused) 1.8.dp else 1.dp,
                        color = if (isFocused) AccentCyan else SurfaceBorder,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = digit,
                    onValueChange = { input ->
                        val clean = input.filter { it.isDigit() }
                        if (clean.length > 1) {
                            // User pasted full OTP
                            val fullClean = clean.take(6)
                            onOtpChange(fullClean)
                            val nextFocus = (fullClean.length).coerceAtMost(5)
                            focusRequesters[nextFocus].requestFocus()
                            if (fullClean.length == 6) {
                                onComplete()
                            }
                        } else if (clean.length == 1) {
                            val newChars = otpValue.toMutableList()
                            if (i < newChars.size) {
                                newChars[i] = clean.first()
                            } else {
                                newChars.add(clean.first())
                            }
                            val result = newChars.take(6).joinToString("")
                            onOtpChange(result)

                            if (i < 5) {
                                focusRequesters[i + 1].requestFocus()
                            } else {
                                onComplete()
                            }
                        } else if (clean.isEmpty()) {
                            // Backspace pressed
                            val newChars = otpValue.toMutableList()
                            if (i < newChars.size) {
                                newChars.removeAt(i)
                                onOtpChange(newChars.joinToString(""))
                            }
                            if (i > 0) {
                                focusRequesters[i - 1].requestFocus()
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequesters[i])
                        .onKeyEvent { event ->
                            if (event.key == Key.Backspace && digit.isEmpty() && i > 0) {
                                focusRequesters[i - 1].requestFocus()
                                true
                            } else {
                                false
                            }
                        }
                )
            }
        }
    }
}

@Composable
private fun TrackApplicationQuickDialog(
    onDismiss: () -> Unit,
    onViewSample: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.track_application),
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy
            )
        },
        text = {
            Text(
                text = "Track your submitted application ST2026-1042 directly in your dashboard timeline.",
                color = TextSecondary,
                fontSize = 13.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onViewSample,
                colors = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.primary()
            ) {
                Text("View Application", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close), color = TextSecondary)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}
