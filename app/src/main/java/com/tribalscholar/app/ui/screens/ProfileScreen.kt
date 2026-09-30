package com.tribalscholar.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tribalscholar.app.ui.components.AssistedCentreDialog
import com.tribalscholar.app.ui.components.PrivacySecurityDialog
import com.tribalscholar.app.ui.components.TopScholarBridgeBar
import com.tribalscholar.app.ui.theme.AccentCyan
import com.tribalscholar.app.ui.theme.PrimaryNavy
import com.tribalscholar.app.ui.theme.SurfaceBorder
import com.tribalscholar.app.ui.theme.SurfaceCard
import com.tribalscholar.app.ui.theme.SurfaceLight
import com.tribalscholar.app.ui.theme.TextOnDark
import com.tribalscholar.app.ui.theme.TextPrimary
import com.tribalscholar.app.ui.theme.TextSecondary
import com.tribalscholar.app.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    currentLanguageCode: String = "en",
    onLanguageSelected: (String) -> Unit = {},
    onNavigateToOfficerPortal: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onLogout: () -> Unit
) {
    androidx.activity.compose.BackHandler {
        onNavigateToHome()
    }
    val profile by viewModel.profile.collectAsState()
    val scrollState = rememberScrollState()

    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
    ) {
        TopScholarBridgeBar(
            title = "ScholarBridge AI",
            subtitle = "Profile",
            showActions = false
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            // Profile Header Card (Section 22)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AK",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = profile?.fullName ?: "Anitha Kumar",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = TextOnDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Student ID: ${profile?.studentId ?: "ST2026-1042"}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "B.Tech AI & Data Science • 2nd Year",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    )

                    Text(
                        text = "Demo Institute of Technology • Tamil Nadu",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Neutral Wording (Section 7)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F2642))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Eligibility information — Provided by student",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF93C5FD),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Student Information Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Student Profile Information",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 14.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    InfoRow("Full Name", profile?.fullName ?: "Anitha Kumar")
                    InfoRow("Student ID", profile?.studentId ?: "ST2026-1042")
                    InfoRow("Institution", profile?.institutionName ?: "Demo Institute of Technology")
                    InfoRow("Course / Program", profile?.courseName ?: "B.Tech in AI & Data Science")
                    InfoRow("Academic Year", profile?.academicYear ?: "2nd Year")
                    InfoRow("State / District", "${profile?.district ?: "Demo District"}, ${profile?.stateOfDomicile ?: "Tamil Nadu"}")
                    InfoRow("Community Category", profile?.eligibilityCategory ?: "Scheduled Tribe (ST)")
                    InfoRow("Annual Family Income", profile?.familyIncome ?: "₹1,80,000")
                    InfoRow("Preferred Language", "English")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Options List (Section 22)
            Text(
                text = "Account & Settings",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            val currentLangName = com.tribalscholar.app.util.LocaleHelper.languages.find { it.code == currentLanguageCode }?.nativeName ?: "English"

            ProfileOptionRow(
                icon = Icons.Default.Edit,
                title = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.edit_profile),
                subtitle = "Update contact and academic information",
                onClick = { showEditDialog = true }
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileOptionRow(
                icon = Icons.Default.Language,
                title = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.preferred_language),
                subtitle = "$currentLangName (Selected)",
                onClick = { showLanguageDialog = true }
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileOptionRow(
                icon = Icons.Default.Security,
                title = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.privacy_security),
                subtitle = "Learn how your student data is protected",
                onClick = { showPrivacyDialog = true }
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileOptionRow(
                icon = Icons.Default.HelpOutline,
                title = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.help_support),
                subtitle = "Assisted centre, voice access & FAQs",
                onClick = { showHelpDialog = true }
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileOptionRow(
                icon = Icons.Default.Info,
                title = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.about_scholarbridge),
                subtitle = "Find. Apply. Track. Succeed. (v2.0)",
                onClick = { showAboutDialog = true }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Officer Portal Entry (Authorized Demo Access - Section 4 & 24)
            Text(
                text = "Officer Administration (Authorized Access)",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToOfficerPortal),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = BorderStroke(1.dp, Color(0xFF86EFAC))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Officer Review & Scrutiny Portal",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534),
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = "Review queue, decision replay, provenance & rule sandbox",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF15803D),
                                fontSize = 11.sp
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Sign Out Button (Section 22)
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFCBD5E1))
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.sign_out),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showPrivacyDialog) {
        PrivacySecurityDialog(onDismiss = { showPrivacyDialog = false })
    }

    if (showHelpDialog) {
        AssistedCentreDialog(onDismiss = { showHelpDialog = false })
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text(androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.about_scholarbridge), fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("ScholarBridge AI v2.0", fontWeight = FontWeight.Bold, color = PrimaryNavy)
                    Text("Find. Apply. Track. Succeed.", color = AccentCyan, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "ScholarBridge AI is an independent, student-first scholarship companion designed to simplify discovering programs, attaching documents, and tracking application progress.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text(androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.close), fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showLanguageDialog) {
        com.tribalscholar.app.ui.components.LanguageSelectorDialog(
            currentLanguageCode = currentLanguageCode,
            onLanguageSelected = onLanguageSelected,
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showEditDialog) {
        var editIncome by remember { mutableStateOf(profile?.familyIncome ?: "₹1,80,000 / annum") }
        var editCourse by remember { mutableStateOf(profile?.courseName ?: "B.Tech in Artificial Intelligence & Data Science") }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text("Edit Profile & Eligibility", fontWeight = FontWeight.Bold, color = PrimaryNavy)
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = "Continuous Eligibility Re-evaluation: Updating your profile will automatically re-evaluate configured scheme criteria and update your Digital Twin.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Annual Family Income", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryNavy)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = editIncome,
                        onValueChange = { editIncome = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Course / Program", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryNavy)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = editCourse,
                        onValueChange = { editCourse = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "DEMO DATA • SYNTHETIC ONLY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        profile?.let { p ->
                            viewModel.updateProfile(
                                p.copy(
                                    familyIncome = editIncome,
                                    courseName = editCourse
                                )
                            )
                        }
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text("Save & Re-evaluate", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 12.sp),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 12.sp),
            modifier = Modifier.weight(1.3f)
        )
    }
}

@Composable
private fun ProfileOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryNavy,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
