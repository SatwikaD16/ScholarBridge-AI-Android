package com.tribalscholar.app.data.model

data class Scholarship(
    val id: String,
    val title: String,
    val schemeCode: String,
    val ministryAgency: String,
    val category: ScholarshipCategory,
    val awardAmount: String,
    val awardFrequency: String,
    val deadline: String,
    val daysRemaining: Int,
    val eligibilitySummary: String,
    val detailedEligibility: List<String>,
    val benefits: List<String>,
    val requiredDocuments: List<String>,
    val matchScore: Int = 90,
    val matchLabel: String = "High Profile Match",
    val eligibilityDisclaimer: String = "Based on the information provided. Final eligibility is subject to official verification.",
    val isBookmarked: Boolean = false,
    val hasApplied: Boolean = false,
    val isOpen: Boolean = true
)

enum class ScholarshipCategory(val displayName: String) {
    ALL("All Schemes"),
    HIGHER_EDUCATION("Higher Education"),
    POST_MATRIC("Post-Matric"),
    PRE_MATRIC("Pre-Matric"),
    OVERSEAS("Overseas Study"),
    TOP_CLASS("Top Class Institutes")
}
