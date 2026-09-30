package com.tribalscholar.app.data.model

enum class OfficerActionType(val label: String) {
    PENDING("Pending Review"),
    CONFIRMED("AI Finding Confirmed"),
    OVERRIDDEN("AI Finding Overridden"),
    CORRECTION_REQUESTED("Correction Requested"),
    MOVED_TO_SCRUTINY("Moved to Scrutiny")
}

data class OfficerApplicationQueueItem(
    val applicationNo: String,
    val studentId: String,
    val studentName: String,
    val schemeTitle: String,
    val district: String,
    val educationLevel: String,
    val applicationStatus: String,
    val readinessStatus: String,
    val documentStatus: String,
    val eligibilityResult: String,
    val deficienciesCount: Int,
    val reviewFlags: List<String>,
    val aiFindings: List<String>,
    val officerAction: OfficerActionType,
    val officerNotes: String = ""
)

data class DecisionReplayStep(
    val stepIndex: Int,
    val stepName: String,
    val ruleVersion: String,
    val ruleApplied: String,
    val evidenceUsed: String,
    val extractedData: String,
    val eligibilityResult: String,
    val officerReview: String,
    val recordedAction: String,
    val timestamp: String
)

data class ProvenanceNode(
    val id: String,
    val nodeType: String, // "APPLICATION", "DOCUMENT", "EXTRACTED_FIELD", "RULE", "ELIGIBILITY_RESULT", "OFFICER_ACTION", "DECISION"
    val title: String,
    val detail: String,
    val parentId: String? = null
)

data class RuleChangeSimulation(
    val schemeTitle: String,
    val parameterName: String,
    val currentRuleDescription: String,
    val proposedRuleDescription: String,
    val newlyMatchingCount: Int,
    val noLongerMatchingCount: Int,
    val needsReviewCount: Int,
    val noChangeCount: Int,
    val simulationTimestamp: String
)

data class SandboxRule(
    val schemeName: String,
    val educationLevel: String,
    val incomeCeiling: String,
    val requiredCertificate: String,
    val minScorePercentage: String,
    val ruleVersion: String = "v2.2-sandbox"
)

data class AuditEvent(
    val id: String,
    val timestamp: String,
    val actor: String,
    val action: String,
    val details: String
)
