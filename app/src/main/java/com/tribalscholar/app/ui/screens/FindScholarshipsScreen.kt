package com.tribalscholar.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tribalscholar.app.R
import com.tribalscholar.app.data.model.Scholarship
import com.tribalscholar.app.data.model.ScholarshipCategory
import com.tribalscholar.app.ui.components.ScholarshipCard
import com.tribalscholar.app.ui.components.TopScholarBridgeBar
import com.tribalscholar.app.ui.theme.AccentCyan
import com.tribalscholar.app.ui.theme.PrimaryNavy
import com.tribalscholar.app.ui.theme.StatusVerifiedGreen
import com.tribalscholar.app.ui.theme.SurfaceBorder
import com.tribalscholar.app.ui.theme.SurfaceCard
import com.tribalscholar.app.ui.theme.SurfaceLight
import com.tribalscholar.app.ui.theme.TextPrimary
import com.tribalscholar.app.ui.theme.TextSecondary
import com.tribalscholar.app.ui.viewmodel.ScholarshipViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FindScholarshipsScreen(
    viewModel: ScholarshipViewModel,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onStartApplication: (String) -> Unit = {}
) {
    androidx.activity.compose.BackHandler {
        onNavigateToHome()
    }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val allScholarships by viewModel.scholarships.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: AI Match Finder, 1: Browse All

    // AI Matching Form State (Section 9)
    var eduLevel by remember { mutableStateOf("Undergraduate") }
    var course by remember { mutableStateOf("B.Tech (AI & Data Science)") }
    var year by remember { mutableStateOf("2nd Year") }
    var stateOfDomicile by remember { mutableStateOf("Tamil Nadu") }
    var district by remember { mutableStateOf("Demo District") }
    var income by remember { mutableStateOf("Below ₹2.5 Lakhs") }
    var communityInfo by remember { mutableStateOf("Scheduled Tribe (ST)") }
    var academicScore by remember { mutableStateOf("Above 80% / 8.5 CGPA") }

    var isAnalyzing by remember { mutableStateOf(false) }
    var hasAnalyzed by remember { mutableStateOf(false) }

    var selectedScholarshipForModal by remember { mutableStateOf<Scholarship?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
    ) {
        TopScholarBridgeBar(
            title = "ScholarBridge AI",
            subtitle = "Find Scholarships",
            showActions = false
        )

        // Tab Selector: AI-Assisted Match vs Browse All
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = SurfaceCard,
            contentColor = PrimaryNavy,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = AccentCyan
                )
            }
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (selectedTabIndex == 0) AccentCyan else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Find My Scholarships",
                            fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            )

            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = {
                    Text(
                        text = "Browse All Schemes",
                        fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            )
        }

        if (selectedTabIndex == 0) {
            // Dedicated AI Matching View (Section 9)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Find My Scholarships",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Answer a few questions and we'll identify scholarships that may match your profile.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Questions Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            MatchingQuestionSelector(
                                label = "Education Level",
                                options = listOf("Undergraduate", "Postgraduate", "Diploma"),
                                selected = eduLevel,
                                onSelect = { eduLevel = it }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            MatchingQuestionSelector(
                                label = "Course / Stream",
                                options = listOf("B.Tech (AI & Data Science)", "Engineering", "Arts & Science", "Medicine"),
                                selected = course,
                                onSelect = { course = it }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            MatchingQuestionSelector(
                                label = "Year of Study",
                                options = listOf("1st Year", "2nd Year", "3rd Year", "Final Year"),
                                selected = year,
                                onSelect = { year = it }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            MatchingQuestionSelector(
                                label = "State of Domicile",
                                options = listOf("Tamil Nadu", "Maharashtra", "Karnataka", "Other"),
                                selected = stateOfDomicile,
                                onSelect = { stateOfDomicile = it }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            MatchingQuestionSelector(
                                label = "Annual Family Income",
                                options = listOf("Below ₹2.5 Lakhs", "₹2.5L - ₹5L", "Above ₹5L"),
                                selected = income,
                                onSelect = { income = it }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            MatchingQuestionSelector(
                                label = "Community / Eligibility Category",
                                options = listOf("Scheduled Tribe (ST)", "Other Category"),
                                selected = communityInfo,
                                onSelect = { communityInfo = it }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            MatchingQuestionSelector(
                                label = "Academic Performance",
                                options = listOf("Above 80% / 8.5 CGPA", "60% - 80%", "Below 60%"),
                                selected = academicScore,
                                onSelect = { academicScore = it }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        isAnalyzing = true
                                        hasAnalyzed = false
                                        delay(900)
                                        isAnalyzing = false
                                        hasAnalyzed = true
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.primary(),
                                enabled = !isAnalyzing
                            ) {
                                if (isAnalyzing) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(stringResource(R.string.analyzing_profile), fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(stringResource(R.string.find_my_scholarships_title), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                if (hasAnalyzed) {
                    item {
                        Text(
                            text = "Potential Matches Found (3)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    val matchedResults = listOf(
                        Triple(
                            "National Fellowship Scheme",
                            "TS-NFS-2026",
                            listOf("✓ Education level (Undergraduate)", "✓ Course (B.Tech AI & Data Science)", "✓ Income range (Below ₹2.5L)", "✓ Eligibility information (ST)")
                        ),
                        Triple(
                            "Higher Education Support",
                            "TS-HES-2026",
                            listOf("✓ Education level (Undergraduate)", "✓ Course (Engineering & Tech)", "✓ Income range (Below ₹2.5L)", "✓ Academic record (>80%)")
                        ),
                        Triple(
                            "Post-Matric Scholarship",
                            "TS-PMS-2026",
                            listOf("✓ Education level (Post-matric degree)", "✓ Enrolled in recognized institute", "✓ Income within ceiling limit")
                        )
                    )

                    items(matchedResults) { (title, code, matchReasons) ->
                        AiMatchResultCard(
                            title = title,
                            code = code,
                            matchReasons = matchReasons,
                            onViewDetails = {
                                selectedScholarshipForModal = allScholarships.find { it.title.contains(title.split(" ").first()) } ?: allScholarships.first()
                            },
                            onStartApplication = {
                                onStartApplication(code)
                            }
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        } else {
            // Browse All Schemes Mode
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Search schemes or keywords...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontSize = 13.sp)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = PrimaryNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryNavy,
                        unfocusedBorderColor = SurfaceBorder
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ScholarshipCategory.values().forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { viewModel.onCategorySelected(category) },
                            label = { Text(category.displayName, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryNavy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Scholarship List
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(allScholarships) { item ->
                        ScholarshipCard(
                            scholarship = item,
                            onViewDetails = { selectedScholarshipForModal = item },
                            onBookmarkToggle = { viewModel.toggleBookmark(item.id) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }

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
            onBookmarkToggle = { viewModel.toggleBookmark(selectedScholarshipForModal!!.id) }
        )
    }
}

@Composable
private fun MatchingQuestionSelector(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                fontSize = 12.sp
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            options.forEach { opt ->
                val isSelected = selected == opt
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) PrimaryNavy else Color(0xFFF1F5F9))
                        .clickable { onSelect(opt) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = opt,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isSelected) Color.White else PrimaryNavy,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun AiMatchResultCard(
    title: String,
    code: String,
    matchReasons: List<String>,
    onViewDetails: () -> Unit,
    onStartApplication: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, SurfaceBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        text = code,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0F2FE))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Potential Match",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AccentCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Why it may match:",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    fontSize = 12.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            matchReasons.forEach { reason ->
                Text(
                    text = reason,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF15803D),
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Missing information / disclaimer (Section 9)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(8.dp)
            ) {
                Text(
                    text = "Note: AI-assisted matching pre-screens provided data. Final eligibility requires official verification.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    shape = RoundedCornerShape(8.dp),
                    border = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.outlinedBorder,
                    colors = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.outlined()
                ) {
                    Text(stringResource(R.string.view_details), fontSize = 12.sp, color = PrimaryNavy, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onStartApplication,
                    shape = RoundedCornerShape(8.dp),
                    colors = com.tribalscholar.app.ui.theme.ScholarBridgeButtons.primary()
                ) {
                    Text(stringResource(R.string.start_application), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
