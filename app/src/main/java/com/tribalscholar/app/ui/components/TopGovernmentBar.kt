package com.tribalscholar.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tribalscholar.app.R
import com.tribalscholar.app.ui.theme.AccentCyan
import com.tribalscholar.app.ui.theme.AccentGold
import com.tribalscholar.app.ui.theme.PrimaryNavy
import com.tribalscholar.app.ui.theme.TextOnDark

@Composable
fun TopScholarBridgeBar(
    title: String = "ScholarBridge AI",
    subtitle: String = "Find. Apply. Track. Succeed.",
    showActions: Boolean = true,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onVoiceClick: () -> Unit = {},
    onHelpClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryNavy)
            .statusBarsPadding()
    ) {
        // Modern accent gradient/line
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Modern ScholarBridge AI Student Emblem
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_scholarbridge_logo),
                    contentDescription = "ScholarBridge AI Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextOnDark,
                        fontSize = 18.sp,
                        letterSpacing = 0.5.sp
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    ),
                    maxLines = 1
                )
            }

            if (showActions) {
                // Voice Assistant Shortcut
                IconButton(
                    onClick = onVoiceClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Assistance",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(2.dp))

                // Notifications with badge
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = AccentGold,
                            contentColor = PrimaryNavy
                        ) {
                            Text("2", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                ) {
                    IconButton(
                        onClick = onNotificationClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = TextOnDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(2.dp))

                // Help Shortcut
                IconButton(
                    onClick = onHelpClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Help & Assisted Centre",
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// Backwards compatibility alias

@Composable
fun TopGovernmentBar(
    title: String = "ScholarBridge AI",
    subtitle: String = "Find. Apply. Track. Succeed.",
    showActions: Boolean = true,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onVoiceClick: () -> Unit = {},
    onHelpClick: () -> Unit = {}
) {
    TopScholarBridgeBar(
        title = title,
        subtitle = subtitle,
        showActions = showActions,
        onNotificationClick = onNotificationClick,
        onProfileClick = onProfileClick,
        onVoiceClick = onVoiceClick,
        onHelpClick = onHelpClick
    )
}
