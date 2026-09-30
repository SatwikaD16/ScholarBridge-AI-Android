package com.tribalscholar.app.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Splash : Screen("splash", "ScholarBridge AI")
    object Login : Screen("login", "Sign In")
    object Consent : Screen("consent", "Before We Continue")
    object Dashboard : Screen("dashboard", "Home")
    object FindScholarships : Screen("scholarships", "Scholarships")
    object Applications : Screen("applications", "Applications")
    object Documents : Screen("documents", "Documents")
    object Profile : Screen("profile", "Profile")
    object ApplicationForm : Screen("apply/{scholarshipId}", "Apply for Scholarship") {
        fun createRoute(scholarshipId: String) = "apply/$scholarshipId"
    }
    object OfficerPortal : Screen("officer_portal", "Officer Experience")
}

