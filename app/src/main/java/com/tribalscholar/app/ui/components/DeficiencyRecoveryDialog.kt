package com.tribalscholar.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tribalscholar.app.data.model.Deficiency
import com.tribalscholar.app.data.model.DeficiencySeverity
import com.tribalscholar.app.data.model.DeficiencyStatus
import com.tribalscholar.app.ui.theme.AccentCyan
import com.tribalscholar.app.ui.theme.PrimaryNavy
import com.tribalscholar.app.ui.theme.StatusErrorRed
import com.tribalscholar.app.ui.theme.StatusPendingAmber
import com.tribalscholar.app.ui.theme.StatusVerifiedGreen
import com.tribalscholar.app.ui.theme.SurfaceBorder
import com.tribalscholar.app.ui.theme.SurfaceCard
import com.tribalscholar.app.ui.theme.TextPrimary
import com.tribalscholar.app.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeficiencyRecoveryDialog(
    deficiencies: List<Deficiency>,
    onDismiss: () -> Unit,
    onResolveDeficiency: (String, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceCard,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = AccentCyan,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Potential Issues Before Submission",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy,
                                fontSize = 17.sp
                            )
                        )
                        Text(
                            text = "AI-assisted deficiency detection & recovery",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Subtitle Guidance
            Text(
                text = "Fix these potential issues before official submission to avoid delays or return of your application.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Deficiencies List
            deficiencies.forEach { def ->
                DeficiencyCard(
                    deficiency = def,
                    onFix = { resolutionNote ->
                        onResolveDeficiency(def.id, resolutionNote)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
            ) {
                Text(
                    text = "Important: Deficiency recovery resolves preventable document and profile issues before final review. This is not approval prediction. Authorized officials conduct official scrutiny.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF64748B),
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    ),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun DeficiencyCard(
    deficiency: Deficiency,
    onFix: (String) -> Unit
) {
    val isResolved = deficiency.status == DeficiencyStatus.RESOLVED

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isResolved) Color(0xFFF0FDF4) else Color(0xFFF8FAFC)
        ),
        border = BorderStroke(
            1.dp,
            if (isResolved) Color(0xFF86EFAC) else SurfaceBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isResolved) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = if (isResolved) StatusVerifiedGreen else when (deficiency.severity) {
                            DeficiencySeverity.HIGH -> StatusErrorRed
                            DeficiencySeverity.MEDIUM -> StatusPendingAmber
                            DeficiencySeverity.LOW -> AccentCyan
                        },
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = deficiency.issue,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 13.sp
                        )
                    )
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (deficiency.status) {
                                DeficiencyStatus.RESOLVED -> Color(0xFFDCFCE7)
                                DeficiencyStatus.IN_PROGRESS -> Color(0xFFE0F2FE)
                                DeficiencyStatus.OPEN -> Color(0xFFFEE2E2)
                                DeficiencyStatus.NEEDS_OFFICER_REVIEW -> Color(0xFFFEF3C7)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = deficiency.status.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = when (deficiency.status) {
                                DeficiencyStatus.RESOLVED -> Color(0xFF166534)
                                DeficiencyStatus.IN_PROGRESS -> Color(0xFF0369A1)
                                DeficiencyStatus.OPEN -> Color(0xFF991B1B)
                                DeficiencyStatus.NEEDS_OFFICER_REVIEW -> Color(0xFF92400E)
                            },
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Evidence: ${deficiency.evidence}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Required Action: ${deficiency.actionRequired}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            )

            if (isResolved && deficiency.resolvedNote != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Resolution: ${deficiency.resolvedNote}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = StatusVerifiedGreen,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                )
            }

            if (!isResolved) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            val note = if (deficiency.category == "DOCUMENTS") {
                                "Attached Bonafide Certificate from Demo Institute of Technology."
                            } else {
                                "Confirmed institutional records use student middle initial 'K.'."
                            }
                            onFix(note)
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryNavy,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = if (deficiency.category == "DOCUMENTS") "Upload Bonafide Now" else "Confirm & Resolve",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
