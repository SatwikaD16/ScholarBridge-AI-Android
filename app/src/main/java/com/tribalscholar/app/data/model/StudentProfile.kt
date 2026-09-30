package com.tribalscholar.app.data.model

data class StudentProfile(
    val studentId: String = "ST2026-1042",
    val fullName: String = "Anitha Kumar",
    val dateOfBirth: String = "14-Aug-2004",
    val gender: String = "Female",
    val stateOfDomicile: String = "Tamil Nadu",
    val district: String = "Demo District",
    val email: String = "anitha.kumar@example.com",
    val mobileNumber: String = "+91 98765 XXXXX",
    
    // Eligibility & Category Information
    val eligibilityCategory: String = "Scheduled Tribe (ST)",
    val eligibilityStatus: String = "Information provided",
    val eligibilityVerificationNote: String = "Eligibility information provided by student",
    val familyIncome: String = "₹1,80,000 / annum",
    
    // Academic Credentials
    val academicLevel: String = "Undergraduate",
    val courseName: String = "B.Tech in Artificial Intelligence & Data Science",
    val institutionName: String = "Demo Institute of Technology",
    val academicYear: String = "2nd Year",
    val scorePercentage: String = "8.8 CGPA (85%)",
    
    // Neutral Scholarship Payment Details
    val paymentTitle: String = "Scholarship Payment Details",
    val paymentInfoNote: String = "Payment information available after official verification",
    val paymentStatus: String = "Not yet verified",
    
    // Digital Documents
    val documents: List<UploadedDocument> = emptyList()
)

data class UploadedDocument(
    val id: String,
    val name: String,
    val documentType: String,
    val issueDate: String,
    val verificationStatus: DocumentStatus,
    val source: String = "Digital Document"
)

enum class DocumentStatus(val label: String) {
    AVAILABLE_FOR_VERIFICATION("Available for verification"),
    UPLOADED("Uploaded"),
    REQUIRED("Required")
}
