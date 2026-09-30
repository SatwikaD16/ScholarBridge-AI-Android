package com.tribalscholar.app.data.model

enum class DeficiencySeverity(val label: String) {
    HIGH("High Priority"),
    MEDIUM("Medium Priority"),
    LOW("Low Priority")
}

enum class DeficiencyStatus(val label: String) {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved"),
    NEEDS_OFFICER_REVIEW("Needs Officer Review")
}

data class Deficiency(
    val id: String,
    val issue: String,
    val evidence: String,
    val severity: DeficiencySeverity,
    val actionRequired: String,
    val status: DeficiencyStatus,
    val category: String,
    val resolvedNote: String? = null
)
