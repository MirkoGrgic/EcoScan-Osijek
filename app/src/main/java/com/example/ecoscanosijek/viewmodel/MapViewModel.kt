package com.example.ecoscanosijek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecoscanosijek.model.Report
import com.example.ecoscanosijek.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapViewModel(private val reportRepository: ReportRepository) : ViewModel() {
    private val _allReports = MutableStateFlow<List<Report>>(emptyList())
    val allReports: StateFlow<List<Report>> = _allReports.asStateFlow()

    init {
        loadAllReports()
    }

    private fun loadAllReports() {
        _allReports.value = reportRepository.getAllReports()
    }
}
