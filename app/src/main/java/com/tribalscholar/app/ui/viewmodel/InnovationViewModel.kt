package com.tribalscholar.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tribalscholar.app.data.model.ApplicationReadiness
import com.tribalscholar.app.data.model.AuditEvent
import com.tribalscholar.app.data.model.CrossDocReport
import com.tribalscholar.app.data.model.DecisionReplayStep
import com.tribalscholar.app.data.model.Deficiency
import com.tribalscholar.app.data.model.DigitalTwinState
import com.tribalscholar.app.data.model.ExplainableEligibilityGraph
import com.tribalscholar.app.data.model.OfficerActionType
import com.tribalscholar.app.data.model.OfficerApplicationQueueItem
import com.tribalscholar.app.data.model.ProvenanceNode
import com.tribalscholar.app.data.model.RuleChangeSimulation
import com.tribalscholar.app.data.model.SandboxRule
import com.tribalscholar.app.data.repository.MockScholarshipRepository
import com.tribalscholar.app.data.repository.ScholarshipRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InnovationViewModel(
    private val repository: ScholarshipRepository = MockScholarshipRepository.getInstance()
) : ViewModel() {

    val userRole: StateFlow<String> = repository.getUserRole()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "STUDENT")

    val readiness: StateFlow<ApplicationReadiness?> = repository.getReadiness()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val deficiencies: StateFlow<List<Deficiency>> = repository.getDeficiencies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val digitalTwin: StateFlow<DigitalTwinState?> = repository.getDigitalTwinState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val crossDocReport: StateFlow<CrossDocReport?> = repository.getCrossDocReport()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val explainableEligibility: StateFlow<ExplainableEligibilityGraph?> =
        repository.getExplainableEligibilityGraph("SCH-001")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val officerQueue: StateFlow<List<OfficerApplicationQueueItem>> = repository.getOfficerQueue()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditEvents: StateFlow<List<AuditEvent>> = repository.getAuditEvents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getDecisionReplay(applicationNo: String): StateFlow<List<DecisionReplayStep>> {
        return repository.getDecisionReplay(applicationNo)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getProvenanceGraph(applicationNo: String): StateFlow<List<ProvenanceNode>> {
        return repository.getProvenanceGraph(applicationNo)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun setUserRole(role: String) {
        viewModelScope.launch {
            repository.setUserRole(role)
        }
    }

    fun resolveDeficiency(deficiencyId: String, note: String) {
        viewModelScope.launch {
            repository.resolveDeficiency(deficiencyId, note)
        }
    }

    fun uploadSyntheticDocument(docType: String, fileName: String) {
        viewModelScope.launch {
            repository.uploadSyntheticDocument(docType, fileName)
        }
    }

    fun updateOfficerAction(applicationNo: String, action: OfficerActionType, notes: String) {
        viewModelScope.launch {
            repository.updateOfficerAction(applicationNo, action, notes)
        }
    }

    fun runRuleSimulation(schemeTitle: String, proposedCeiling: String): RuleChangeSimulation {
        return repository.runRuleSimulation(schemeTitle, proposedCeiling)
    }

    fun testSandboxRule(rule: SandboxRule): Pair<Int, Int> {
        return repository.testSandboxRule(rule)
    }
}
