package com.tribalscholar.app.data.repository

import com.tribalscholar.app.data.model.Announcement
import com.tribalscholar.app.data.model.ApplicationItem
import com.tribalscholar.app.data.model.ApplicationReadiness
import com.tribalscholar.app.data.model.AuditEvent
import com.tribalscholar.app.data.model.CrossDocReport
import com.tribalscholar.app.data.model.DashboardMetrics
import com.tribalscholar.app.data.model.DecisionReplayStep
import com.tribalscholar.app.data.model.Deficiency
import com.tribalscholar.app.data.model.DigitalTwinState
import com.tribalscholar.app.data.model.ExplainableEligibilityGraph
import com.tribalscholar.app.data.model.OfficerActionType
import com.tribalscholar.app.data.model.OfficerApplicationQueueItem
import com.tribalscholar.app.data.model.ProvenanceNode
import com.tribalscholar.app.data.model.RuleChangeSimulation
import com.tribalscholar.app.data.model.SandboxRule
import com.tribalscholar.app.data.model.Scholarship
import com.tribalscholar.app.data.model.ScholarshipCategory
import com.tribalscholar.app.data.model.StudentProfile
import com.tribalscholar.app.data.model.UserSession
import kotlinx.coroutines.flow.Flow

interface ScholarshipRepository {
    fun getSession(): Flow<UserSession>
    fun getStudentProfile(): Flow<StudentProfile>
    fun getDashboardMetrics(): Flow<DashboardMetrics>
    fun getActiveApplications(): Flow<List<ApplicationItem>>
    fun getScholarships(
        query: String = "",
        category: ScholarshipCategory = ScholarshipCategory.ALL
    ): Flow<List<Scholarship>>
    fun getScholarshipById(id: String): Flow<Scholarship?>
    fun getAnnouncements(): Flow<List<Announcement>>
    suspend fun toggleBookmark(scholarshipId: String)
    suspend fun applyForScholarship(scholarshipId: String): Result<String>
    suspend fun login(studentId: String, passcode: String): Result<UserSession>
    suspend fun loginWithDemoAccount(): Result<UserSession>
    suspend fun logout()
    suspend fun updateProfile(profile: StudentProfile)

    // Shared State & End-to-End Innovation Layer
    fun getReadiness(): Flow<ApplicationReadiness>
    fun getDeficiencies(): Flow<List<Deficiency>>
    fun getDigitalTwinState(): Flow<DigitalTwinState>
    fun getCrossDocReport(): Flow<CrossDocReport>
    fun getExplainableEligibilityGraph(schemeId: String): Flow<ExplainableEligibilityGraph>
    fun getOfficerQueue(): Flow<List<OfficerApplicationQueueItem>>
    fun getDecisionReplay(applicationNo: String): Flow<List<DecisionReplayStep>>
    fun getProvenanceGraph(applicationNo: String): Flow<List<ProvenanceNode>>
    fun getAuditEvents(): Flow<List<AuditEvent>>
    suspend fun resolveDeficiency(deficiencyId: String, resolutionNote: String)
    suspend fun updateOfficerAction(applicationNo: String, action: OfficerActionType, notes: String)
    suspend fun uploadSyntheticDocument(docType: String, fileName: String)
    fun runRuleSimulation(schemeTitle: String, proposedCeiling: String): RuleChangeSimulation
    fun testSandboxRule(rule: SandboxRule): Pair<Int, Int> // (passCount, totalCount)
    fun getUserRole(): Flow<String> // "STUDENT" or "OFFICER"
    suspend fun setUserRole(role: String)
}
