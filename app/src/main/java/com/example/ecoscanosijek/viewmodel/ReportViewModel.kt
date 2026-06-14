package com.example.ecoscanosijek.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoscanosijek.model.Report
import com.example.ecoscanosijek.model.ReportStatus
import com.example.ecoscanosijek.model.User
import com.example.ecoscanosijek.model.UserRole
import com.example.ecoscanosijek.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import kotlinx.coroutines.Job

class ReportViewModel(private val reportRepository: ReportRepository) : ViewModel() {
    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    private var reportsJob: Job? = null
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()


    fun loadReports(user: User?) {
        reportsJob?.cancel()
        _reports.value = emptyList()

        if (user == null) {
            return
        }

        reportsJob = viewModelScope.launch {
            val reportsFlow = when (user.role) {
                UserRole.WORKER -> reportRepository.getAllReports()
                UserRole.CITIZEN -> reportRepository.getReportsForUser(user.id)
            }

            reportsFlow.collectLatest {
                _reports.value = it
            }
        }
    }
    fun clearReports() {
        reportsJob?.cancel()
        _reports.value = emptyList()
    }

    fun addReport(
        description: String,
        latitude: Double,
        longitude: Double,
        userId: String,
        imageUri: Uri?,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (imageUri != null) {
            reportRepository.uploadImage(imageUri) { imageUrl ->
                viewModelScope.launch {
                    try {
                        saveReport(
                            description = description,
                            latitude = latitude,
                            longitude = longitude,
                            userId = userId,
                            imageUrl = imageUrl ?: ""
                        )
                        onSuccess()
                    } catch (e: Exception) {
                        onError("Greška pri spremanju prijave.")
                    }
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    saveReport(
                        description = description,
                        latitude = latitude,
                        longitude = longitude,
                        userId = userId,
                        imageUrl = ""
                    )
                    onSuccess()
                } catch (e: Exception) {
                    onError("Greška pri spremanju prijave.")
                }
            }
        }
    }

    private suspend fun saveReport(
        description: String,
        latitude: Double,
        longitude: Double,
        userId: String,
        imageUrl: String
    ) {
        val newReport = Report(
            id = UUID.randomUUID().toString(),
            userId = userId,
            imageUrl = imageUrl,
            description = description,
            latitude = latitude,
            longitude = longitude,
            status = ReportStatus.NEW,
            createdAt = System.currentTimeMillis(),
            pointsAwarded = false
        )

        reportRepository.addReport(newReport)
    }

    fun getReportById(id: String, onResult: (Report?) -> Unit) {
        viewModelScope.launch {
            val report = reportRepository.getReportById(id)
            onResult(report)
        }
    }

    fun updateReportStatus(
        reportId: String,
        status: ReportStatus,
        workerId: String? = null
    ) {
        viewModelScope.launch {
            reportRepository.updateReportStatus(reportId, status,workerId)
        }
    }
}
