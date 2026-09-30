package com.tribalscholar.app.data.model

data class Announcement(
    val id: String,
    val title: String,
    val department: String = "Scholarship Updates",
    val date: String,
    val isUrgent: Boolean = false,
    val summary: String,
    val circularPdfUrl: String = ""
)

data class DashboardMetrics(
    val profileCompletionPercent: Int = 80,
    val potentiallyRelevantCount: Int = 6,
    val activeApplicationsCount: Int = 1,
    val pendingDocumentsCount: Int = 2,
    val applicationStatusText: String = "Under Review",
    val estimatedBenefitText: String = "As specified by the selected scheme",
    val eligibleSchemesCount: Int = 6,
    val totalDisbursedAmount: String = "As specified by selected scheme"
)
