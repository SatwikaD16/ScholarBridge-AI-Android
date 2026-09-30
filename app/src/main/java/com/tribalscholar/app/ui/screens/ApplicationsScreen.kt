package com.tribalscholar.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.tribalscholar.app.R
import com.tribalscholar.app.data.model.ApplicationItem
import com.tribalscholar.app.data.model.ApplicationStage
import com.tribalscholar.app.ui.components.TopScholarBridgeBar
import com.tribalscholar.app.ui.theme.AccentCyan
import com.tribalscholar.app.ui.theme.PrimaryNavy
import com.tribalscholar.app.ui.theme.StatusVerifiedGreen
import com.tribalscholar.app.ui.theme.SurfaceBorder
import com.tribalscholar.app.ui.theme.SurfaceCard
import com.tribalscholar.app.ui.theme.SurfaceLight
import com.tribalscholar.app.ui.theme.TextPrimary
import com.tribalscholar.app.ui.theme.TextSecondary
import com.tribalscholar.app.ui.viewmodel.DashboardViewModel

@Composable
fun ApplicationsScreen(
    viewModel: DashboardViewModel,
    onNavigateToScholarships: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    androidx.activity.compose.BackHandler {
        onNavigateToHome()
    }
    val applications by viewModel.activeApplications.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
    ) {
        TopScholarBridgeBar(
            title = "ScholarBridge AI",
            subtitle = "Applications",
            showActions = false
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Text(
                text = "Applications & Tracking",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    fontSize = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Track the review stages of your submitted scholarship applications.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (applications.isEmpty()) {
                // Empty state (Section 24)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assignment,
                                contentDescription = null,
                                tint = PrimaryNavy,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "No applications yet",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Your submitted applications will appear here.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onNavigateToScholarships,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryNavy,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.tribalscholar.app.R.string.find_scholarships),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                applications.forEach { app ->
                    ApplicationTrackingCard(app = app)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ApplicationTrackingCard(app: ApplicationItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, SurfaceBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            val isSubmitted = app.currentStatus == com.tribalscholar.app.data.model.ApplicationStage.APPLICATION_SUBMITTED
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.scholarshipTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Application No: ${app.applicationNo}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSubmitted) Color(0xFFDCFCE7) else Color(0xFFFEF3C7))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = app.currentStatus.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isSubmitted) Color(0xFF15803D) else Color(0xFFD97706),
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Modern Timeline (Section 15)
            Text(
                text = "Application Stages",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    fontSize = 13.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            val timelineStages = if (app.timeline.isNotEmpty()) {
                app.timeline.map { milestone ->
                    Triple(milestone.stage.title, milestone.date, milestone.isCompleted to milestone.isCurrent)
                }
            } else {
                listOf(
                    Triple(stringResource(R.string.stage_submitted), "04-Aug-2026", true to false),
                    Triple(stringResource(R.string.stage_docs_received), "12-Aug-2026", true to false),
                    Triple(stringResource(R.string.stage_ai_verification), "25-Aug-2026", true to false),
                    Triple(stringResource(R.string.stage_officer_review), "Awaiting officer review", false to true),
                    Triple(stringResource(R.string.stage_official_decision), "Pending official decision", false to false)
                )
            }

            timelineStages.forEachIndexed { index, (stageTitle, dateStr, statusPair) ->
                val (isCompleted, isCurrent) = statusPair
                TimelineStageRow(
                    title = stageTitle,
                    subtext = dateStr,
                    isCompleted = isCompleted,
                    isCurrent = isCurrent,
                    isLast = index == timelineStages.size - 1
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Official Review Callout (Section 19)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSubmitted) Color(0xFFF0FDF4) else Color(0xFFF8FAFC))
                    .border(1.dp, if (isSubmitted) Color(0xFFBBF7D0) else Color(0xFFCBD5E1), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = if (isSubmitted) Icons.Default.CheckCircle else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (isSubmitted) StatusVerifiedGreen else AccentCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isSubmitted) "Application submitted successfully." else "Application is under official review.",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy,
                                fontSize = 11.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isSubmitted)
                                "Your application has been received and queued for review. Status updates will reflect here as processing continues."
                            else
                                "Authorized officials review applications and make the final decision. Status updates are published as official reviews complete.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineStageRow(
    title: String,
    subtext: String,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> StatusVerifiedGreen
                            isCurrent -> AccentCyan
                            else -> Color(0xFFE2E8F0)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                } else if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF94A3B8))
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(28.dp)
                        .background(if (isCompleted) StatusVerifiedGreen else Color(0xFFCBD5E1))
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.padding(bottom = if (!isLast) 12.dp else 0.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isCompleted || isCurrent) FontWeight.Bold else FontWeight.Medium,
                    color = if (isCompleted || isCurrent) TextPrimary else Color(0xFF94A3B8),
                    fontSize = 13.sp
                )
            )
            Text(
                text = subtext,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (isCurrent) AccentCyan else TextSecondary,
                    fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            )
        }
    }
}
