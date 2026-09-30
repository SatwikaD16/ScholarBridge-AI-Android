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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tribalscholar.app.data.model.AuditEvent
import com.tribalscholar.app.data.model.DecisionReplayStep
import com.tribalscholar.app.data.model.OfficerActionType
import com.tribalscholar.app.data.model.OfficerApplicationQueueItem
import com.tribalscholar.app.data.model.ProvenanceNode
import com.tribalscholar.app.data.model.RuleChangeSimulation
import com.tribalscholar.app.data.model.SandboxRule
import com.tribalscholar.app.ui.theme.AccentCyan
import com.tribalscholar.app.ui.theme.AccentGold
import com.tribalscholar.app.ui.theme.PrimaryNavy
import com.tribalscholar.app.ui.theme.StatusErrorRed
import com.tribalscholar.app.ui.theme.StatusPendingAmber
import com.tribalscholar.app.ui.theme.StatusVerifiedGreen
import com.tribalscholar.app.ui.theme.SurfaceBorder
import com.tribalscholar.app.ui.theme.SurfaceCard
import com.tribalscholar.app.ui.theme.SurfaceLight
import com.tribalscholar.app.ui.theme.TextPrimary
import com.tribalscholar.app.ui.theme.TextSecondary
import com.tribalscholar.app.ui.viewmodel.InnovationViewModel

@Composable
fun OfficerPortalScreen(
    viewModel: InnovationViewModel,
    onBackToStudent: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val officerQueue by viewModel.officerQueue.collectAsState()
    val auditEvents by viewModel.auditEvents.collectAsState()

    val tabTitles = listOf(
        "Dashboard",
        "Review Queue",
        "Decision Replay",
        "Provenance Graph",
        "Rule Sandbox & Simulator",
        "Audit Trail"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
    ) {
        // Officer Top Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryNavy)
                .statusBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(AccentCyan)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackToStudent) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Student View",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ScholarBridge AI",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 17.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AccentGold)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "OFFICER PORTAL",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = PrimaryNavy,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                        Text(
                            text = "Scrutiny, Provenance & Rule Evaluation Desk",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Button(
                    onClick = onBackToStudent,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF334155),
                        contentColor = Color.White
                    )
                ) {
                    Text("Student View", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Scrollable Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = PrimaryNavy,
                contentColor = Color.White,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = AccentCyan
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (selectedTab == index) AccentCyan else Color(0xFF94A3B8)
                            )
                        }
                    )
                }
            }
        }

        // Tab Content
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedTab) {
                0 -> OfficerDashboardTab(queue = officerQueue)
                1 -> OfficerReviewQueueTab(
                    queue = officerQueue,
                    onUpdateAction = { appNo, action, notes ->
                        viewModel.updateOfficerAction(appNo, action, notes)
                    }
                )
                2 -> OfficerDecisionReplayTab(viewModel = viewModel)
                3 -> OfficerProvenanceTab(viewModel = viewModel)
                4 -> OfficerRuleSimulatorTab(viewModel = viewModel)
                5 -> OfficerAuditTrailTab(auditEvents = auditEvents)
            }
        }
    }
}

// ==========================================
// TAB 1: OFFICER DASHBOARD
// ==========================================
@Composable
private fun OfficerDashboardTab(queue: List<OfficerApplicationQueueItem>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Scrutiny Overview",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                fontSize = 18.sp
            )
        )
        Text(
            text = "Synthetic demonstration metrics for authorized verification officials",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 11.sp
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OfficerMetricCard("Total Applications", "24", AccentCyan, Modifier.weight(1f))
            OfficerMetricCard("Pending Verification", "4", StatusPendingAmber, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OfficerMetricCard("Deficient / Incomplete", "2", StatusErrorRed, Modifier.weight(1f))
            OfficerMetricCard("Needs Review", "3", Color(0xFFF59E0B), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OfficerMetricCard("Ready for Scrutiny", "12", StatusVerifiedGreen, Modifier.weight(1f))
            OfficerMetricCard("Avg Processing Time", "3.2 Days", PrimaryNavy, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Breakdown Cards
        Text(
            text = "Application Distribution by Stage",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                fontSize = 13.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                StageProgressBar("Application Submitted", 4, 24, AccentCyan)
                StageProgressBar("Document Received", 5, 24, Color(0xFF0284C7))
                StageProgressBar("AI-Assisted Verification", 3, 24, Color(0xFFF59E0B))
                StageProgressBar("Officer Review Desk", 8, 24, PrimaryNavy)
                StageProgressBar("Official Decision", 4, 24, StatusVerifiedGreen)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // District Breakdown
        Text(
            text = "District Distribution (Demo Data)",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                fontSize = 13.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                DistrictRow("Demo District", 9)
                DistrictRow("Nilgiris District", 6)
                DistrictRow("Dindigul District", 5)
                DistrictRow("Salem District", 4)
            }
        }
    }
}

// ==========================================
// TAB 2: OFFICER REVIEW QUEUE
// ==========================================
@Composable
private fun OfficerReviewQueueTab(
    queue: List<OfficerApplicationQueueItem>,
    onUpdateAction: (String, OfficerActionType, String) -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedFilter by remember { mutableStateOf("ALL") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Review Queue (${queue.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy,
                        fontSize = 17.sp
                    )
                )
                Text(
                    text = "Confirm or override AI pre-screening findings",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        queue.forEach { item ->
            OfficerQueueCard(item = item, onUpdateAction = onUpdateAction)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun OfficerQueueCard(
    item: OfficerApplicationQueueItem,
    onUpdateAction: (String, OfficerActionType, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${item.studentName} (${item.studentId})",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 14.sp
                        )
                    )
                    Text(
                        text = "${item.schemeTitle} • ${item.district}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (item.officerAction) {
                                OfficerActionType.CONFIRMED -> Color(0xFFDCFCE7)
                                OfficerActionType.MOVED_TO_SCRUTINY -> Color(0xFFE0F2FE)
                                OfficerActionType.CORRECTION_REQUESTED -> Color(0xFFFEE2E2)
                                else -> Color(0xFFFEF3C7)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = item.officerAction.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = when (item.officerAction) {
                                OfficerActionType.CONFIRMED -> Color(0xFF166534)
                                OfficerActionType.MOVED_TO_SCRUTINY -> Color(0xFF0369A1)
                                OfficerActionType.CORRECTION_REQUESTED -> Color(0xFF991B1B)
                                else -> Color(0xFF92400E)
                            },
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // AI Findings & Review Flags
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Readiness: ${item.readinessStatus}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = TextPrimary)
                    )
                    Text(
                        text = "Eligibility: ${item.eligibilityResult}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = TextPrimary)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Documents: ${item.documentStatus}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = TextPrimary)
                    )
                    Text(
                        text = "Deficiencies: ${item.deficienciesCount}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = TextPrimary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // AI Finding Snippet
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "AI Pre-screening Finding:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 10.sp
                        )
                    )
                    item.aiFindings.forEach { finding ->
                        Text(
                            text = "• $finding",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Officer Action Buttons
            Text(
                text = "Officer Decision Action:",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    fontSize = 11.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = {
                        onUpdateAction(
                            item.applicationNo,
                            OfficerActionType.CONFIRMED,
                            "Officer verified documents and confirmed AI pre-screening findings."
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text("Confirm", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        onUpdateAction(
                            item.applicationNo,
                            OfficerActionType.OVERRIDDEN,
                            "Officer determined name variation is benign middle initial. Overrode flag."
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0284C7),
                        contentColor = Color.White
                    )
                ) {
                    Text("Override", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        onUpdateAction(
                            item.applicationNo,
                            OfficerActionType.CORRECTION_REQUESTED,
                            "Returned to student for additional document attachment."
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF4444),
                        contentColor = Color.White
                    )
                ) {
                    Text("Request Fix", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==========================================
// TAB 3: DECISION REPLAY
// ==========================================
@Composable
private fun OfficerDecisionReplayTab(viewModel: InnovationViewModel) {
    val steps by viewModel.getDecisionReplay("ST2026-1042").collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Decision Replay (Application ST2026-1042)",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                fontSize = 17.sp
            )
        )
        Text(
            text = "Immutable audit of rule versions, extracted evidence, and officer actions",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 11.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        steps.forEachIndexed { index, step ->
            DecisionReplayCard(step = step, isLast = index == steps.lastIndex)
        }
    }
}

@Composable
private fun DecisionReplayCard(step: DecisionReplayStep, isLast: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(AccentCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${step.stepIndex}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy,
                                fontSize = 11.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = step.stepName,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 13.sp
                        )
                    )
                }
                Text(
                    text = step.timestamp,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            ReplayRow("Rule Version", step.ruleVersion)
            ReplayRow("Rule Applied", step.ruleApplied)
            ReplayRow("Evidence Used", step.evidenceUsed)
            ReplayRow("Extracted Data", step.extractedData)
            ReplayRow("Result", step.eligibilityResult)
            ReplayRow("Officer Review", step.officerReview)
            ReplayRow("Recorded Action", step.recordedAction)
        }
    }

    if (!isLast) {
        Box(
            modifier = Modifier
                .padding(start = 24.dp)
                .width(2.dp)
                .height(16.dp)
                .background(AccentCyan.copy(alpha = 0.5f))
        )
    }
}

@Composable
private fun ReplayRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B),
                fontSize = 10.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

// ==========================================
// TAB 4: PROVENANCE GRAPH
// ==========================================
@Composable
private fun OfficerProvenanceTab(viewModel: InnovationViewModel) {
    val nodes by viewModel.getProvenanceGraph("ST2026-1042").collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Application Provenance Graph",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                fontSize = 17.sp
            )
        )
        Text(
            text = "Complete lineage from raw document evidence to official decision",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 11.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        nodes.forEachIndexed { index, node ->
            ProvenanceNodeCard(node = node, isLast = index == nodes.lastIndex)
        }
    }
}

@Composable
private fun ProvenanceNodeCard(node: ProvenanceNode, isLast: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFE0F2FE))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = node.nodeType,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = AccentCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = node.title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = node.detail,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }

    if (!isLast) {
        Box(
            modifier = Modifier
                .padding(start = 24.dp)
                .width(2.dp)
                .height(14.dp)
                .background(Color(0xFFCBD5E1))
        )
    }
}

// ==========================================
// TAB 5: RULE SIMULATOR & SANDBOX
// ==========================================
@Composable
private fun OfficerRuleSimulatorTab(viewModel: InnovationViewModel) {
    val scrollState = rememberScrollState()

    var proposedCeiling by remember { mutableStateOf("₹2,50,000") }
    var simulationResult by remember { mutableStateOf<RuleChangeSimulation?>(null) }

    // Sandbox state
    var sandboxIncome by remember { mutableStateOf("₹2,50,000") }
    var sandboxScore by remember { mutableStateOf("60%") }
    var sandboxTestResult by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Section A: Rule-Change Impact Simulator
        Text(
            text = "Rule-Change Impact Simulator",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                fontSize = 17.sp
            )
        )
        Text(
            text = "Simulate candidate pool shifts before publishing official rule modifications",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 11.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current Rule: Income ≤ ₹2,00,000",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("SIMULATION ONLY", color = Color(0xFF92400E), fontWeight = FontWeight.Bold, fontSize = 9.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = proposedCeiling,
                    onValueChange = { proposedCeiling = it },
                    label = { Text("Proposed Annual Income Ceiling") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        simulationResult = viewModel.runRuleSimulation("Post-Matric Scholarship", proposedCeiling)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy, contentColor = Color.White)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Run Impact Simulation", fontWeight = FontWeight.Bold)
                }

                if (simulationResult != null) {
                    val sim = simulationResult!!
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Projected Impact (${sim.simulationTimestamp})",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SimResultChip("Newly Matching", "+${sim.newlyMatchingCount}", StatusVerifiedGreen, Modifier.weight(1f))
                        SimResultChip("No Longer Matching", "-${sim.noLongerMatchingCount}", StatusErrorRed, Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SimResultChip("Needs Review", "${sim.needsReviewCount}", StatusPendingAmber, Modifier.weight(1f))
                        SimResultChip("No Change", "${sim.noChangeCount}", Color(0xFF64748B), Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section B: Officer Rule Sandbox
        Text(
            text = "Officer Rule Sandbox",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                fontSize = 17.sp
            )
        )
        Text(
            text = "Test synthetic IF-AND-THEN rule conditions against applicant cohort",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 11.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "IF Education Level = Undergraduate AND",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = PrimaryNavy)
                )
                Text(
                    text = "AND Income Ceiling ≤ $sandboxIncome",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = PrimaryNavy)
                )
                Text(
                    text = "AND Minimum Marks ≥ $sandboxScore",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = PrimaryNavy)
                )
                Text(
                    text = "THEN Eligibility = PASS (Synthetic v2.2)",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = StatusVerifiedGreen)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val rule = SandboxRule(
                            schemeName = "Higher Education Support",
                            educationLevel = "Undergraduate",
                            incomeCeiling = sandboxIncome,
                            requiredCertificate = "Community & Bonafide",
                            minScorePercentage = sandboxScore
                        )
                        sandboxTestResult = viewModel.testSandboxRule(rule)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7), contentColor = Color.White)
                ) {
                    Text("Test Rule in Sandbox", fontWeight = FontWeight.Bold)
                }

                if (sandboxTestResult != null) {
                    val (passed, total) = sandboxTestResult!!
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Sandbox Evaluation: $passed of $total synthetic applicants satisfy configured criteria.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = StatusVerifiedGreen,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SimResultChip(label: String, value: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = label, fontSize = 9.sp, color = color, fontWeight = FontWeight.Bold)
            Text(text = value, fontSize = 14.sp, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

// ==========================================
// TAB 6: AUDIT TRAIL
// ==========================================
@Composable
private fun OfficerAuditTrailTab(auditEvents: List<AuditEvent>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Application Audit Trail",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                fontSize = 17.sp
            )
        )
        Text(
            text = "Chronological system activity and officer review journal",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 11.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        auditEvents.forEach { event ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = event.action,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy,
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = event.timestamp,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Actor: ${event.actor}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AccentCyan,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = event.details,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextPrimary,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}

// Shared helper composables
@Composable
private fun OfficerMetricCard(title: String, value: String, accentColor: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    fontSize = 20.sp
                )
            )
        }
    }
}

@Composable
private fun StageProgressBar(stageName: String, count: Int, total: Int, color: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stageName, fontSize = 11.sp, color = TextPrimary)
            Text(text = "$count", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(3.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFFE2E8F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(count.toFloat() / total)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun DistrictRow(districtName: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = districtName, fontSize = 11.sp, color = TextPrimary)
        Text(text = "$count applicants", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = PrimaryNavy)
    }
}
