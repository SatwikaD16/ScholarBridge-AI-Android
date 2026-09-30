package com.tribalscholar.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tribalscholar.app.data.model.Scholarship
import com.tribalscholar.app.data.model.ScholarshipCategory
import com.tribalscholar.app.data.repository.MockScholarshipRepository
import com.tribalscholar.app.data.repository.ScholarshipRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ScholarshipUiState(
    val searchQuery: String = "",
    val selectedCategory: ScholarshipCategory = ScholarshipCategory.ALL,
    val selectedScholarship: Scholarship? = null,
    val isApplying: Boolean = false,
    val applicationSuccessNumber: String? = null,
    val applicationError: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class ScholarshipViewModel(
    private val repository: ScholarshipRepository = MockScholarshipRepository.getInstance()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ScholarshipCategory.ALL)
    val selectedCategory: StateFlow<ScholarshipCategory> = _selectedCategory.asStateFlow()

    private val _uiState = MutableStateFlow(ScholarshipUiState())
    val uiState: StateFlow<ScholarshipUiState> = _uiState.asStateFlow()

    val scholarships: StateFlow<List<Scholarship>> = combine(_searchQuery, _selectedCategory) { query, category ->
        Pair(query, category)
    }.flatMapLatest { (query, category) ->
        repository.getScholarships(query, category)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategorySelected(category: ScholarshipCategory) {
        _selectedCategory.value = category
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun selectScholarship(scholarship: Scholarship?) {
        _uiState.value = _uiState.value.copy(selectedScholarship = scholarship)
    }

    fun toggleBookmark(scholarshipId: String) {
        viewModelScope.launch {
            repository.toggleBookmark(scholarshipId)
            // also update current selected if open
            if (_uiState.value.selectedScholarship?.id == scholarshipId) {
                val current = _uiState.value.selectedScholarship
                if (current != null) {
                    _uiState.value = _uiState.value.copy(
                        selectedScholarship = current.copy(isBookmarked = !current.isBookmarked)
                    )
                }
            }
        }
    }

    fun applyForScholarship(scholarshipId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isApplying = true, applicationError = null)
            val result = repository.applyForScholarship(scholarshipId)
            _uiState.value = _uiState.value.copy(isApplying = false)

            result.onSuccess { appNumber ->
                _uiState.value = _uiState.value.copy(
                    applicationSuccessNumber = appNumber,
                    selectedScholarship = _uiState.value.selectedScholarship?.copy(hasApplied = true)
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    applicationError = error.localizedMessage ?: "Failed to submit application"
                )
            }
        }
    }

    fun dismissSuccessDialog() {
        _uiState.value = _uiState.value.copy(applicationSuccessNumber = null)
    }

    fun dismissErrorDialog() {
        _uiState.value = _uiState.value.copy(applicationError = null)
    }
}
