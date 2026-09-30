package com.tribalscholar.app.data.model

data class ApplicationItem(
    val applicationNo: String,
    val scholarshipId: String,
    val scholarshipTitle: String,
    val schemeCategory: String,
    val appliedDate: String,
    val academicYear: String,
    val sanctionedAmount: String,
    val currentStatus: ApplicationStage,
    val timeline: List<StageMilestone>,
    val remarks: String
)

enum class ApplicationStage(val title: String, val stepNumber: Int) {
    APPLICATION_SUBMITTED("Application Submitted", 1),
    DOCUMENTS_RECEIVED("Documents Received", 2),
    AI_ASSISTED_VERIFICATION("AI-Assisted Verification", 3),
    OFFICER_REVIEW("Awaiting Officer Review", 4),
    OFFICIAL_DECISION("Official Decision", 5)
}

data class StageMilestone(
    val stage: ApplicationStage,
    val date: String,
    val officerOrOffice: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)
