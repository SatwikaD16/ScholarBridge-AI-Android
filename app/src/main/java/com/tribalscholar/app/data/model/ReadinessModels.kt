package com.tribalscholar.app.data.model

enum class ReadinessCategory(val displayName: String) {
    PROFILE("Profile"),
    ELIGIBILITY("Eligibility"),
    DOCUMENTS("Documents"),
    APPLICATION("Application Details"),
    CONSISTENCY("Cross-Document Consistency")
}

enum class ReadinessSeverity {
    CRITICAL,
    WARNING,
    INFO
}

data class ReadinessIssue(
    val id: String,
    val category: ReadinessCategory,
    val title: String,
    val description: String,
    val severity: ReadinessSeverity,
    val actionLabel: String,
    val actionTarget: String
)

data class ApplicationReadiness(
    val isReadyToSubmit: Boolean,
    val totalIssuesCount: Int,
    val passedCategories: List<ReadinessCategory>,
    val issues: List<ReadinessIssue>,
    val summaryText: String
)
