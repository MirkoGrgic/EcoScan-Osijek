package com.example.ecoscanosijek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecoscanosijek.model.Report
import com.example.ecoscanosijek.model.ReportStatus
import com.example.ecoscanosijek.model.User
import com.example.ecoscanosijek.model.UserRole
import com.example.ecoscanosijek.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReportViewModel(private val reportRepository: ReportRepository) : ViewModel() {
    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()

    fun loadReports(user: User?) {
        if (user == null) {
            _reports.value = emptyList()
            return
        }

        val allReports = reportRepository.getAllReports()
        _reports.value = when (user.role) {
            UserRole.MUNICIPAL_WORKER -> allReports
            UserRole.CITIZEN -> allReports.filter { it.userId == user.id }
        }
    }

    fun addReport(description: String, latitude: Double, longitude: Double, userId: String) {
        val newReport = Report(
            id = java.util.UUID.randomUUID().toString(),
            userId = userId,
            imageUrl = "", // TODO: Camera integration
            description = description,
            latitude = latitude,
            longitude = longitude,
            status = ReportStatus.NEW,
            createdAt = System.currentTimeMillis()
        )
        reportRepository.addReport(newReport)
        // Refresh
        val currentReports = _reports.value.toMutableList()
        currentReports.add(0, newReport)
        _reports.value = currentReports
    }

    fun getReportById(id: String): Report? {
        return reportRepository.getReportById(id)
    }

    fun updateReportStatus(reportId: String, status: ReportStatus) {
        reportRepository.updateReportStatus(reportId, status)
        // Update local list to reflect changes in UI
        _reports.value = _reports.value.map {
            if (it.id == reportId) it.copy(status = status) else it
        }
    }
}
