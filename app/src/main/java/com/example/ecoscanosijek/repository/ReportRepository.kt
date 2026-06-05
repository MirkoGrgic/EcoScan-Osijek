package com.example.ecoscanosijek.repository

import com.example.ecoscanosijek.data.MockData
import com.example.ecoscanosijek.model.Report
import com.example.ecoscanosijek.model.ReportStatus

class ReportRepository {
    fun getAllReports(): List<Report> = MockData.reports

    fun getReportsForUser(userId: String): List<Report> =
        MockData.reports.filter { it.userId == userId }

    fun addReport(report: Report) {
        MockData.reports.add(report)
    }

    fun getReportById(id: String): Report? =
        MockData.reports.find { it.id == id }

    fun updateReportStatus(reportId: String, status: ReportStatus) {
        val index = MockData.reports.indexOfFirst { it.id == reportId }
        if (index != -1) {
            val oldReport = MockData.reports[index]
            MockData.reports[index] = oldReport.copy(status = status)
        }
    }
}
