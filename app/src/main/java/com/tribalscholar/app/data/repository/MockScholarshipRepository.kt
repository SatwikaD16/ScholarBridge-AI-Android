package com.tribalscholar.app.data.repository

import com.tribalscholar.app.data.model.AiConsistencyFinding
import com.tribalscholar.app.data.model.Announcement
import com.tribalscholar.app.data.model.ApplicationItem
import com.tribalscholar.app.data.model.ApplicationReadiness
import com.tribalscholar.app.data.model.ApplicationStage
import com.tribalscholar.app.data.model.AuditEvent
import com.tribalscholar.app.data.model.CrossDocReport
import com.tribalscholar.app.data.model.DashboardMetrics
import com.tribalscholar.app.data.model.DecisionReplayStep
import com.tribalscholar.app.data.model.Deficiency
import com.tribalscholar.app.data.model.DeficiencySeverity
import com.tribalscholar.app.data.model.DeficiencyStatus
import com.tribalscholar.app.data.model.DigitalTwinStage
import com.tribalscholar.app.data.model.DigitalTwinStageStatus
import com.tribalscholar.app.data.model.DigitalTwinState
import com.tribalscholar.app.data.model.DocumentStatus
import com.tribalscholar.app.data.model.EligibilityRuleNode
import com.tribalscholar.app.data.model.ExplainableEligibilityGraph
import com.tribalscholar.app.data.model.ExtractedDocFields
import com.tribalscholar.app.data.model.FieldComparison
import com.tribalscholar.app.data.model.NextBestActionItem
import com.tribalscholar.app.data.model.OfficerActionType
import com.tribalscholar.app.data.model.OfficerApplicationQueueItem
import com.tribalscholar.app.data.model.ProvenanceNode
import com.tribalscholar.app.data.model.ReadinessCategory
import com.tribalscholar.app.data.model.ReadinessIssue
import com.tribalscholar.app.data.model.ReadinessSeverity
import com.tribalscholar.app.data.model.RuleChangeSimulation
import com.tribalscholar.app.data.model.RuleResult
import com.tribalscholar.app.data.model.SandboxRule
import com.tribalscholar.app.data.model.Scholarship
import com.tribalscholar.app.data.model.ScholarshipCategory
import com.tribalscholar.app.data.model.StageMilestone
import com.tribalscholar.app.data.model.StudentProfile
import com.tribalscholar.app.data.model.UploadedDocument
import com.tribalscholar.app.data.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MockScholarshipRepository private constructor() : ScholarshipRepository {

    companion object {
        @Volatile
        private var INSTANCE: MockScholarshipRepository? = null

        fun getInstance(): MockScholarshipRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MockScholarshipRepository().also { INSTANCE = it }
            }
        }

        fun resetInstance(): MockScholarshipRepository {
            return synchronized(this) {
                MockScholarshipRepository().also { INSTANCE = it }
            }
        }
    }

    private val userRoleFlow = MutableStateFlow("STUDENT") // "STUDENT" or "OFFICER"

    private val sessionFlow = MutableStateFlow(
        UserSession(
            isLoggedIn = false,
            studentId = "",
            studentName = "",
            mobileNumber = "",
            lastLoginTime = ""
        )
    )

    private val profileFlow = MutableStateFlow(
        StudentProfile(
            studentId = "ST2026-1042",
            fullName = "Anitha Kumar",
            dateOfBirth = "14-Aug-2004",
            gender = "Female",
            stateOfDomicile = "Tamil Nadu",
            district = "Demo District",
            email = "anitha.kumar@example.com",
            mobileNumber = "+91 98765 XXXXX",
            eligibilityCategory = "Scheduled Tribe (ST)",
            eligibilityStatus = "Information provided",
            eligibilityVerificationNote = "Eligibility information provided by student",
            familyIncome = "₹1,80,000 / annum",
            academicLevel = "Undergraduate",
            courseName = "B.Tech in Artificial Intelligence & Data Science",
            institutionName = "Demo Institute of Technology",
            academicYear = "2nd Year",
            scorePercentage = "8.8 CGPA (85%)",
            paymentTitle = "Scholarship Payment Details",
            paymentInfoNote = "Available after official verification",
            paymentStatus = "Not yet verified",
            documents = listOf(
                UploadedDocument(
                    id = "DOC-01",
                    name = "Community Certificate",
                    documentType = "Community Proof",
                    issueDate = "15-Jan-2024",
                    verificationStatus = DocumentStatus.AVAILABLE_FOR_VERIFICATION,
                    source = "Digital Document"
                ),
                UploadedDocument(
                    id = "DOC-02",
                    name = "Income Certificate",
                    documentType = "Income Proof",
                    issueDate = "12-May-2026",
                    verificationStatus = DocumentStatus.UPLOADED,
                    source = "Digital Document"
                ),
                UploadedDocument(
                    id = "DOC-03",
                    name = "Bonafide Certificate",
                    documentType = "Academic Proof",
                    issueDate = "Pending",
                    verificationStatus = DocumentStatus.REQUIRED,
                    source = "Digital Document"
                ),
                UploadedDocument(
                    id = "DOC-04",
                    name = "Academic Marksheet",
                    documentType = "Academic Record",
                    issueDate = "20-Jun-2026",
                    verificationStatus = DocumentStatus.UPLOADED,
                    source = "Digital Document"
                )
            )
        )
    )

    private val scholarshipsFlow = MutableStateFlow(
        listOf(
            Scholarship(
                id = "SCH-001",
                title = "National Fellowship Scheme",
                schemeCode = "GR-NFS-2026",
                ministryAgency = "Higher Education Support Services",
                category = ScholarshipCategory.HIGHER_EDUCATION,
                awardAmount = "₹35,000 / month",
                awardFrequency = "Plus Annual Contingency Support",
                deadline = "30 Nov 2026",
                daysRemaining = 64,
                eligibilitySummary = "Eligible full-time scholars pursuing higher education programs.",
                detailedEligibility = listOf(
                    "Candidate belongs to eligible category.",
                    "Enrolled in recognized undergraduate or postgraduate program.",
                    "Annual family income within prescribed limits.",
                    "Selection based on academic merit and guidelines."
                ),
                benefits = listOf(
                    "Monthly fellowship grant",
                    "Annual contingency allowance",
                    "Special assistance allowance where applicable"
                ),
                requiredDocuments = listOf(
                    "Community Certificate",
                    "Institution Bonafide Certificate",
                    "Income Certificate",
                    "Academic Certificate"
                ),
                matchScore = 96,
                matchLabel = "Potentially Relevant",
                isBookmarked = true,
                hasApplied = false
            ),
            Scholarship(
                id = "SCH-002",
                title = "Post-Matric Scholarship",
                schemeCode = "GR-PMS-2026",
                ministryAgency = "Scholarships & Fellowship Services",
                category = ScholarshipCategory.POST_MATRIC,
                awardAmount = "Tuition & Maintenance Support",
                awardFrequency = "Per Academic Session",
                deadline = "25 Oct 2026",
                daysRemaining = 28,
                eligibilitySummary = "Students pursuing approved professional courses in recognized institutions.",
                detailedEligibility = listOf(
                    "Must be an enrolled student in an accredited institution.",
                    "Total family income from all sources within eligible range.",
                    "Continuous academic progress in current course."
                ),
                benefits = listOf(
                    "Tuition fee assistance as per scheme guidelines",
                    "Living expenses support allowance",
                    "Books and learning materials assistance"
                ),
                requiredDocuments = listOf(
                    "Community Certificate",
                    "Bonafide Certificate",
                    "Academic Certificate",
                    "Income Certificate"
                ),
                matchScore = 95,
                matchLabel = "Potentially Relevant",
                isBookmarked = false,
                hasApplied = true
            ),
            Scholarship(
                id = "SCH-003",
                title = "Higher Education Support",
                schemeCode = "GR-HES-2026",
                ministryAgency = "Scholarships & Fellowship Services",
                category = ScholarshipCategory.TOP_CLASS,
                awardAmount = "Full Tuition Support",
                awardFrequency = "Plus Living & Learning Grant",
                deadline = "15 Oct 2026",
                daysRemaining = 18,
                eligibilitySummary = "Enrolled undergraduate students in technical & AI disciplines.",
                detailedEligibility = listOf(
                    "Enrolled in a recognized college or university course.",
                    "Family income within scheme ceiling.",
                    "Satisfactory attendance and academic standing."
                ),
                benefits = listOf(
                    "Course maintenance allowance",
                    "Reimbursement of eligible academic fees",
                    "Study materials support"
                ),
                requiredDocuments = listOf(
                    "Community Certificate",
                    "Previous Academic Marksheet",
                    "Income Certificate",
                    "College Bonafide Letter"
                ),
                matchScore = 92,
                matchLabel = "Potentially Relevant",
                isBookmarked = false,
                hasApplied = false
            ),
            Scholarship(
                id = "SCH-004",
                title = "Higher Education Excellence Grant",
                schemeCode = "GR-HEEG-2026",
                ministryAgency = "Scholarships & Fellowship Services",
                category = ScholarshipCategory.HIGHER_EDUCATION,
                awardAmount = "Course & Research Grant",
                awardFrequency = "Per Academic Year",
                deadline = "15 Jan 2027",
                daysRemaining = 110,
                eligibilitySummary = "Merit-based financial assistance for advanced degree students.",
                detailedEligibility = listOf(
                    "Enrolled in degree program with minimum qualifying marks.",
                    "Demonstrated academic performance.",
                    "Family income meets specified guidelines."
                ),
                benefits = listOf(
                    "Tuition assistance grant",
                    "Academic travel allowance",
                    "Study resources grant"
                ),
                requiredDocuments = listOf(
                    "Community Certificate",
                    "Academic Certificate",
                    "Income Certificate",
                    "Identity Document"
                ),
                matchScore = 88,
                matchLabel = "Potentially Relevant",
                isBookmarked = false,
                hasApplied = false
            ),
            Scholarship(
                id = "SCH-005",
                title = "Pre-Matric Educational Assistance",
                schemeCode = "GR-PRE-2026",
                ministryAgency = "Scholarships & Fellowship Services",
                category = ScholarshipCategory.PRE_MATRIC,
                awardAmount = "Annual Study Stipend",
                awardFrequency = "Per Academic Year",
                deadline = "30 Oct 2026",
                daysRemaining = 33,
                eligibilitySummary = "Students enrolled in secondary education stages.",
                detailedEligibility = listOf(
                    "Enrolled in recognized school.",
                    "Family income within prescribed limits.",
                    "Regular school attendance record."
                ),
                benefits = listOf(
                    "Annual education stipend",
                    "Books and stationery grant"
                ),
                requiredDocuments = listOf(
                    "School Bonafide Certificate",
                    "Community Certificate",
                    "Income Certificate"
                ),
                matchScore = 74,
                matchLabel = "Potentially Relevant",
                isBookmarked = false,
                hasApplied = false
            ),
            Scholarship(
                id = "SCH-006",
                title = "STEM & Innovation Student Fellowship",
                schemeCode = "GR-STEM-2026",
                ministryAgency = "Scholarships & Fellowship Services",
                category = ScholarshipCategory.HIGHER_EDUCATION,
                awardAmount = "Project Innovation Grant",
                awardFrequency = "Direct Educational Grant",
                deadline = "10 Nov 2026",
                daysRemaining = 44,
                eligibilitySummary = "Undergraduate and postgraduate students developing innovative technical solutions.",
                detailedEligibility = listOf(
                    "Enrolled in Engineering, Technology, or Science program.",
                    "Proposed project addressing community or regional challenges.",
                    "Department or faculty mentor recommendation."
                ),
                benefits = listOf(
                    "Project seed grant",
                    "Academic mentorship access",
                    "Student innovation recognition"
                ),
                requiredDocuments = listOf(
                    "Project synopsis",
                    "Academic Certificate",
                    "Community Certificate"
                ),
                matchScore = 91,
                matchLabel = "Potentially Relevant",
                isBookmarked = true,
                hasApplied = false
            )
        )
    )

    private val applicationsFlow = MutableStateFlow(
        listOf(
            ApplicationItem(
                applicationNo = "ST2026-1042",
                scholarshipId = "SCH-002",
                scholarshipTitle = "Post-Matric Scholarship",
                schemeCategory = "Post-Matric",
                appliedDate = "04-Aug-2026",
                academicYear = "2026-2027",
                sanctionedAmount = "As specified by scheme",
                currentStatus = ApplicationStage.OFFICER_REVIEW,
                timeline = listOf(
                    StageMilestone(
                        stage = ApplicationStage.APPLICATION_SUBMITTED,
                        date = "04-Aug-2026",
                        officerOrOffice = "Student Portal Submission",
                        isCompleted = true,
                        isCurrent = false
                    ),
                    StageMilestone(
                        stage = ApplicationStage.DOCUMENTS_RECEIVED,
                        date = "12-Aug-2026",
                        officerOrOffice = "Document Verification Desk",
                        isCompleted = true,
                        isCurrent = false
                    ),
                    StageMilestone(
                        stage = ApplicationStage.AI_ASSISTED_VERIFICATION,
                        date = "25-Aug-2026",
                        officerOrOffice = "Automated Verification Check",
                        isCompleted = true,
                        isCurrent = false
                    ),
                    StageMilestone(
                        stage = ApplicationStage.OFFICER_REVIEW,
                        date = "In Progress",
                        officerOrOffice = "Awaiting Officer Review",
                        isCompleted = false,
                        isCurrent = true
                    ),
                    StageMilestone(
                        stage = ApplicationStage.OFFICIAL_DECISION,
                        date = "Pending",
                        officerOrOffice = "Official Decision Desk",
                        isCompleted = false,
                        isCurrent = false
                    )
                ),
                remarks = "Application under review. Final eligibility is subject to official verification."
            )
        )
    )

    private val announcementsFlow = MutableStateFlow(
        listOf(
            Announcement(
                id = "ANN-01",
                title = "Application Window Open",
                department = "Scholarship Updates",
                date = "22 Sep 2026",
                isUrgent = false,
                summary = "Your selected scholarship application window is open."
            ),
            Announcement(
                id = "ANN-02",
                title = "Document Verification Status",
                department = "Scholarship Updates",
                date = "15 Sep 2026",
                isUrgent = true,
                summary = "Additional document required for your application."
            ),
            Announcement(
                id = "ANN-03",
                title = "Application Progress Update",
                department = "Scholarship Updates",
                date = "05 Sep 2026",
                isUrgent = false,
                summary = "Your application has moved to officer review."
            )
        )
    )

    // ==========================================
    // INNOVATION LAYER SHARED REACTIVE STATE
    // ==========================================

    private val deficienciesFlow = MutableStateFlow(
        listOf(
            Deficiency(
                id = "DEF-01",
                issue = "Bonafide Certificate Required",
                evidence = "Current academic enrollment proof is missing from file.",
                severity = DeficiencySeverity.HIGH,
                actionRequired = "Upload Bonafide Certificate issued by Demo Institute of Technology.",
                status = DeficiencyStatus.OPEN,
                category = "DOCUMENTS"
            ),
            Deficiency(
                id = "DEF-02",
                issue = "Possible Name Variation Needs Review",
                evidence = "Application name: 'Anitha Kumar' vs Marksheet name: 'Anitha K. Kumar'.",
                severity = DeficiencySeverity.MEDIUM,
                actionRequired = "Confirm institutional name record or request officer acknowledgement.",
                status = DeficiencyStatus.NEEDS_OFFICER_REVIEW,
                category = "CONSISTENCY"
            )
        )
    )

    private val crossDocReportFlow = MutableStateFlow(
        CrossDocReport(
            overallStatus = "Needs Review",
            comparisons = listOf(
                FieldComparison(
                    fieldName = "Student Name",
                    applicationValue = "Anitha Kumar",
                    documentValues = mapOf(
                        "Income Certificate" to "Anitha Kumar",
                        "Community Certificate" to "Anitha Kumar",
                        "Academic Marksheet" to "Anitha K. Kumar"
                    ),
                    isConsistent = false,
                    findingNote = "Middle initial 'K.' present on Marksheet"
                ),
                FieldComparison(
                    fieldName = "Date of Birth",
                    applicationValue = "14-Aug-2004",
                    documentValues = mapOf(
                        "Community Certificate" to "14-Aug-2004",
                        "Academic Marksheet" to "14-Aug-2004"
                    ),
                    isConsistent = true
                ),
                FieldComparison(
                    fieldName = "Institution",
                    applicationValue = "Demo Institute of Technology",
                    documentValues = mapOf(
                        "Academic Marksheet" to "Demo Institute of Technology"
                    ),
                    isConsistent = true
                ),
                FieldComparison(
                    fieldName = "Course",
                    applicationValue = "B.Tech (AI & Data Science)",
                    documentValues = mapOf(
                        "Academic Marksheet" to "B.Tech in Artificial Intelligence & Data Science"
                    ),
                    isConsistent = true
                ),
                FieldComparison(
                    fieldName = "Annual Income",
                    applicationValue = "₹1,80,000",
                    documentValues = mapOf(
                        "Income Certificate" to "₹1,80,000"
                    ),
                    isConsistent = true
                )
            ),
            findings = listOf(
                AiConsistencyFinding(
                    id = "FIND-01",
                    title = "Possible Name Variation Detected",
                    whatDiffered = "Marksheet displays 'Anitha K. Kumar' while application states 'Anitha Kumar'.",
                    whereDidItDiffer = "Academic Marksheet vs Application Profile",
                    whyItMightMatter = "Verification guidelines require name consistency to prevent mismatched disbursements.",
                    recommendedAction = "Verify whether institutional records include father's initial 'K.'. Officer can confirm or student can attach bonafide.",
                    status = "Needs Review"
                )
            ),
            extractedDocs = listOf(
                ExtractedDocFields(
                    docId = "DOC-01",
                    docName = "Community Certificate",
                    studentName = "Anitha Kumar",
                    dateOfBirth = "14-Aug-2004",
                    institution = "Government Revenue Authority",
                    course = "N/A",
                    incomeOrCategory = "Scheduled Tribe (ST)",
                    certificateNo = "REV/2024/ST/8821",
                    issueDate = "15-Jan-2024",
                    validityStatus = "Permanent Validity"
                ),
                ExtractedDocFields(
                    docId = "DOC-02",
                    docName = "Income Certificate",
                    studentName = "Anitha Kumar",
                    dateOfBirth = "14-Aug-2004",
                    institution = "Tahsildar Office, Demo District",
                    course = "N/A",
                    incomeOrCategory = "₹1,80,000 per annum",
                    certificateNo = "INC/2026/4109",
                    issueDate = "12-May-2026",
                    validityStatus = "Valid for FY 2026-27"
                ),
                ExtractedDocFields(
                    docId = "DOC-04",
                    docName = "Academic Marksheet",
                    studentName = "Anitha K. Kumar",
                    dateOfBirth = "14-Aug-2004",
                    institution = "Demo Institute of Technology",
                    course = "B.Tech in Artificial Intelligence & Data Science",
                    incomeOrCategory = "CGPA: 8.8 / 10.0 (85%)",
                    certificateNo = "UNIV/2026/ENG/402",
                    issueDate = "20-Jun-2026",
                    validityStatus = "Final Transcript"
                )
            )
        )
    )

    private val readinessFlow = MutableStateFlow(
        ApplicationReadiness(
            isReadyToSubmit = false,
            totalIssuesCount = 2,
            passedCategories = listOf(
                ReadinessCategory.PROFILE,
                ReadinessCategory.ELIGIBILITY,
                ReadinessCategory.APPLICATION
            ),
            issues = listOf(
                ReadinessIssue(
                    id = "ISSUE-01",
                    category = ReadinessCategory.DOCUMENTS,
                    title = "Bonafide Certificate Missing",
                    description = "A college bonafide certificate is required for enrollment verification.",
                    severity = ReadinessSeverity.CRITICAL,
                    actionLabel = "Upload Bonafide",
                    actionTarget = "DOC_UPLOAD_BONAFIDE"
                ),
                ReadinessIssue(
                    id = "ISSUE-02",
                    category = ReadinessCategory.CONSISTENCY,
                    title = "Name Consistency Needs Review",
                    description = "Marksheet has 'Anitha K. Kumar' vs Application 'Anitha Kumar'.",
                    severity = ReadinessSeverity.WARNING,
                    actionLabel = "Review Consistency",
                    actionTarget = "CROSS_DOC_REVIEW"
                )
            ),
            summaryText = "2 issues need attention before final submission."
        )
    )

    private val digitalTwinFlow = MutableStateFlow(
        DigitalTwinState(
            currentStatus = "Document Verification",
            currentBlocker = "Bonafide Certificate Missing",
            nextBestAction = NextBestActionItem(
                title = "Upload Bonafide Certificate",
                reason = "Your application requires a college bonafide certificate to proceed.",
                actionText = "Upload Now",
                actionType = "UPLOAD_BONAFIDE"
            ),
            stages = listOf(
                DigitalTwinStage("STAGE-1", "Profile Details", DigitalTwinStageStatus.COMPLETED, "Profile complete and verified", false),
                DigitalTwinStage("STAGE-2", "Configured Eligibility", DigitalTwinStageStatus.COMPLETED, "Meets income and education criteria", false),
                DigitalTwinStage("STAGE-3", "Document Verification", DigitalTwinStageStatus.ATTENTION_REQUIRED, "1 document missing, 1 flagged for review", true),
                DigitalTwinStage("STAGE-4", "Application Readiness", DigitalTwinStageStatus.ATTENTION_REQUIRED, "2 issues need resolution before submission", false),
                DigitalTwinStage("STAGE-5", "Application Submission", DigitalTwinStageStatus.PENDING, "Ready to submit once issues resolved", false),
                DigitalTwinStage("STAGE-6", "Officer Review", DigitalTwinStageStatus.PENDING, "Awaiting submission", false),
                DigitalTwinStage("STAGE-7", "Official Decision", DigitalTwinStageStatus.PENDING, "Final decision by authorized officials", false)
            )
        )
    )

    private val auditEventsFlow = MutableStateFlow(
        listOf(
            AuditEvent(
                id = "AUD-01",
                timestamp = "04-Aug-2026 10:14 AM",
                actor = "Student: Anitha Kumar",
                action = "Account Session Authenticated",
                details = "Mobile number verified via phone authentication."
            ),
            AuditEvent(
                id = "AUD-02",
                timestamp = "04-Aug-2026 10:20 AM",
                actor = "Student: Anitha Kumar",
                action = "Profile Created",
                details = "Completed synthetic registration for B.Tech program."
            ),
            AuditEvent(
                id = "AUD-03",
                timestamp = "12-Aug-2026 11:05 AM",
                actor = "AI Document Engine",
                action = "Document Extracted & Analyzed",
                details = "Extracted income (₹1,80,000) and marks (8.8 CGPA)."
            ),
            AuditEvent(
                id = "AUD-04",
                timestamp = "12-Aug-2026 11:06 AM",
                actor = "AI Consistency Analyzer",
                action = "Cross-Document Comparison",
                details = "Identified middle initial 'K.' variation on marksheet."
            ),
            AuditEvent(
                id = "AUD-05",
                timestamp = "25-Aug-2026 02:30 PM",
                actor = "Verification Officer: M. Raman",
                action = "Review Queue Opened",
                details = "Preliminary examination of application ST2026-1042."
            )
        )
    )

    private val officerQueueFlow = MutableStateFlow(
        listOf(
            OfficerApplicationQueueItem(
                applicationNo = "ST2026-1042",
                studentId = "ST2026-1042",
                studentName = "Anitha Kumar",
                schemeTitle = "Post-Matric Scholarship",
                district = "Demo District",
                educationLevel = "Undergraduate (B.Tech)",
                applicationStatus = "Awaiting Officer Review",
                readinessStatus = "Needs Attention",
                documentStatus = "3 Uploaded, 1 Required",
                eligibilityResult = "PASS (v2.1)",
                deficienciesCount = 1,
                reviewFlags = listOf("Name variation on Marksheet", "Income verified"),
                aiFindings = listOf("Possible name variation 'Anitha K. Kumar' on Marksheet", "Income ₹1,80,000 ≤ ₹2,50,000 ceiling"),
                officerAction = OfficerActionType.PENDING,
                officerNotes = "Middle initial variation under scrutiny."
            ),
            OfficerApplicationQueueItem(
                applicationNo = "ST2026-0819",
                studentId = "ST2026-0819",
                studentName = "Praveen Murugan",
                schemeTitle = "National Fellowship Scheme",
                district = "Nilgiris District",
                educationLevel = "Postgraduate (M.Sc)",
                applicationStatus = "Ready for Scrutiny",
                readinessStatus = "Ready",
                documentStatus = "All 4 Verified",
                eligibilityResult = "PASS (v2.1)",
                deficienciesCount = 0,
                reviewFlags = listOf("All documents verified", "High merit ranking"),
                aiFindings = listOf("Zero cross-document discrepancies", "Income ₹1,40,000 within threshold"),
                officerAction = OfficerActionType.CONFIRMED,
                officerNotes = "All verification criteria satisfied."
            ),
            OfficerApplicationQueueItem(
                applicationNo = "ST2026-1102",
                studentId = "ST2026-1102",
                studentName = "Divya Rathore",
                schemeTitle = "Higher Education Support",
                district = "Dindigul District",
                educationLevel = "Undergraduate (B.Sc)",
                applicationStatus = "Deficient",
                readinessStatus = "Action Needed",
                documentStatus = "Income Certificate Expired",
                eligibilityResult = "NEEDS REVIEW (v2.1)",
                deficienciesCount = 2,
                reviewFlags = listOf("Expired income certificate", "Incomplete enrollment proof"),
                aiFindings = listOf("Income certificate dated 2024 requires current FY renewal"),
                officerAction = OfficerActionType.CORRECTION_REQUESTED,
                officerNotes = "Requested updated income certificate for current FY."
            ),
            OfficerApplicationQueueItem(
                applicationNo = "ST2026-0455",
                studentId = "ST2026-0455",
                studentName = "Karthik Soren",
                schemeTitle = "STEM & Innovation Fellowship",
                district = "Salem District",
                educationLevel = "Undergraduate (B.E)",
                applicationStatus = "Pending Verification",
                readinessStatus = "Needs Attention",
                documentStatus = "Project Synopsis Attached",
                eligibilityResult = "PASS (v2.1)",
                deficienciesCount = 1,
                reviewFlags = listOf("Faculty endorsement pending"),
                aiFindings = listOf("Innovation project abstract verified; waiting for institutional HOD signature"),
                officerAction = OfficerActionType.PENDING,
                officerNotes = "Awaiting HOD endorsement upload."
            )
        )
    )

    // ==========================================
    // RECALCULATION & SYNC ENGINE
    // ==========================================

    private fun recalculateUnifiedState() {
        val currentDeficiencies = deficienciesFlow.value
        val openCount = currentDeficiencies.count { it.status == DeficiencyStatus.OPEN }
        val needsReviewCount = currentDeficiencies.count { it.status == DeficiencyStatus.NEEDS_OFFICER_REVIEW }
        val totalIssues = openCount + needsReviewCount

        // 1. Update Readiness
        val issuesList = mutableListOf<ReadinessIssue>()
        val passedCats = mutableListOf(ReadinessCategory.PROFILE, ReadinessCategory.ELIGIBILITY, ReadinessCategory.APPLICATION)

        if (currentDeficiencies.any { it.category == "DOCUMENTS" && it.status == DeficiencyStatus.OPEN }) {
            issuesList.add(
                ReadinessIssue(
                    id = "ISSUE-01",
                    category = ReadinessCategory.DOCUMENTS,
                    title = "Bonafide Certificate Missing",
                    description = "A college bonafide certificate is required for enrollment verification.",
                    severity = ReadinessSeverity.CRITICAL,
                    actionLabel = "Upload Bonafide",
                    actionTarget = "DOC_UPLOAD_BONAFIDE"
                )
            )
        } else {
            passedCats.add(ReadinessCategory.DOCUMENTS)
        }

        if (currentDeficiencies.any { it.category == "CONSISTENCY" && it.status != DeficiencyStatus.RESOLVED }) {
            issuesList.add(
                ReadinessIssue(
                    id = "ISSUE-02",
                    category = ReadinessCategory.CONSISTENCY,
                    title = "Name Consistency Needs Review",
                    description = "Marksheet has 'Anitha K. Kumar' vs Application 'Anitha Kumar'.",
                    severity = ReadinessSeverity.WARNING,
                    actionLabel = "Review Consistency",
                    actionTarget = "CROSS_DOC_REVIEW"
                )
            )
        } else {
            passedCats.add(ReadinessCategory.CONSISTENCY)
        }

        val isReady = issuesList.none { it.severity == ReadinessSeverity.CRITICAL }

        readinessFlow.value = ApplicationReadiness(
            isReadyToSubmit = isReady,
            totalIssuesCount = issuesList.size,
            passedCategories = passedCats,
            issues = issuesList,
            summaryText = if (isReady) "All required conditions satisfied. Ready for submission." else "${issuesList.size} issues need attention before final submission."
        )

        // 2. Update Digital Twin
        val nextAction = when {
            issuesList.any { it.category == ReadinessCategory.DOCUMENTS } -> NextBestActionItem(
                title = "Upload Bonafide Certificate",
                reason = "Your application requires a college bonafide certificate to proceed.",
                actionText = "Upload Now",
                actionType = "UPLOAD_BONAFIDE"
            )
            issuesList.any { it.category == ReadinessCategory.CONSISTENCY } -> NextBestActionItem(
                title = "Confirm Name Variation",
                reason = "Acknowledge the middle initial on your marksheet.",
                actionText = "Confirm Record",
                actionType = "CONFIRM_NAME"
            )
            else -> NextBestActionItem(
                title = "Review and Submit",
                reason = "All checks passed. You can submit your application for official review.",
                actionText = "Submit Now",
                actionType = "SUBMIT_APPLICATION"
            )
        }

        val blocker = when {
            issuesList.any { it.category == ReadinessCategory.DOCUMENTS } -> "Bonafide Certificate Missing"
            issuesList.any { it.category == ReadinessCategory.CONSISTENCY } -> "Name Consistency Under Review"
            else -> null
        }

        val currentStatus = when {
            blocker != null -> "Document Verification"
            else -> "Ready to Submit"
        }

        val docStageStatus = if (issuesList.any { it.category == ReadinessCategory.DOCUMENTS || it.category == ReadinessCategory.CONSISTENCY }) {
            DigitalTwinStageStatus.ATTENTION_REQUIRED
        } else {
            DigitalTwinStageStatus.COMPLETED
        }

        val readinessStageStatus = if (isReady) DigitalTwinStageStatus.COMPLETED else DigitalTwinStageStatus.ATTENTION_REQUIRED

        digitalTwinFlow.value = DigitalTwinState(
            currentStatus = currentStatus,
            currentBlocker = blocker,
            nextBestAction = nextAction,
            stages = listOf(
                DigitalTwinStage("STAGE-1", "Profile Details", DigitalTwinStageStatus.COMPLETED, "Profile complete and verified", false),
                DigitalTwinStage("STAGE-2", "Configured Eligibility", DigitalTwinStageStatus.COMPLETED, "Meets income and education criteria", false),
                DigitalTwinStage("STAGE-3", "Document Verification", docStageStatus, if (docStageStatus == DigitalTwinStageStatus.COMPLETED) "All documents verified" else "Pending documents/consistency review", docStageStatus == DigitalTwinStageStatus.ATTENTION_REQUIRED),
                DigitalTwinStage("STAGE-4", "Application Readiness", readinessStageStatus, if (isReady) "Ready to submit" else "${issuesList.size} issues pending", isReady),
                DigitalTwinStage("STAGE-5", "Application Submission", if (isReady) DigitalTwinStageStatus.IN_PROGRESS else DigitalTwinStageStatus.PENDING, "Awaiting final confirmation", false),
                DigitalTwinStage("STAGE-6", "Officer Review", DigitalTwinStageStatus.PENDING, "Awaiting submission", false),
                DigitalTwinStage("STAGE-7", "Official Decision", DigitalTwinStageStatus.PENDING, "Final decision by authorized officials", false)
            )
        )
    }

    private fun addAuditEvent(actor: String, action: String, details: String) {
        val sdf = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH)
        val timestamp = sdf.format(Date())
        val newEvent = AuditEvent(
            id = "AUD-${System.currentTimeMillis() % 10000}",
            timestamp = timestamp,
            actor = actor,
            action = action,
            details = details
        )
        auditEventsFlow.value = listOf(newEvent) + auditEventsFlow.value
    }

    // ==========================================
    // REPOSITORY IMPLEMENTATION METHODS
    // ==========================================

    override fun getUserRole(): Flow<String> = userRoleFlow.asStateFlow()

    override suspend fun setUserRole(role: String) {
        userRoleFlow.value = role
        addAuditEvent(
            actor = if (role == "OFFICER") "Authorized Officer Portal" else "Student Portal",
            action = "Portal Role Switched",
            details = "Active user context changed to $role."
        )
    }

    override fun getSession(): Flow<UserSession> = sessionFlow.asStateFlow()

    override fun getStudentProfile(): Flow<StudentProfile> = profileFlow.asStateFlow()

    override fun getDashboardMetrics(): Flow<DashboardMetrics> {
        return applicationsFlow.map { apps ->
            val statusText = if (apps.any { it.currentStatus == ApplicationStage.APPLICATION_SUBMITTED }) {
                "Application Submitted"
            } else {
                "Under Review"
            }
            DashboardMetrics(
                profileCompletionPercent = 80,
                potentiallyRelevantCount = 6,
                activeApplicationsCount = apps.size,
                pendingDocumentsCount = 2,
                applicationStatusText = statusText,
                estimatedBenefitText = "As specified by the selected scheme",
                eligibleSchemesCount = 6,
                totalDisbursedAmount = "As specified by selected scheme"
            )
        }
    }

    override fun getActiveApplications(): Flow<List<ApplicationItem>> = applicationsFlow.asStateFlow()

    override fun getScholarships(
        query: String,
        category: ScholarshipCategory
    ): Flow<List<Scholarship>> {
        return scholarshipsFlow.map { list ->
            list.filter { item ->
                (category == ScholarshipCategory.ALL || item.category == category) &&
                    (query.isBlank() || item.title.contains(query, ignoreCase = true) || item.ministryAgency.contains(query, ignoreCase = true))
            }
        }
    }

    override fun getScholarshipById(id: String): Flow<Scholarship?> {
        return scholarshipsFlow.map { list -> list.find { it.id == id } }
    }

    override fun getAnnouncements(): Flow<List<Announcement>> = announcementsFlow.asStateFlow()

    override suspend fun toggleBookmark(scholarshipId: String) {
        scholarshipsFlow.value = scholarshipsFlow.value.map {
            if (it.id == scholarshipId) it.copy(isBookmarked = !it.isBookmarked) else it
        }
    }

    override suspend fun applyForScholarship(scholarshipId: String): Result<String> {
        val appNo = "ST2026-${(1000..9999).random()}"
        val targetScholarship = scholarshipsFlow.value.find { it.id == scholarshipId }
            ?: scholarshipsFlow.value.first()

        val newApp = ApplicationItem(
            applicationNo = appNo,
            scholarshipId = targetScholarship.id,
            scholarshipTitle = targetScholarship.title,
            schemeCategory = targetScholarship.category.displayName,
            appliedDate = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(Date()),
            academicYear = "2026-2027",
            sanctionedAmount = "As specified by scheme",
            currentStatus = ApplicationStage.APPLICATION_SUBMITTED,
            timeline = listOf(
                StageMilestone(ApplicationStage.APPLICATION_SUBMITTED, "Today", "Student Portal Submission", true, true),
                StageMilestone(ApplicationStage.DOCUMENTS_RECEIVED, "Pending", "Document Verification Desk", false, false),
                StageMilestone(ApplicationStage.AI_ASSISTED_VERIFICATION, "Pending", "Automated Verification Check", false, false),
                StageMilestone(ApplicationStage.OFFICER_REVIEW, "Pending", "Awaiting Officer Review", false, false),
                StageMilestone(ApplicationStage.OFFICIAL_DECISION, "Pending", "Official Decision Desk", false, false)
            ),
            remarks = "Application submitted. Queued for document verification."
        )

        applicationsFlow.value = listOf(newApp) + applicationsFlow.value
        scholarshipsFlow.value = scholarshipsFlow.value.map {
            if (it.id == scholarshipId) it.copy(hasApplied = true) else it
        }

        // Also add to Officer Queue
        val queueItem = OfficerApplicationQueueItem(
            applicationNo = appNo,
            studentId = profileFlow.value.studentId,
            studentName = profileFlow.value.fullName,
            schemeTitle = targetScholarship.title,
            district = profileFlow.value.district,
            educationLevel = profileFlow.value.academicLevel,
            applicationStatus = "Application Submitted",
            readinessStatus = "Ready for Review",
            documentStatus = "All Required Attached",
            eligibilityResult = "PASS (v2.1)",
            deficienciesCount = 0,
            reviewFlags = listOf("New Submission"),
            aiFindings = listOf("Preliminary criteria satisfied"),
            officerAction = OfficerActionType.PENDING,
            officerNotes = "Newly submitted application."
        )
        officerQueueFlow.value = listOf(queueItem) + officerQueueFlow.value

        // Update announcementsFlow with immediate confirmation update
        val newAnnouncement = Announcement(
            id = "ANN-${System.currentTimeMillis() % 10000}",
            title = "Application Submitted: ${targetScholarship.title}",
            date = "Today",
            isUrgent = false,
            summary = "Your application ($appNo) has been submitted successfully and queued for review."
        )
        announcementsFlow.value = listOf(newAnnouncement) + announcementsFlow.value

        // Update digitalTwinFlow state
        digitalTwinFlow.value = digitalTwinFlow.value.copy(
            currentStatus = "Application Submitted",
            currentBlocker = "Awaiting Document Verification & Officer Review",
            nextBestAction = NextBestActionItem(
                title = "Track Application: ${targetScholarship.title}",
                reason = "Application $appNo was submitted successfully. Track verification progress in Applications.",
                actionText = "Track Status",
                actionType = "TRACK_APPLICATION"
            )
        )

        addAuditEvent(
            actor = "Student: ${profileFlow.value.fullName}",
            action = "Application Submitted",
            details = "Submitted application $appNo for ${targetScholarship.title}."
        )

        return Result.success(appNo)
    }

    override suspend fun login(studentId: String, passcode: String): Result<UserSession> {
        val trimmed = studentId.trim()
        val session = UserSession(
            isLoggedIn = true,
            studentId = if (trimmed.isNotEmpty()) trimmed else "ST2026-1042",
            studentName = "Anitha Kumar",
            mobileNumber = "+91 98765 XXXXX",
            lastLoginTime = "Just now"
        )
        sessionFlow.value = session
        addAuditEvent("Student: Anitha Kumar", "Login", "Signed in with Student ID.")
        return Result.success(session)
    }

    override suspend fun loginWithDemoAccount(): Result<UserSession> {
        val demoSession = UserSession(
            isLoggedIn = true,
            studentId = "ST2026-1042",
            studentName = "Anitha Kumar",
            mobileNumber = "+91 98765 XXXXX",
            lastLoginTime = "Just now"
        )
        sessionFlow.value = demoSession
        addAuditEvent("Student: Anitha Kumar", "Login", "Signed in with session.")
        return Result.success(demoSession)
    }

    override suspend fun logout() {
        sessionFlow.value = UserSession(isLoggedIn = false)
        addAuditEvent("Student: Anitha Kumar", "Logout", "Session ended.")
    }

    override suspend fun updateProfile(profile: StudentProfile) {
        val previousProfile = profileFlow.value
        profileFlow.value = profile

        // CONTINUOUS ELIGIBILITY RE-EVALUATION
        val hasIncomeChanged = previousProfile.familyIncome != profile.familyIncome
        val hasCourseChanged = previousProfile.courseName != profile.courseName
        val hasCategoryChanged = previousProfile.eligibilityCategory != profile.eligibilityCategory

        if (hasIncomeChanged || hasCourseChanged || hasCategoryChanged) {
            addAuditEvent(
                actor = "Continuous Eligibility Engine",
                action = "Continuous Eligibility Re-evaluation",
                details = "Student profile updated (Income: ${profile.familyIncome}, Course: ${profile.courseName}). Configured rules re-evaluated."
            )

            // Add notification
            announcementsFlow.value = listOf(
                Announcement(
                    id = "ANN-${System.currentTimeMillis() % 1000}",
                    title = "Eligibility Re-evaluated",
                    department = "ScholarBridge AI Rules",
                    date = "Today",
                    isUrgent = false,
                    summary = "Your eligibility information has changed. Configured scheme criteria were re-evaluated."
                )
            ) + announcementsFlow.value
        }

        recalculateUnifiedState()
    }

    // ==========================================
    // INNOVATION FEATURES
    // ==========================================

    override fun getReadiness(): Flow<ApplicationReadiness> = readinessFlow.asStateFlow()

    override fun getDeficiencies(): Flow<List<Deficiency>> = deficienciesFlow.asStateFlow()

    override fun getDigitalTwinState(): Flow<DigitalTwinState> = digitalTwinFlow.asStateFlow()

    override fun getCrossDocReport(): Flow<CrossDocReport> = crossDocReportFlow.asStateFlow()

    override fun getExplainableEligibilityGraph(schemeId: String): Flow<ExplainableEligibilityGraph> {
        val profile = profileFlow.value
        return MutableStateFlow(
            ExplainableEligibilityGraph(
                schemeId = schemeId,
                schemeTitle = "National Fellowship Scheme",
                overallStatus = RuleResult.PASS,
                ruleNodes = listOf(
                    EligibilityRuleNode(
                        id = "RULE-01",
                        ruleName = "Configured Income Ceiling",
                        conditionDescription = "Annual family income must not exceed ₹2,50,000 / annum.",
                        evidenceDocument = "Income Certificate (DOC-02)",
                        observedValue = profile.familyIncome,
                        ruleVersion = "v2.1",
                        result = RuleResult.PASS,
                        explanation = "Observed family income (${profile.familyIncome}) is within the configured limit of ₹2,50,000."
                    ),
                    EligibilityRuleNode(
                        id = "RULE-02",
                        ruleName = "Community Eligibility Condition",
                        conditionDescription = "Applicant must belong to eligible community category.",
                        evidenceDocument = "Community Certificate (DOC-01)",
                        observedValue = profile.eligibilityCategory,
                        ruleVersion = "v2.1",
                        result = RuleResult.PASS,
                        explanation = "Applicant category (${profile.eligibilityCategory}) verified from official community record."
                    ),
                    EligibilityRuleNode(
                        id = "RULE-03",
                        ruleName = "Academic Enrollment Condition",
                        conditionDescription = "Must be enrolled full-time in approved technical or professional program.",
                        evidenceDocument = "Academic Marksheet & Bonafide",
                        observedValue = "${profile.courseName} (${profile.academicYear})",
                        ruleVersion = "v2.1",
                        result = RuleResult.PASS,
                        explanation = "Enrolled student at ${profile.institutionName} with minimum required academic score."
                    )
                ),
                whyEligibleSummary = "Based on configured rules v2.1: Income condition passed, Community criteria satisfied, and Academic enrollment verified."
            )
        )
    }

    override fun getOfficerQueue(): Flow<List<OfficerApplicationQueueItem>> = officerQueueFlow.asStateFlow()

    override fun getDecisionReplay(applicationNo: String): Flow<List<DecisionReplayStep>> {
        return MutableStateFlow(
            listOf(
                DecisionReplayStep(
                    stepIndex = 1,
                    stepName = "Scheme Rules Ingested",
                    ruleVersion = "v2.1",
                    ruleApplied = "Income ceiling ≤ ₹2,50,000 & Community ST",
                    evidenceUsed = "Configured Scheme Policy File",
                    extractedData = "Thresholds defined",
                    eligibilityResult = "INITIALIZED",
                    officerReview = "System Automation",
                    recordedAction = "Rules loaded into engine",
                    timestamp = "04-Aug-2026 10:00 AM"
                ),
                DecisionReplayStep(
                    stepIndex = 2,
                    stepName = "Income Condition Evaluated",
                    ruleVersion = "v2.1",
                    ruleApplied = "Annual family income check",
                    evidenceUsed = "Income Certificate (INC/2026/4109)",
                    extractedData = "₹1,80,000 / annum",
                    eligibilityResult = "PASS",
                    officerReview = "AI Pre-screening Check",
                    recordedAction = "Condition marked satisfied",
                    timestamp = "12-Aug-2026 11:05 AM"
                ),
                DecisionReplayStep(
                    stepIndex = 3,
                    stepName = "AI Consistency Pre-Screening",
                    ruleVersion = "v2.1",
                    ruleApplied = "Cross-document identity consistency",
                    evidenceUsed = "Academic Marksheet vs Application",
                    extractedData = "Name: 'Anitha K. Kumar' vs 'Anitha Kumar'",
                    eligibilityResult = "NEEDS REVIEW",
                    officerReview = "AI Flagged",
                    recordedAction = "Flagged benign middle initial for officer confirmation",
                    timestamp = "12-Aug-2026 11:06 AM"
                ),
                DecisionReplayStep(
                    stepIndex = 4,
                    stepName = "Authorized Officer Scrutiny",
                    ruleVersion = "v2.1",
                    ruleApplied = "Officer verification of document authenticity",
                    evidenceUsed = "Uploaded Documents & Institutional record",
                    extractedData = "Anitha Kumar (ST2026-1042)",
                    eligibilityResult = "PASS",
                    officerReview = "Officer M. Raman",
                    recordedAction = "Officer confirmed name variation is valid initial. Overrode review flag.",
                    timestamp = "25-Aug-2026 02:40 PM"
                )
            )
        )
    }

    override fun getProvenanceGraph(applicationNo: String): Flow<List<ProvenanceNode>> {
        return MutableStateFlow(
            listOf(
                ProvenanceNode(
                    id = "PROV-1",
                    nodeType = "APPLICATION",
                    title = "Application ST2026-1042",
                    detail = "Submitted for Post-Matric Scholarship"
                ),
                ProvenanceNode(
                    id = "PROV-2",
                    nodeType = "DOCUMENT",
                    title = "Income Certificate (DOC-02)",
                    detail = "Issued by Revenue Authority, Demo District",
                    parentId = "PROV-1"
                ),
                ProvenanceNode(
                    id = "PROV-3",
                    nodeType = "EXTRACTED_FIELD",
                    title = "Extracted Income: ₹1,80,000",
                    detail = "OCR extracted with 99.4% confidence",
                    parentId = "PROV-2"
                ),
                ProvenanceNode(
                    id = "PROV-4",
                    nodeType = "RULE",
                    title = "Income Condition (Rule v2.1)",
                    detail = "Configured threshold: Ceiling ≤ ₹2,50,000",
                    parentId = "PROV-3"
                ),
                ProvenanceNode(
                    id = "PROV-5",
                    nodeType = "ELIGIBILITY_RESULT",
                    title = "Condition Result: PASS",
                    detail = "₹1,80,000 satisfies ≤ ₹2,50,000 criterion",
                    parentId = "PROV-4"
                ),
                ProvenanceNode(
                    id = "PROV-6",
                    nodeType = "OFFICER_ACTION",
                    title = "Officer Review Desk",
                    detail = "Reviewed by Officer M. Raman (Tamil Nadu Cadre)",
                    parentId = "PROV-5"
                ),
                ProvenanceNode(
                    id = "PROV-7",
                    nodeType = "DECISION",
                    title = "Official Recommendation",
                    detail = "Qualified for Award Processing",
                    parentId = "PROV-6"
                )
            )
        )
    }

    override fun getAuditEvents(): Flow<List<AuditEvent>> = auditEventsFlow.asStateFlow()

    override suspend fun resolveDeficiency(deficiencyId: String, resolutionNote: String) {
        deficienciesFlow.value = deficienciesFlow.value.map {
            if (it.id == deficiencyId) {
                it.copy(
                    status = DeficiencyStatus.RESOLVED,
                    resolvedNote = resolutionNote
                )
            } else it
        }

        addAuditEvent(
            actor = "Student: ${profileFlow.value.fullName}",
            action = "Deficiency Resolved",
            details = "Resolved deficiency $deficiencyId: $resolutionNote"
        )

        recalculateUnifiedState()
    }

    override suspend fun updateOfficerAction(
        applicationNo: String,
        action: OfficerActionType,
        notes: String
    ) {
        officerQueueFlow.value = officerQueueFlow.value.map {
            if (it.applicationNo == applicationNo) {
                it.copy(
                    officerAction = action,
                    officerNotes = notes,
                    applicationStatus = when (action) {
                        OfficerActionType.CONFIRMED -> "Reviewed & Approved by Officer"
                        OfficerActionType.OVERRIDDEN -> "AI Finding Overridden"
                        OfficerActionType.CORRECTION_REQUESTED -> "Correction Requested"
                        OfficerActionType.MOVED_TO_SCRUTINY -> "Moved to Higher Scrutiny"
                        OfficerActionType.PENDING -> "Under Officer Review"
                    }
                )
            } else it
        }

        // If this matches the student application, update it as well
        applicationsFlow.value = applicationsFlow.value.map {
            if (it.applicationNo == applicationNo) {
                val newStatus = when (action) {
                    OfficerActionType.CONFIRMED, OfficerActionType.MOVED_TO_SCRUTINY -> ApplicationStage.OFFICIAL_DECISION
                    else -> ApplicationStage.OFFICER_REVIEW
                }
                it.copy(
                    currentStatus = newStatus,
                    remarks = "Officer review update: ${action.label}. $notes"
                )
            } else it
        }

        addAuditEvent(
            actor = "Officer Review Desk",
            action = "Officer Decision Recorded",
            details = "Updated $applicationNo to ${action.label}. Notes: $notes"
        )

        // Generate student notification
        announcementsFlow.value = listOf(
            Announcement(
                id = "ANN-${System.currentTimeMillis() % 1000}",
                title = "Application Update: ${action.label}",
                department = "Officer Review Desk",
                date = "Today",
                isUrgent = action == OfficerActionType.CORRECTION_REQUESTED,
                summary = "Your application $applicationNo has been updated by authorized officials: $notes"
            )
        ) + announcementsFlow.value

        recalculateUnifiedState()
    }

    override suspend fun uploadSyntheticDocument(docType: String, fileName: String) {
        val updatedDocs = profileFlow.value.documents.map {
            if (it.documentType == docType || it.name.contains(docType, ignoreCase = true)) {
                it.copy(
                    verificationStatus = DocumentStatus.UPLOADED,
                    issueDate = "Current Academic Year"
                )
            } else it
        }
        profileFlow.value = profileFlow.value.copy(documents = updatedDocs)

        // Automatically resolve matching deficiency if it was a missing document
        if (docType.contains("Bonafide", ignoreCase = true)) {
            resolveDeficiency("DEF-01", "Uploaded $fileName from Demo Institute of Technology.")
        }

        addAuditEvent(
            actor = "Student: ${profileFlow.value.fullName}",
            action = "Document Uploaded",
            details = "Uploaded $fileName ($docType)."
        )

        recalculateUnifiedState()
    }

    override fun runRuleSimulation(
        schemeTitle: String,
        proposedCeiling: String
    ): RuleChangeSimulation {
        val ceilingValue = proposedCeiling.filter { it.isDigit() }.toIntOrNull() ?: 250000
        val isRelaxed = ceilingValue > 200000

        val newlyMatching = if (isRelaxed) 8 else 0
        val noLonger = if (!isRelaxed) 4 else 0
        val needsRev = 3
        val noChange = 24 - (newlyMatching + noLonger + needsRev)

        return RuleChangeSimulation(
            schemeTitle = schemeTitle,
            parameterName = "Annual Income Ceiling",
            currentRuleDescription = "Annual family income ≤ ₹2,00,000 / annum",
            proposedRuleDescription = "Annual family income ≤ $proposedCeiling / annum",
            newlyMatchingCount = newlyMatching,
            noLongerMatchingCount = noLonger,
            needsReviewCount = needsRev,
            noChangeCount = noChange,
            simulationTimestamp = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH).format(Date())
        )
    }

    override fun testSandboxRule(rule: SandboxRule): Pair<Int, Int> {
        val incomeLimit = rule.incomeCeiling.filter { it.isDigit() }.toIntOrNull() ?: 250000
        val minScore = rule.minScorePercentage.filter { it.isDigit() }.toIntOrNull() ?: 60

        // Test against 24 synthetic applicants
        var passed = 0
        val total = 24
        for (i in 1..total) {
            val applicantIncome = 120000 + (i * 7000)
            val applicantScore = 55 + (i * 2)
            if (applicantIncome <= incomeLimit && applicantScore >= minScore) {
                passed++
            }
        }
        return Pair(passed, total)
    }
}
