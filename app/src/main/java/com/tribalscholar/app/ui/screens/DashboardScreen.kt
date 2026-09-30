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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tribalscholar.app.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tribalscholar.app.data.model.DeficiencyStatus
import com.tribalscholar.app.data.model.ReadinessCategory
import com.tribalscholar.app.data.model.Scholarship
import com.tribalscholar.app.ui.components.AiExplanationModal
import com.tribalscholar.app.ui.components.ApplicationReadinessDialog
import com.tribalscholar.app.ui.components.AssistedCentreDialog
import com.tribalscholar.app.ui.components.CrossDocIntelligenceDialog
import com.tribalscholar.app.ui.components.DeficiencyRecoveryDialog
import com.tribalscholar.app.ui.components.DigitalTwinDialog
import com.tribalscholar.app.ui.components.ExplainableEligibilityDialog
import com.tribalscholar.app.ui.components.NotificationsDialog
import com.tribalscholar.app.ui.components.TopScholarBridgeBar
import com.tribalscholar.app.ui.components.VoiceAssistanceDialog
import com.tribalscholar.app.ui.theme.AccentCyan
import com.tribalscholar.app.ui.theme.PrimaryNavy
import com.tribalscholar.app.ui.theme.StatusVerifiedGreen
import com.tribalscholar.app.ui.theme.SurfaceBorder
import com.tribalscholar.app.ui.theme.SurfaceCard
import com.tribalscholar.app.ui.theme.SurfaceLight
import com.tribalscholar.app.ui.theme.TextPrimary
import com.tribalscholar.app.ui.theme.TextSecondary
import com.tribalscholar.app.ui.viewmodel.DashboardViewModel
import com.tribalscholar.app.ui.viewmodel.InnovationViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    innovationViewModel: InnovationViewModel = viewModel(),
    onNavigateToScholarships: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToApplications: () -> Unit = {},
    onNavigateToDocuments: () -> Unit = {},
    onStartApplication: (String) -> Unit = {}
) {
    val profile by viewModel.profile.collectAsState()
    val activeApplications by viewModel.activeApplications.collectAsState()
    val metrics by viewModel.metrics.collectAsState()
    val readiness by innovationViewModel.readiness.collectAsState()
    val digitalTwin by innovationViewModel.digitalTwin.collectAsState()
    val deficiencies by innovationViewModel.deficiencies.collectAsState()
    val crossDocReport by innovationViewModel.crossDocReport.collectAsState()
    val explainableEligibility by innovationViewModel.explainableEligibility.collectAsState()
    val scrollState = rememberScrollState()

    var showVoiceDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var selectedScholarshipForModal by remember { mutableStateOf<Scholarship?>(null) }

    var showReadinessDialog by remember { mutableStateOf(false) }
    var showDigitalTwinDialog by remember { mutableStateOf(false) }
    var showDeficiencyDialog by remember { mutableStateOf(false) }
    var showCrossDocDialog by remember { mutableStateOf(false) }
    var showEligibilityDialog by remember { mutableStateOf(false) }
    var showAiExplanationModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
    ) {
        TopScholarBridgeBar(
            title = "ScholarBridge AI",
            subtitle = "Find. Apply. Track. Succeed.",
            onNotificationClick = { showNotificationsDialog = true },
            onProfileClick = onNavigateToProfile,
            onVoiceClick = { showVoiceDialog = true },
            onHelpClick = { showHelpDialog = true }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            // Student Welcome (Section 8)
            val studentName = profile?.fullName?.split(" ")?.firstOrNull() ?: "Anitha"
            Text(
                text = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.welcome_back_name, studentName),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    fontSize = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.dashboard_subtitle),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Main CTA: Find My Scholarships (Section 8)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToScholarships),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.find_my_scholarships),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 17.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.find_scholarships_sub),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFCBD5E1),
                                fontSize = 12.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(AccentCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Find Scholarships",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Access Action Shortcuts Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionPill(
                    icon = Icons.Default.Mic,
                    label = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.voice_assist),
                    tint = AccentCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { showVoiceDialog = true }
                )

                QuickActionPill(
                    icon = Icons.Default.Folder,
                    label = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.documents_nav),
                    tint = PrimaryNavy,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToDocuments
                )

                QuickActionPill(
                    icon = Icons.Default.HelpOutline,
                    label = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.get_help),
                    tint = Color(0xFF64748B),
                    modifier = Modifier.weight(1f),
                    onClick = { showHelpDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Scholarship Journey (Unified Student Journey - Section 9 & 38)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { showDigitalTwinDialog = true }),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountTree,
                                contentDescription = null,
                                tint = AccentCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.scholarship_journey),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy,
                                    fontSize = 15.sp
                                )
                            )
                        }

                        Text(
                            text = stringResource(R.string.digital_twin_nav),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AccentCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Clean horizontal progress visualization
                    val journeyStages = listOf(
                        stringResource(R.string.journey_profile),
                        stringResource(R.string.journey_eligibility),
                        stringResource(R.string.journey_documents),
                        stringResource(R.string.journey_verification),
                        stringResource(R.string.journey_review),
                        stringResource(R.string.journey_decision)
                    )
                    val hasSubmittedApp = activeApplications.any { it.currentStatus == com.tribalscholar.app.data.model.ApplicationStage.APPLICATION_SUBMITTED }
                    val currentStageIdx = if (hasSubmittedApp) 3 else 2 // Verification is current stage if submitted, else Documents

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        journeyStages.forEachIndexed { idx, stageName ->
                            val isCompleted = idx < currentStageIdx
                            val isCurrent = idx == currentStageIdx
                            val chipBg = when {
                                isCurrent -> PrimaryNavy
                                isCompleted -> Color(0xFFDCFCE7)
                                else -> Color(0xFFF1F5F9)
                            }
                            val textColor = when {
                                isCurrent -> Color.White
                                isCompleted -> Color(0xFF166534)
                                else -> Color(0xFF64748B)
                            }

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(chipBg)
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color(0xFF166534),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                } else if (isCurrent) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(AccentCyan)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = stageName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                        color = textColor,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            if (idx < journeyStages.size - 1) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "→",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val latestApp = activeApplications.firstOrNull()
                        val (statusSubtitle, stepText) = if (latestApp != null && hasSubmittedApp) {
                            val shortTitle = if (latestApp.scholarshipTitle.length > 25) latestApp.scholarshipTitle.take(25) + "..." else latestApp.scholarshipTitle
                            "$shortTitle • ${latestApp.currentStatus.title}" to "Step 4 of 6"
                        } else {
                            "${stringResource(R.string.journey_documents)} • ${stringResource(R.string.ai_check_pending)}" to "Step 3 of 6"
                        }
                        Text(
                            text = statusSubtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stepText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PrimaryNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Application Readiness Diagnostic Card (Section 10 & 38)
            val readyIssues = readiness?.issues ?: emptyList()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { showReadinessDialog = true }),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.application_readiness),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy,
                                    fontSize = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stringResource(R.string.pre_submission_checklist),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (readyIssues.isNotEmpty()) Color(0xFFFEF3C7) else Color(0xFFDCFCE7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (readyIssues.isNotEmpty()) stringResource(R.string.issues_attention, readyIssues.size) else stringResource(R.string.ready_to_submit),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (readyIssues.isNotEmpty()) Color(0xFFB45309) else Color(0xFF166534),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val profileIssues = readyIssues.filter { it.category == ReadinessCategory.PROFILE }
                    val eligibilityIssues = readyIssues.filter { it.category == ReadinessCategory.ELIGIBILITY }
                    val documentsIssues = readyIssues.filter { it.category == ReadinessCategory.DOCUMENTS }
                    val applicationIssues = readyIssues.filter { it.category == ReadinessCategory.APPLICATION }
                    val consistencyIssues = readyIssues.filter { it.category == ReadinessCategory.CONSISTENCY }

                    DiagnosticItem(
                        category = stringResource(R.string.readiness_profile),
                        status = if (profileIssues.isNotEmpty()) "⚠ ${profileIssues.size} issue" else stringResource(R.string.profile_check_complete),
                        isOk = profileIssues.isEmpty()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    DiagnosticItem(
                        category = stringResource(R.string.readiness_eligibility),
                        status = if (eligibilityIssues.isNotEmpty()) "⚠ ${eligibilityIssues.size} issue" else stringResource(R.string.eligibility_check_checked),
                        isOk = eligibilityIssues.isEmpty()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    DiagnosticItem(
                        category = stringResource(R.string.readiness_documents),
                        status = if (documentsIssues.isNotEmpty()) "⚠ 1 issue" else stringResource(R.string.profile_check_complete),
                        isOk = documentsIssues.isEmpty()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    DiagnosticItem(
                        category = stringResource(R.string.readiness_application),
                        status = if (applicationIssues.isNotEmpty()) "⚠ ${applicationIssues.size} issue" else stringResource(R.string.profile_check_complete),
                        isOk = applicationIssues.isEmpty()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    DiagnosticItem(
                        category = stringResource(R.string.readiness_consistency),
                        status = if (consistencyIssues.isNotEmpty()) "⚠ 2 issues" else stringResource(R.string.profile_check_complete),
                        isOk = consistencyIssues.isEmpty()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { showReadinessDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.primary()
                    ) {
                        Icon(imageVector = Icons.Default.TaskAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.check_readiness), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Next Best Action (Section 17 & 38)
            val submittedApp = activeApplications.firstOrNull { it.currentStatus == com.tribalscholar.app.data.model.ApplicationStage.APPLICATION_SUBMITTED }
            val nextAction = digitalTwin?.nextBestAction
            val actionTitle = if (submittedApp != null) "Track Application: ${submittedApp.scholarshipTitle}" else (nextAction?.title ?: "Upload Bonafide Certificate")
            val actionReason = if (submittedApp != null) "Application ${submittedApp.applicationNo} was submitted successfully and queued for review." else (nextAction?.reason ?: "Your application has one missing document. Upload it now to complete verification readiness.")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.next_best_action),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF166534),
                                letterSpacing = 1.sp,
                                fontSize = 10.sp
                            )
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (submittedApp != null) "SUBMITTED" else stringResource(R.string.action_required),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = actionTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 15.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = actionReason,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF334155),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                if (submittedApp != null) {
                                    onNavigateToApplications()
                                } else {
                                    val target = nextAction?.actionType ?: ""
                                    if (target.contains("DEF") || target.contains("ISSUE", ignoreCase = true)) {
                                        showDeficiencyDialog = true
                                    } else {
                                        onNavigateToDocuments()
                                    }
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.success(),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Icon(imageVector = if (submittedApp != null) Icons.Default.ArrowForward else Icons.Default.FlashOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (submittedApp != null) "Track Status" else stringResource(R.string.fix_now), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Innovation Capabilities Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InnovationChip(
                    icon = Icons.Default.AccountTree,
                    label = stringResource(R.string.digital_twin),
                    tint = AccentCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { showDigitalTwinDialog = true }
                )

                InnovationChip(
                    icon = Icons.Default.Warning,
                    label = "${stringResource(R.string.deficiencies_title)} (${deficiencies.count { it.status != DeficiencyStatus.RESOLVED }})",
                    tint = Color(0xFFD97706),
                    modifier = Modifier.weight(1f),
                    onClick = { showDeficiencyDialog = true }
                )

                InnovationChip(
                    icon = Icons.Default.AutoAwesome,
                    label = "Explain AI",
                    tint = PrimaryNavy,
                    modifier = Modifier.weight(1f),
                    onClick = { showAiExplanationModal = true }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InnovationChip(
                    icon = Icons.Default.Assignment,
                    label = stringResource(R.string.cross_doc_intelligence),
                    tint = PrimaryNavy,
                    modifier = Modifier.weight(1f),
                    onClick = { showCrossDocDialog = true }
                )

                InnovationChip(
                    icon = Icons.Default.CheckCircle,
                    label = stringResource(R.string.explainable_eligibility),
                    tint = StatusVerifiedGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { showEligibilityDialog = true }
                )
            }

            // Live Application Status
            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToApplications),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            tint = AccentCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    if (activeApplications.isNotEmpty()) {
                        val app = activeApplications.first()
                        val activeCount = activeApplications.size
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Active Applications: $activeCount",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryNavy,
                                        fontSize = 13.sp
                                    )
                                )
                                val isSubmitted = app.currentStatus == com.tribalscholar.app.data.model.ApplicationStage.APPLICATION_SUBMITTED
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSubmitted) Color(0xFFDCFCE7) else Color(0xFFE0F2FE))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = app.currentStatus.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSubmitted) Color(0xFF15803D) else Color(0xFF0284C7),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${app.scholarshipTitle} • ${app.applicationNo}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF64748B),
                                    fontSize = 11.sp
                                ),
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    } else {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Active Applications: 0",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = "No active applications yet • Tap to browse scholarships",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF64748B),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Potentially Relevant Scholarships Section (Section 8)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.potentially_relevant_for_you),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy,
                        fontSize = 16.sp
                    )
                )

                Text(
                    text = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.view_all),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = AccentCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.clickable(onClick = onNavigateToScholarships)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card 1: National Fellowship Scheme
            CleanScholarshipCard(
                title = "National Fellowship Scheme",
                award = "₹35,000 / month",
                awardSubtitle = "Plus Annual Contingency Support",
                description = "Based on your education, eligibility information and profile.",
                deadline = "30 Nov 2026",
                onViewDetails = {
                    selectedScholarshipForModal = Scholarship(
                        id = "SCH-001",
                        title = "National Fellowship Scheme",
                        schemeCode = "TS-NFS-2026",
                        ministryAgency = "Higher Education Support Services",
                        category = com.tribalscholar.app.data.model.ScholarshipCategory.HIGHER_EDUCATION,
                        awardAmount = "₹35,000 / month",
                        awardFrequency = "Plus Annual Contingency Support",
                        deadline = "30 Nov 2026",
                        daysRemaining = 64,
                        eligibilitySummary = "Full-time scholars enrolled in degree programs.",
                        detailedEligibility = listOf(
                            "Candidate belongs to eligible community category.",
                            "Enrolled in recognized undergraduate or postgraduate course.",
                            "Annual family income within prescribed limits."
                        ),
                        benefits = listOf(
                            "Monthly fellowship allowance",
                            "Annual study and contingency grant"
                        ),
                        requiredDocuments = listOf(
                            "Community Certificate",
                            "Income Certificate",
                            "Bonafide Certificate"
                        ),
                        matchLabel = "Potentially Relevant"
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Card 2: Post-Matric Scholarship
            CleanScholarshipCard(
                title = "Post-Matric Scholarship",
                award = "Tuition & Maintenance",
                awardSubtitle = "Per Academic Session",
                description = "Matches some of your provided information.",
                deadline = "25 Oct 2026",
                onViewDetails = {
                    selectedScholarshipForModal = Scholarship(
                        id = "SCH-003",
                        title = "Post-Matric Scholarship",
                        schemeCode = "TS-PMS-2026",
                        ministryAgency = "Scholarships & Fellowship Services",
                        category = com.tribalscholar.app.data.model.ScholarshipCategory.POST_MATRIC,
                        awardAmount = "Tuition & Maintenance Support",
                        awardFrequency = "Per Academic Session",
                        deadline = "25 Oct 2026",
                        daysRemaining = 28,
                        eligibilitySummary = "Students in accredited post-matric and degree programs.",
                        detailedEligibility = listOf(
                            "Enrolled in accredited degree or diploma course.",
                            "Family income meets eligibility ceiling.",
                            "Satisfactory academic performance."
                        ),
                        benefits = listOf(
                            "Course maintenance allowance",
                            "Tuition fee reimbursement"
                        ),
                        requiredDocuments = listOf(
                            "Community Certificate",
                            "Academic Marksheet",
                            "Income Certificate"
                        ),
                        matchLabel = "Potentially Relevant"
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Card 3: Higher Education Support
            CleanScholarshipCard(
                title = "Higher Education Support",
                award = "Full Tuition Support",
                awardSubtitle = "Plus Living & Learning Grant",
                description = "Based on your education, eligibility information and profile.",
                deadline = "15 Oct 2026",
                onViewDetails = {
                    selectedScholarshipForModal = Scholarship(
                        id = "SCH-002",
                        title = "Higher Education Support",
                        schemeCode = "TS-HES-2026",
                        ministryAgency = "Scholarships & Fellowship Services",
                        category = com.tribalscholar.app.data.model.ScholarshipCategory.TOP_CLASS,
                        awardAmount = "Full Tuition Support",
                        awardFrequency = "Plus Living & Learning Grant",
                        deadline = "15 Oct 2026",
                        daysRemaining = 18,
                        eligibilitySummary = "Enrolled undergraduate students in technical & AI disciplines.",
                        detailedEligibility = listOf(
                            "Full-time regular student in recognized institution.",
                            "Family income within specified limit.",
                            "Maintained continuous academic enrollment."
                        ),
                        benefits = listOf(
                            "Complete tuition fee assistance",
                            "Books & computer resources grant"
                        ),
                        requiredDocuments = listOf(
                            "Community Certificate",
                            "Bonafide Certificate",
                            "Income Certificate"
                        ),
                        matchLabel = "Potentially Relevant"
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Disclaimer Footer (Section 8)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Based on information provided. Final eligibility is subject to official verification by authorized authorities.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Modal dialogs
    if (selectedScholarshipForModal != null) {
        ScholarshipDetailModal(
            scholarship = selectedScholarshipForModal!!,
            isApplying = false,
            onDismiss = { selectedScholarshipForModal = null },
            onApply = {
                val id = selectedScholarshipForModal?.id ?: "SCH-001"
                selectedScholarshipForModal = null
                onStartApplication(id)
            },
            onBookmarkToggle = {}
        )
    }

    if (showVoiceDialog) {
        VoiceAssistanceDialog(
            onDismiss = { showVoiceDialog = false },
            onNavigateToScholarships = onNavigateToScholarships
        )
    }

    if (showHelpDialog) {
        AssistedCentreDialog(
            onDismiss = { showHelpDialog = false },
            onOpenVoice = { showVoiceDialog = true }
        )
    }

    if (showNotificationsDialog) {
        NotificationsDialog(
            applications = activeApplications,
            onDismiss = { showNotificationsDialog = false }
        )
    }

    if (showReadinessDialog) {
        ApplicationReadinessDialog(
            readiness = readiness,
            onDismiss = { showReadinessDialog = false },
            onFixAction = { actionKey ->
                showReadinessDialog = false
                if (actionKey.contains("DOC") || actionKey.contains("UPLOAD", ignoreCase = true)) {
                    onNavigateToDocuments()
                } else if (actionKey.contains("DEF") || actionKey.contains("ISSUE", ignoreCase = true)) {
                    showDeficiencyDialog = true
                } else {
                    showCrossDocDialog = true
                }
            }
        )
    }

    if (showDigitalTwinDialog) {
        DigitalTwinDialog(
            digitalTwinState = digitalTwin,
            onDismiss = { showDigitalTwinDialog = false },
            onActionClick = { _ ->
                showDigitalTwinDialog = false
                showDeficiencyDialog = true
            }
        )
    }

    if (showDeficiencyDialog) {
        DeficiencyRecoveryDialog(
            deficiencies = deficiencies,
            onDismiss = { showDeficiencyDialog = false },
            onResolveDeficiency = { defId, note ->
                innovationViewModel.resolveDeficiency(defId, note)
            }
        )
    }

    if (showCrossDocDialog) {
        CrossDocIntelligenceDialog(
            report = crossDocReport,
            onDismiss = { showCrossDocDialog = false }
        )
    }

    if (showEligibilityDialog) {
        ExplainableEligibilityDialog(
            graph = explainableEligibility,
            onDismiss = { showEligibilityDialog = false }
        )
    }

    if (showAiExplanationModal) {
        AiExplanationModal(
            onDismiss = { showAiExplanationModal = false }
        )
    }
}

@Composable
private fun DiagnosticItem(category: String, status: String, isOk: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isOk) Color(0xFFF8FAFC) else Color(0xFFFFFBEB))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isOk) Color(0xFF16A34A) else Color(0xFFD97706))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryNavy,
                    fontSize = 13.sp
                )
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isOk) Color(0xFFDCFCE7) else Color(0xFFFEF3C7))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isOk) Color(0xFF166534) else Color(0xFFB45309),
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
private fun InnovationChip(
    icon: ImageVector,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryNavy,
                    fontSize = 11.sp
                ),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun QuickActionPill(
    icon: ImageVector,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryNavy,
                    fontSize = 12.sp
                ),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun CleanScholarshipCard(
    title: String,
    award: String,
    awardSubtitle: String,
    description: String,
    deadline: String,
    onViewDetails: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, SurfaceBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0F2FE))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.potentially_relevant),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AccentCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = award,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 13.sp
                        )
                    )
                    Text(
                        text = "Deadline: $deadline",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            fontSize = 11.sp
                        )
                    )
                }

                Button(
                    onClick = onViewDetails,
                    shape = RoundedCornerShape(8.dp),
                    colors = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.primary(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.view_details),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
