package com.tribalscholar.app.data.model

enum class DigitalTwinStageStatus(val label: String) {
    COMPLETED("Completed"),
    ATTENTION_REQUIRED("Attention Required"),
    IN_PROGRESS("In Progress"),
    PENDING("Pending")
}

data class DigitalTwinStage(
    val id: String,
    val name: String,
    val status: DigitalTwinStageStatus,
    val detail: String,
    val isCurrent: Boolean = false
)

data class NextBestActionItem(
    val title: String,
    val reason: String,
    val actionText: String,
    val actionType: String // "UPLOAD_INCOME", "FIX_NAME_CONSISTENCY", "CHECK_ELIGIBILITY", "REVIEW_SUBMIT", "TRACK_APPLICATION"
)

data class DigitalTwinState(
    val currentStatus: String,
    val currentBlocker: String?,
    val nextBestAction: NextBestActionItem,
    val stages: List<DigitalTwinStage>
)
