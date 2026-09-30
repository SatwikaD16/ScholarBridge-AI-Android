package com.tribalscholar.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tribalscholar.app.data.model.StudentProfile
import com.tribalscholar.app.data.model.UploadedDocument
import com.tribalscholar.app.data.repository.MockScholarshipRepository
import com.tribalscholar.app.data.repository.ScholarshipRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isEditMode: Boolean = false,
    val isConnectingDocumentSource: Boolean = false,
    val selectedDocument: UploadedDocument? = null,
    val statusMessage: String? = null
)

class ProfileViewModel(
    private val repository: ScholarshipRepository = MockScholarshipRepository.getInstance()
) : ViewModel() {

    val profile: StateFlow<StudentProfile?> = repository.getStudentProfile()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun selectDocument(doc: UploadedDocument?) {
        _uiState.value = _uiState.value.copy(selectedDocument = doc)
    }

    fun connectDigitalDocumentSource() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isConnectingDocumentSource = true, statusMessage = null)
            delay(1000)
            _uiState.value = _uiState.value.copy(
                isConnectingDocumentSource = false,
                statusMessage = "Digital document source connected. Available documents ready for verification."
            )
        }
    }



    fun clearStatusMessage() {
        _uiState.value = _uiState.value.copy(statusMessage = null)
    }

    fun updateProfile(updated: StudentProfile) {
        viewModelScope.launch {
            repository.updateProfile(updated)
        }
    }
}
