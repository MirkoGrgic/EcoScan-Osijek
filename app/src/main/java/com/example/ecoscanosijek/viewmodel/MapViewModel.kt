package com.example.ecoscanosijek.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoscanosijek.model.Report
import com.example.ecoscanosijek.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapViewModel(private val reportRepository: ReportRepository) : ViewModel() {
    private val _allReports = MutableStateFlow<List<Report>>(emptyList())
    val allReports: StateFlow<List<Report>> = _allReports.asStateFlow()

    init {
        loadAllReports()
    }

    private fun loadAllReports() {
        viewModelScope.launch {
            reportRepository.getAllReports().collectLatest {
                _allReports.value = it
            }
        }
    }
    fun deleteReport(reportId: String) {
        viewModelScope.launch {
            reportRepository.deleteReport(reportId)
        }
    }
}
