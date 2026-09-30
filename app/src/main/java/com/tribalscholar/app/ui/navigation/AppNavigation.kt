package com.tribalscholar.app.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tribalscholar.app.ui.components.BottomNavBar
import com.tribalscholar.app.ui.screens.ApplicationFormScreen
import com.tribalscholar.app.ui.screens.ApplicationsScreen
import com.tribalscholar.app.ui.screens.ConsentScreen
import com.tribalscholar.app.ui.screens.DashboardScreen
import com.tribalscholar.app.ui.screens.DocumentsScreen
import com.tribalscholar.app.ui.screens.FindScholarshipsScreen
import com.tribalscholar.app.ui.screens.LoginScreen
import com.tribalscholar.app.ui.screens.OfficerPortalScreen
import com.tribalscholar.app.ui.screens.ProfileScreen
import com.tribalscholar.app.ui.screens.SplashScreen
import com.tribalscholar.app.ui.viewmodel.AuthViewModel
import com.tribalscholar.app.ui.viewmodel.DashboardViewModel
import com.tribalscholar.app.ui.viewmodel.InnovationViewModel
import com.tribalscholar.app.ui.viewmodel.ProfileViewModel
import com.tribalscholar.app.ui.viewmodel.ScholarshipViewModel

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.tribalscholar.app.util.LocaleHelper

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    dashboardViewModel: DashboardViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    scholarshipViewModel: ScholarshipViewModel = viewModel(),
    innovationViewModel: InnovationViewModel = viewModel()
) {
    val context = LocalContext.current
    var currentLanguageCode by remember { mutableStateOf(LocaleHelper.getSavedLanguage(context)) }
    val localizedContext = remember(currentLanguageCode) {
        LocaleHelper.createLocalizedContext(context, currentLanguageCode)
    }

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        androidx.compose.ui.platform.LocalConfiguration provides localizedContext.resources.configuration
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Show bottom navigation bar on the 5 core tabs
        val mainTabs = listOf(
            Screen.Dashboard.route,
            Screen.FindScholarships.route,
            Screen.Applications.route,
            Screen.Documents.route,
            Screen.Profile.route
        )
        val showBottomBar = currentRoute in mainTabs

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { targetRoute ->
                        if (targetRoute != currentRoute) {
                            navController.navigate(targetRoute) {
                                popUpTo(Screen.Dashboard.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.fillMaxSize(),
                enterTransition = { fadeIn(animationSpec = tween(250)) },
                exitTransition = { fadeOut(animationSpec = tween(250)) }
            ) {
            // Screen 1: Splash Screen
            composable(Screen.Splash.route) {
                SplashScreen(
                    onTimeout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // Screen 2: Login Screen
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    currentLanguageCode = currentLanguageCode,
                    onLanguageSelected = { newLang ->
                        LocaleHelper.setLanguage(context, newLang)
                        currentLanguageCode = newLang
                    },
                    onLoginSuccess = {
                        navController.navigate(Screen.Consent.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // Screen 3: Consent Screen (Section 6)
            composable(Screen.Consent.route) {
                ConsentScreen(
                    onContinue = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Consent.route) { inclusive = true }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Tab 1: Dashboard / Home Screen (Section 8)
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = dashboardViewModel,
                    innovationViewModel = innovationViewModel,
                    onNavigateToScholarships = {
                        navController.navigate(Screen.FindScholarships.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToApplications = {
                        navController.navigate(Screen.Applications.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToDocuments = {
                        navController.navigate(Screen.Documents.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onStartApplication = { scholarshipId ->
                        navController.navigate(Screen.ApplicationForm.createRoute(scholarshipId))
                    }
                )
            }

            // Tab 2: Find Scholarships Screen (Section 9)
            composable(Screen.FindScholarships.route) {
                FindScholarshipsScreen(
                    viewModel = scholarshipViewModel,
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onStartApplication = { scholarshipId ->
                        navController.navigate(Screen.ApplicationForm.createRoute(scholarshipId))
                    }
                )
            }

            // Tab 3: Applications & Tracking Screen (Section 15)
            composable(Screen.Applications.route) {
                ApplicationsScreen(
                    viewModel = dashboardViewModel,
                    onNavigateToScholarships = {
                        navController.navigate(Screen.FindScholarships.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            // Tab 4: Documents Center Screen (Section 12 & 13)
            composable(Screen.Documents.route) {
                DocumentsScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            // Tab 5: Profile Screen (Section 22)
            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = profileViewModel,
                    currentLanguageCode = currentLanguageCode,
                    onLanguageSelected = { newLang ->
                        LocaleHelper.setLanguage(context, newLang)
                        currentLanguageCode = newLang
                    },
                    onNavigateToOfficerPortal = {
                        navController.navigate(Screen.OfficerPortal.route)
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onLogout = {
                        authViewModel.logout {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }

            // Officer Experience Screen (Section 4 & 14-22)
            composable(Screen.OfficerPortal.route) {
                OfficerPortalScreen(
                    viewModel = innovationViewModel,
                    onBackToStudent = {
                        navController.popBackStack()
                    }
                )
            }

            // Application Form Process (Section 11)
            composable(
                route = Screen.ApplicationForm.route,
                arguments = listOf(navArgument("scholarshipId") { type = NavType.StringType; defaultValue = "SCH-001" })
            ) { backStackEntry ->
                val sId = backStackEntry.arguments?.getString("scholarshipId") ?: "SCH-001"
                ApplicationFormScreen(
                    scholarshipId = sId,
                    onFinish = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
}
}
