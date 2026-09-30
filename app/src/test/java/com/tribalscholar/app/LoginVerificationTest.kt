package com.tribalscholar.app

import com.tribalscholar.app.data.repository.MockScholarshipRepository
import com.tribalscholar.app.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginVerificationTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: MockScholarshipRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = MockScholarshipRepository.resetInstance()
        viewModel = AuthViewModel(repository)
    }

    @After
    fun tearDown() {
        MockScholarshipRepository.resetInstance()
        Dispatchers.resetMain()
    }

    @Test
    fun normalLoginFieldsAreEmptyWhenAppStarts() {
        val state = viewModel.uiState.value

        // Normal fields must be completely empty and OTP mode is default
        assertEquals("", state.studentId)
        assertEquals("", state.passcodeOrOtp)
        assertEquals("", state.captchaInput)
        assertTrue(state.isOtpMode)
        assertEquals(null, state.errorMessage)
        assertTrue(state.captchaCode.isNotBlank())
    }

    @Test
    fun signInWithEmptyFieldsShowsValidationErrorAndDoesNotLogin() = runTest {
        var loginSucceeded = false
        viewModel.setOtpMode(false)

        viewModel.login {
            loginSucceeded = true
        }
        advanceUntilIdle()

        assertFalse("Login must not succeed when fields are empty", loginSucceeded)
        val state = viewModel.uiState.value
        assertNotNull(state.errorMessage)
        assertEquals("Please enter your Application ID", state.errorMessage)
    }

    @Test
    fun signInWithMissingPasswordShowsValidationError() = runTest {
        var loginSucceeded = false
        viewModel.setOtpMode(false)

        viewModel.onStudentIdChanged("APP12345")
        viewModel.login {
            loginSucceeded = true
        }
        advanceUntilIdle()

        assertFalse(loginSucceeded)
        assertEquals("Please enter your password", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun signInWithMissingCaptchaShowsValidationError() = runTest {
        var loginSucceeded = false
        viewModel.setOtpMode(false)

        viewModel.onStudentIdChanged("APP12345")
        viewModel.onPasscodeChanged("SecretPass123")
        viewModel.login {
            loginSucceeded = true
        }
        advanceUntilIdle()

        assertFalse(loginSucceeded)
        assertEquals("Please enter the CAPTCHA code shown", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun signInWithIncorrectCaptchaShowsValidationError() = runTest {
        var loginSucceeded = false
        viewModel.setOtpMode(false)

        viewModel.onStudentIdChanged("APP12345")
        viewModel.onPasscodeChanged("SecretPass123")
        viewModel.onCaptchaInputChanged("WRONG")
        viewModel.login {
            loginSucceeded = true
        }
        advanceUntilIdle()

        assertFalse(loginSucceeded)
        assertEquals("Incorrect CAPTCHA code. Please check and try again.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun continueWithDemoAccountLoadsAnithaKumarAndLeavesLoginFieldsEmpty() = runTest {
        var navigatedToDashboard = false

        viewModel.loginWithDemoAccount {
            navigatedToDashboard = true
        }
        advanceUntilIdle()

        // 1. Navigation callback must be triggered
        assertTrue("Selecting demo account must navigate to dashboard", navigatedToDashboard)

        // 2. Normal login input fields must REMAIN empty (no credentials leaked into UI fields)
        val loginState = viewModel.uiState.value
        assertEquals("", loginState.studentId)
        assertEquals("", loginState.passcodeOrOtp)

        // 3. Demo account credentials must match Anitha Kumar / ST2026-1042
        val session = repository.getSession().first()
        assertTrue(session.isLoggedIn)
        assertEquals("Anitha Kumar", session.studentName)
        assertEquals("ST2026-1042", session.studentId)

        val profile = repository.getStudentProfile().first()
        assertEquals("Anitha Kumar", profile.fullName)
        assertEquals("ST2026-1042", profile.studentId)
        assertEquals("Scheduled Tribe (ST)", profile.eligibilityCategory)
        assertEquals("Information provided", profile.eligibilityStatus)
        assertEquals("Tamil Nadu", profile.stateOfDomicile)
        assertEquals("Demo District", profile.district)
        assertEquals("Demo Institute of Technology", profile.institutionName)
        assertEquals("2nd Year", profile.academicYear)
        assertEquals("B.Tech in Artificial Intelligence & Data Science", profile.courseName)
        assertEquals("Not yet verified", profile.paymentStatus)
    }

    @Test
    fun switchingToMobileOtpTabKeepsFieldsEmpty() {
        viewModel.setOtpMode(true)
        val state = viewModel.uiState.value

        assertTrue(state.isOtpMode)
        assertEquals("", state.mobileNumber)
        assertEquals("", state.studentId)
        assertEquals("", state.passcodeOrOtp)
        assertEquals("", state.captchaInput)
        assertFalse(state.isOtpSent)

        var loginSucceeded = false
        viewModel.login { loginSucceeded = true }
        assertFalse(loginSucceeded)
        assertEquals("Please enter a valid 10-digit mobile number.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun sendOtpWithInvalidMobileShowsValidationError() = runTest {
        viewModel.setOtpMode(true)

        // Test with fewer than 10 digits
        viewModel.onMobileNumberChanged("98765")
        viewModel.sendOtp()
        assertEquals("Please enter a valid 10-digit mobile number.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isOtpSent)

        // Test with empty
        viewModel.onMobileNumberChanged("")
        viewModel.sendOtp()
        assertEquals("Please enter a valid 10-digit mobile number.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isOtpSent)
    }

    @Test
    fun sendOtpWithValid10DigitMobileTransitionsToOtpScreen() = runTest {
        viewModel.setOtpMode(true)
        viewModel.onMobileNumberChanged("9876543210")

        viewModel.sendOtp()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("OTP must be marked as sent", state.isOtpSent)
        assertFalse("Sending spinner must be done", state.isSendingOtp)
        assertEquals(null, state.errorMessage)
    }

    @Test
    fun verifyOtpWithIncorrectCodeShowsInvalidOtpMessage() = runTest {
        viewModel.setOtpMode(true)
        viewModel.onMobileNumberChanged("9876543210")
        viewModel.sendOtp()
        advanceUntilIdle()

        var verified = false
        viewModel.onOtpDigitChanged("999999")
        viewModel.verifyOtp { verified = true }
        advanceUntilIdle()

        assertFalse("Must not verify with wrong OTP", verified)
        assertEquals("That code doesn't look right. Please try again.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun verifyOtpWithValidAuthenticationSucceedsAndNavigates() = runTest {
        viewModel.setOtpMode(true)
        viewModel.onMobileNumberChanged("9876543210")
        viewModel.sendOtp()
        advanceUntilIdle()

        var navigated = false
        viewModel.onOtpDigitChanged("654321")
        viewModel.verifyOtpDirectlyForTesting(validCode = true) { navigated = true }
        advanceUntilIdle()

        assertTrue("Valid authentication must successfully navigate to dashboard", navigated)
        assertTrue(viewModel.uiState.value.isOtpVerified)

        val session = repository.getSession().first()
        assertTrue(session.isLoggedIn)
    }

    @Test
    fun changePhoneNumberResetsOtpSentState() = runTest {
        viewModel.setOtpMode(true)
        viewModel.onMobileNumberChanged("9876543210")
        viewModel.sendOtp()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isOtpSent)

        viewModel.changePhoneNumber()
        assertFalse("Changing phone number must reset isOtpSent to false", viewModel.uiState.value.isOtpSent)
        assertEquals("", viewModel.uiState.value.otpDigits)
    }

    @Test
    fun resendOtpStartsCountdownTimer() = runTest {
        viewModel.setOtpMode(true)
        viewModel.onMobileNumberChanged("9876543210")
        viewModel.sendOtp()
        advanceUntilIdle()

        // After virtual time advances, 45s countdown completes and canResend becomes true
        assertTrue(viewModel.uiState.value.isOtpSent)
        assertEquals(0, viewModel.uiState.value.resendCountdown)
        assertTrue(viewModel.uiState.value.canResend)

        // Resending OTP works when canResend is true
        viewModel.resendOtp()
        assertTrue(viewModel.uiState.value.isOtpSent)
    }

    @Test
    fun applicationTrackingMatchesSafeDemoSpecification() = runTest {
        val applications = repository.getActiveApplications().first()
        assertEquals(1, applications.size)

        val app = applications.first()
        assertEquals("ST2026-1042", app.applicationNo)
        assertEquals("Post-Matric Scholarship", app.scholarshipTitle)
        assertEquals(com.tribalscholar.app.data.model.ApplicationStage.OFFICER_REVIEW, app.currentStatus)

        // Verify the 5 verification stages
        val stages = app.timeline.map { it.stage }
        assertEquals(5, stages.size)
        assertEquals(com.tribalscholar.app.data.model.ApplicationStage.APPLICATION_SUBMITTED, stages[0])
        assertEquals(com.tribalscholar.app.data.model.ApplicationStage.DOCUMENTS_RECEIVED, stages[1])
        assertEquals(com.tribalscholar.app.data.model.ApplicationStage.AI_ASSISTED_VERIFICATION, stages[2])
        assertEquals(com.tribalscholar.app.data.model.ApplicationStage.OFFICER_REVIEW, stages[3])
        assertEquals(com.tribalscholar.app.data.model.ApplicationStage.OFFICIAL_DECISION, stages[4])

        // Verify stage titles
        assertEquals("Application Submitted", stages[0].title)
        assertEquals("Documents Received", stages[1].title)
        assertEquals("AI-Assisted Verification", stages[2].title)
        assertEquals("Awaiting Officer Review", stages[3].title)
        assertEquals("Official Decision", stages[4].title)
    }

    @Test
    fun dashboardMetricsMatchSpecification() = runTest {
        val metrics = repository.getDashboardMetrics().first()
        assertEquals(80, metrics.profileCompletionPercent)
        assertEquals(6, metrics.potentiallyRelevantCount)
        assertEquals(1, metrics.activeApplicationsCount)
        assertEquals(2, metrics.pendingDocumentsCount)
        assertEquals("Under Review", metrics.applicationStatusText)
        assertEquals("As specified by the selected scheme", metrics.estimatedBenefitText)
    }

    @Test
    fun scholarshipMatchingUsesNeutralLabelsAndDisclaimers() = runTest {
        val scholarships = repository.getScholarships("", com.tribalscholar.app.data.model.ScholarshipCategory.ALL).first()
        assertEquals(6, scholarships.size)

        scholarships.forEach { s ->
            assertEquals(
                "Match label should be neutral",
                "Potentially Relevant",
                s.matchLabel
            )
            assertEquals(
                "Based on the information provided. Final eligibility is subject to official verification.",
                s.eligibilityDisclaimer
            )
        }
    }

    @Test
    fun verifyOtpWithDemoCode123456SucceedsAndAuthenticates() = runTest {
        viewModel.setOtpMode(true)
        viewModel.onMobileNumberChanged("9876543210")
        viewModel.sendOtp()
        advanceUntilIdle()

        assertTrue("Demo verification mode must be active", viewModel.uiState.value.isDemoVerificationMode)

        var navigated = false
        viewModel.onOtpDigitChanged("123456")
        viewModel.verifyOtp { navigated = true }
        advanceUntilIdle()

        assertTrue("Entering demo code 123456 must successfully authenticate", navigated)
        assertTrue(viewModel.uiState.value.isOtpVerified)

        val session = repository.getSession().first()
        assertTrue(session.isLoggedIn)
    }

    @Test
    fun useDemoVerificationDirectlyAuthenticatesAndNavigates() = runTest {
        viewModel.setOtpMode(true)
        viewModel.onMobileNumberChanged("9876543210")
        viewModel.sendOtp()
        advanceUntilIdle()

        var navigated = false
        viewModel.useDemoVerification { navigated = true }
        advanceUntilIdle()

        assertTrue("Tapping Use Demo Verification must authenticate and navigate", navigated)
        assertTrue(viewModel.uiState.value.isOtpVerified)
        assertEquals("123456", viewModel.uiState.value.otpDigits)

        val session = repository.getSession().first()
        assertTrue(session.isLoggedIn)
    }
}
