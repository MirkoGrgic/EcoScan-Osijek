package com.example.ecoscanosijek.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoscanosijek.model.Report
import com.example.ecoscanosijek.notifications.NotificationHelper
import com.example.ecoscanosijek.repository.ReportRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.ecoscanosijek.model.ReportStatus


class MapViewModel(private val reportRepository: ReportRepository) : ViewModel() {
    private val _allReports = MutableStateFlow<List<Report>>(emptyList())
    val allReports: StateFlow<List<Report>> = _allReports.asStateFlow()

    private var firstLoad = true
    private var listenerStarted = false

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
    fun listenForNewReports(context: Context) {
        if (listenerStarted) return
        listenerStarted = true

        FirebaseFirestore.getInstance()
            .collection("reports")
            .whereEqualTo("status", ReportStatus.NEW.name)
            .addSnapshotListener { snapshot, error ->

                if (error != null || snapshot == null) return@addSnapshotListener

                if (firstLoad) {
                    firstLoad = false
                    return@addSnapshotListener
                }

                snapshot.documentChanges.forEach { change ->
                    if (change.type == DocumentChange.Type.ADDED) {
                        val report = change.document.toObject(Report::class.java)

                        NotificationHelper.showNotification(
                            context,
                            "Nova prijava",
                            report.description.ifBlank {
                                "Zaprimljena je nova prijava za pregled."
                            }.take(60)
                        )
                    }
                }
            }
    }


    fun deleteReport(reportId: String) {
        viewModelScope.launch {
            reportRepository.deleteReport(reportId)
        }
    }
}
