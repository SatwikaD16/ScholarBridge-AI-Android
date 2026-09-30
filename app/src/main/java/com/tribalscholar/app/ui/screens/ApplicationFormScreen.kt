package com.tribalscholar.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tribalscholar.app.data.repository.MockScholarshipRepository
import com.tribalscholar.app.ui.components.TopScholarBridgeBar
import com.tribalscholar.app.ui.theme.AccentCyan
import com.tribalscholar.app.ui.theme.PrimaryNavy
import com.tribalscholar.app.ui.theme.StatusVerifiedGreen
import com.tribalscholar.app.ui.theme.SurfaceBorder
import com.tribalscholar.app.ui.theme.SurfaceCard
import com.tribalscholar.app.ui.theme.SurfaceLight
import com.tribalscholar.app.ui.theme.TextPrimary
import com.tribalscholar.app.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ApplicationFormScreen(
    scholarshipTitle: String = "National Fellowship Scheme",
    scholarshipId: String = "SCH-001",
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val allScholarships by MockScholarshipRepository.getInstance()
        .getScholarships("", com.tribalscholar.app.data.model.ScholarshipCategory.ALL)
        .collectAsState(initial = emptyList())
    val matchedScholarship = allScholarships.find { it.id == scholarshipId }
    val displayTitle = matchedScholarship?.title ?: scholarshipTitle

    var currentStep by remember { mutableIntStateOf(1) }
    var isSubmitting by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }
    var generatedAppNo by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Form states
    var fullName by remember { mutableStateOf("Anitha Kumar") }
    var studentId by remember { mutableStateOf("ST2026-1042") }
    var mobile by remember { mutableStateOf("+91 98765 XXXXX") }
    var email by remember { mutableStateOf("anitha.kumar@example.com") }

    var institution by remember { mutableStateOf("Demo Institute of Technology") }
    var course by remember { mutableStateOf("B.Tech in Artificial Intelligence & Data Science") }
    var year by remember { mutableStateOf("2nd Year") }
    var rollNo by remember { mutableStateOf("ST-2024-AI-042") }

    var income by remember { mutableStateOf("₹1,80,000 / annum") }
    var category by remember { mutableStateOf("Scheduled Tribe (ST)") }
    var domicile by remember { mutableStateOf("Tamil Nadu") }

    val stepTitles = listOf(
        "Personal Details",
        "Education",
        "Eligibility Information",
        "Documents",
        "Review",
        "Submit"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .navigationBarsPadding()
    ) {
        TopScholarBridgeBar(
            title = "ScholarBridge AI",
            subtitle = displayTitle,
            showActions = false
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            // Top Navigation & Step Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (currentStep > 1 && !isSubmitted) {
                            currentStep--
                        } else {
                            onBack()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryNavy
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Step $currentStep of 6: ${stepTitles[currentStep - 1]}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 15.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { currentStep / 6f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AccentCyan,
                        trackColor = Color(0xFFE2E8F0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step Content
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    when (currentStep) {
                        1 -> {
                            // Step 1: Personal Details
                            Text(
                                text = "Personal Details",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            FormReadOnlyField(label = "Full Name", value = fullName)
                            Spacer(modifier = Modifier.height(10.dp))
                            FormReadOnlyField(label = "Student ID", value = studentId)
                            Spacer(modifier = Modifier.height(10.dp))
                            FormReadOnlyField(label = "Mobile Number", value = mobile)
                            Spacer(modifier = Modifier.height(10.dp))
                            FormReadOnlyField(label = "Email Address", value = email)
                        }
                        2 -> {
                            // Step 2: Education
                            Text(
                                text = "Academic & Institution Details",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            FormReadOnlyField(label = "Institution Name", value = institution)
                            Spacer(modifier = Modifier.height(10.dp))
                            FormReadOnlyField(label = "Enrolled Program", value = course)
                            Spacer(modifier = Modifier.height(10.dp))
                            FormReadOnlyField(label = "Academic Year", value = year)
                            Spacer(modifier = Modifier.height(10.dp))
                            FormReadOnlyField(label = "Roll / Registration Number", value = rollNo)
                        }
                        3 -> {
                            // Step 3: Eligibility Information
                            Text(
                                text = "Eligibility Information",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            FormReadOnlyField(label = "Community Category", value = category)
                            Spacer(modifier = Modifier.height(10.dp))
                            FormReadOnlyField(label = "Annual Family Income", value = income)
                            Spacer(modifier = Modifier.height(10.dp))
                            FormReadOnlyField(label = "State of Domicile", value = domicile)

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Eligibility information provided by student for pre-screening.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                        4 -> {
                            // Step 4: Documents
                            Text(
                                text = "Attached Verification Documents",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            AttachedDocPill("Caste Certificate", "caste_certificate.pdf")
                            Spacer(modifier = Modifier.height(10.dp))
                            AttachedDocPill("Income Certificate", "income_cert_2026.pdf")
                            Spacer(modifier = Modifier.height(10.dp))
                            AttachedDocPill("Academic Marksheet", "sem3_marksheet.pdf")
                            Spacer(modifier = Modifier.height(10.dp))
                            AttachedDocPill("Bonafide Certificate", "bonafide_issued.pdf")
                        }
                        5 -> {
                            // Step 5: Review
                            Text(
                                text = "Review Application",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Applicant: $fullName ($studentId)", fontSize = 13.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Scheme: $displayTitle", fontSize = 13.sp, color = AccentCyan)
                            Text("Institution: $institution", fontSize = 12.sp, color = TextSecondary)
                            Text("Program: $course ($year)", fontSize = 12.sp, color = TextSecondary)
                            Text("Income: $income • Category: $category", fontSize = 12.sp, color = TextSecondary)
                            Text("Documents: 4 verified documents attached", fontSize = 12.sp, color = TextSecondary)

                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "By submitting, you declare the information provided is accurate and agree to official verification by authorized authorities.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF475569),
                                    lineHeight = 15.sp
                                )
                            }
                        }
                        6 -> {
                            // Step 6: Submit
                            if (!isSubmitted) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Ready to Submit",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = PrimaryNavy
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Your application is complete and ready for submission to the review process.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))

                                    Button(
                                        onClick = {
                                            if (isSubmitting || isSubmitted) return@Button
                                            coroutineScope.launch {
                                                isSubmitting = true
                                                val result = MockScholarshipRepository.getInstance().applyForScholarship(scholarshipId)
                                                if (result.isSuccess) {
                                                    generatedAppNo = result.getOrNull() ?: "ST2026-1042"
                                                    isSubmitting = false
                                                    isSubmitted = true
                                                    Toast.makeText(context, "Application submitted successfully", Toast.LENGTH_SHORT).show()
                                                    delay(700)
                                                    onFinish()
                                                } else {
                                                    isSubmitting = false
                                                    val errorMsg = result.exceptionOrNull()?.message ?: "Application submission failed. Please try again."
                                                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = PrimaryNavy,
                                            contentColor = Color.White,
                                            disabledContainerColor = PrimaryNavy.copy(alpha = 0.6f),
                                            disabledContentColor = Color.White.copy(alpha = 0.75f)
                                        ),
                                        enabled = !isSubmitting && !isSubmitted
                                    ) {
                                        if (isSubmitting) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp),
                                                color = Color.White,
                                                strokeWidth = 2.dp
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text("Submitting Application...", fontSize = 14.sp, color = Color.White)
                                        } else {
                                            Text("Confirm & Submit Application", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }
                            } else {
                                // Confirmation
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFDCFCE7)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = StatusVerifiedGreen,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text(
                                        text = "Application Submitted!",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = PrimaryNavy,
                                            fontSize = 18.sp
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Application No: ${if (generatedAppNo.isNotEmpty()) generatedAppNo else "ST2026-1042"}",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = AccentCyan,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 13.sp
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Your application for $displayTitle has been received and queued for review. Returning to Home Dashboard...",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Button(
                                        onClick = onFinish,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = PrimaryNavy,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text("Return to Home Dashboard", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }

                    if (currentStep < 6) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (currentStep > 1) {
                                OutlinedButton(
                                    onClick = { currentStep-- },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, PrimaryNavy)
                                ) {
                                    Text("Back", color = PrimaryNavy, fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                            }

                            Button(
                                onClick = { currentStep++ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryNavy,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Continue", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FormReadOnlyField(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFF64748B),
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF8FAFC))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = PrimaryNavy,
                    fontSize = 13.sp
                )
            )
        }
    }
}

@Composable
private fun AttachedDocPill(title: String, filename: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = StatusVerifiedGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                )
                Text(
                    text = filename,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
