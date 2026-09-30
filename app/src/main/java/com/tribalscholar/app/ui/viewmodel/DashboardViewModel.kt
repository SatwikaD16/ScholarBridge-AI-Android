package com.tribalscholar.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tribalscholar.app.data.model.Announcement
import com.tribalscholar.app.data.model.ApplicationItem
import com.tribalscholar.app.data.model.DashboardMetrics
import com.tribalscholar.app.data.model.StudentProfile
import com.tribalscholar.app.data.repository.MockScholarshipRepository
import com.tribalscholar.app.data.repository.ScholarshipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isRefreshing: Boolean = false,
    val selectedApplication: ApplicationItem? = null
)

class DashboardViewModel(
    private val repository: ScholarshipRepository = MockScholarshipRepository.getInstance()
) : ViewModel() {

    val profile: StateFlow<StudentProfile?> = repository.getStudentProfile()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val metrics: StateFlow<DashboardMetrics?> = repository.getDashboardMetrics()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val activeApplications: StateFlow<List<ApplicationItem>> = repository.getActiveApplications()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val announcements: StateFlow<List<Announcement>> = repository.getAnnouncements()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun selectApplication(applicationItem: ApplicationItem?) {
        _uiState.value = _uiState.value.copy(selectedApplication = applicationItem)
    }

    fun refreshDashboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            kotlinx.coroutines.delay(800)
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }
}
