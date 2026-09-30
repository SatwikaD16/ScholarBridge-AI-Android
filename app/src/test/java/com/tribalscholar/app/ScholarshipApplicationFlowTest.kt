package com.tribalscholar.app

import com.tribalscholar.app.data.model.ApplicationStage
import com.tribalscholar.app.data.repository.MockScholarshipRepository
import com.tribalscholar.app.ui.viewmodel.DashboardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScholarshipApplicationFlowTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: MockScholarshipRepository
    private lateinit var dashboardViewModel: DashboardViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = MockScholarshipRepository.resetInstance()
        dashboardViewModel = DashboardViewModel(repository)
    }

    @After
    fun tearDown() {
        MockScholarshipRepository.resetInstance()
        Dispatchers.resetMain()
    }

    @Test
    fun testSuccessfulScholarshipApplicationSubmission() = runTest {
        val scholarshipId = "SCH-001"
        val initialApps = repository.getActiveApplications().first()
        val initialCount = initialApps.size

        val result = repository.applyForScholarship(scholarshipId)
        assertTrue("Application submission must succeed", result.isSuccess)
        val appNo = result.getOrNull()
        assertNotNull("Generated application number must not be null", appNo)
        assertTrue("Application number should start with ST2026-", appNo!!.startsWith("ST2026-"))

        // Check active applications
        val updatedApps = repository.getActiveApplications().first()
        assertEquals("Active applications count should increase by 1", initialCount + 1, updatedApps.size)

        val latestApp = updatedApps.first()
        assertEquals("Application number matches", appNo, latestApp.applicationNo)
        assertEquals("Scholarship ID matches", scholarshipId, latestApp.scholarshipId)
        assertEquals("Status is APPLICATION_SUBMITTED", ApplicationStage.APPLICATION_SUBMITTED, latestApp.currentStatus)
        assertEquals("Status title is Application Submitted", "Application Submitted", latestApp.currentStatus.title)

        // Check scholarship hasApplied flag
        val scholarship = repository.getScholarshipById(scholarshipId).first()
        assertNotNull("Scholarship exists", scholarship)
        assertTrue("Scholarship marked as applied", scholarship!!.hasApplied)

        // Check dashboard metrics flow
        val metrics = repository.getDashboardMetrics().first()
        assertEquals("Dashboard active count updated", updatedApps.size, metrics.activeApplicationsCount)
        assertEquals("Dashboard application status is Application Submitted", "Application Submitted", metrics.applicationStatusText)

        // Check announcements
        val announcements = repository.getAnnouncements().first()
        assertTrue("Announcements contains application submission", announcements.any { it.title.contains("Application Submitted") })

        // Check timeline milestones
        assertTrue("Timeline has milestones", latestApp.timeline.isNotEmpty())
        assertEquals("First timeline milestone is Application Submitted", ApplicationStage.APPLICATION_SUBMITTED, latestApp.timeline.first().stage)
        assertTrue("First milestone is completed", latestApp.timeline.first().isCompleted)
    }

    @Test
    fun testMultipleDistinctScholarshipApplications() = runTest {
        val initialCount = repository.getActiveApplications().first().size

        // First application: SCH-001
        val res1 = repository.applyForScholarship("SCH-001")
        assertTrue("First application succeeds", res1.isSuccess)
        val appNo1 = res1.getOrNull()!!

        // Second application: SCH-002
        val res2 = repository.applyForScholarship("SCH-002")
        assertTrue("Second application succeeds", res2.isSuccess)
        val appNo2 = res2.getOrNull()!!

        // Verify distinct numbers
        assertNotEquals("Different applications must have distinct application numbers", appNo1, appNo2)

        val allApps = repository.getActiveApplications().first()
        assertEquals("Total active applications increased by 2", initialCount + 2, allApps.size)

        val app1 = allApps.find { it.applicationNo == appNo1 }
        val app2 = allApps.find { it.applicationNo == appNo2 }

        assertNotNull("First application present", app1)
        assertNotNull("Second application present", app2)

        assertEquals("First application has SCH-001", "SCH-001", app1!!.scholarshipId)
        assertEquals("Second application has SCH-002", "SCH-002", app2!!.scholarshipId)

        assertEquals("First application has status Application Submitted", ApplicationStage.APPLICATION_SUBMITTED, app1.currentStatus)
        assertEquals("Second application has status Application Submitted", ApplicationStage.APPLICATION_SUBMITTED, app2.currentStatus)

        // Verify both scholarships marked hasApplied
        val s1 = repository.getScholarshipById("SCH-001").first()
        val s2 = repository.getScholarshipById("SCH-002").first()
        assertTrue("SCH-001 marked applied", s1!!.hasApplied)
        assertTrue("SCH-002 marked applied", s2!!.hasApplied)
    }

    @Test
    fun testDashboardViewModelReflectsSubmission() = runTest {
        backgroundScope.launch { dashboardViewModel.activeApplications.collect {} }
        advanceUntilIdle()

        val initialCount = dashboardViewModel.activeApplications.value.size

        val res = repository.applyForScholarship("SCH-003")
        assertTrue(res.isSuccess)
        advanceUntilIdle()

        val updatedApps = dashboardViewModel.activeApplications.value
        assertEquals(initialCount + 1, updatedApps.size)
        assertEquals(ApplicationStage.APPLICATION_SUBMITTED, updatedApps.first().currentStatus)
    }
}
