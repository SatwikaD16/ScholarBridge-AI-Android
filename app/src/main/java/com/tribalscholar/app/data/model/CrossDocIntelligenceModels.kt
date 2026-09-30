package com.tribalscholar.app.data.model

data class ExtractedDocFields(
    val docId: String,
    val docName: String,
    val studentName: String,
    val dateOfBirth: String,
    val institution: String,
    val course: String,
    val incomeOrCategory: String,
    val certificateNo: String,
    val issueDate: String,
    val validityStatus: String
)

data class FieldComparison(
    val fieldName: String,
    val applicationValue: String,
    val documentValues: Map<String, String>, // e.g. "Income Certificate" -> "Anitha Kumar", "Marksheet" -> "Anitha K. Kumar"
    val isConsistent: Boolean,
    val findingNote: String? = null
)

data class AiConsistencyFinding(
    val id: String,
    val title: String,
    val whatDiffered: String,
    val whereDidItDiffer: String,
    val whyItMightMatter: String,
    val recommendedAction: String,
    val status: String // "Needs Review", "Verified"
)

data class CrossDocReport(
    val overallStatus: String, // "Needs Review" or "No major mismatch detected"
    val comparisons: List<FieldComparison>,
    val findings: List<AiConsistencyFinding>,
    val extractedDocs: List<ExtractedDocFields>
)
