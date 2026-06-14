package com.example.ecoscanosijek.repository

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.ecoscanosijek.model.Report
import com.example.ecoscanosijek.model.ReportStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FieldValue
import java.util.UUID

class ReportRepository(private val context: Context) {
    private val db = FirebaseFirestore.getInstance()

    fun getAllReports(): Flow<List<Report>> = callbackFlow {
        val subscription = db.collection("reports")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val reports = snapshot.toObjects(Report::class.java)
                    trySend(reports)
                }
            }
        awaitClose { subscription.remove() }
    }

    fun getReportsForUser(userId: String): Flow<List<Report>> = callbackFlow {
        val subscription = db.collection("reports")
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val reports = snapshot.toObjects(Report::class.java)
                    trySend(reports)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun addReport(report: Report) {
        val reportRef = db.collection("reports").document(report.id)
        val userRef = db.collection("users").document(report.userId)

        db.runBatch { batch ->
            batch.set(reportRef, report)

            if (report.userId.isNotBlank()) {
                batch.update(
                    userRef,
                    "reportCount",
                    FieldValue.increment(1)
                )
            }
        }.await()
    }
    suspend fun deleteReport(reportId: String) {
        db.collection("reports").document(reportId).delete().await()
    }

    suspend fun getReportById(id: String): Report? {
        val document = db.collection("reports").document(id).get().await()
        return document.toObject(Report::class.java)
    }

    suspend fun updateReportStatus(
        reportId: String,
        status: ReportStatus,
        workerId: String? = null
    ) {
        val reportRef = db.collection("reports").document(reportId)

        db.runTransaction { transaction ->
            val reportSnapshot = transaction.get(reportRef)
            val report = reportSnapshot.toObject(Report::class.java)

            if (report != null) {
                val citizenRef = db.collection("users").document(report.userId)
                val citizenSnapshot =
                    if (report.userId.isNotBlank()) transaction.get(citizenRef) else null

                val isBecomingResolved =
                    status == ReportStatus.RESOLVED &&
                            report.status != ReportStatus.RESOLVED

                val shouldAwardCitizen =
                    isBecomingResolved &&
                            !report.pointsAwarded &&
                            report.userId.isNotBlank() &&
                            citizenSnapshot?.exists() == true

                transaction.update(reportRef, "status", status)

                if (shouldAwardCitizen) {
                    transaction.update(citizenRef, "points", FieldValue.increment(10))
                    transaction.update(citizenRef, "resolvedCount", FieldValue.increment(1))
                    transaction.update(reportRef, "pointsAwarded", true)
                }

                if (
                    isBecomingResolved &&
                    !workerId.isNullOrBlank() &&
                    workerId != report.userId
                ) {
                    val workerRef = db.collection("users").document(workerId)
                    transaction.update(workerRef, "resolvedCount", FieldValue.increment(1))
                }
            }
        }.await()
    }

    fun uploadImage(imageUri: Uri, onResult: (String?) -> Unit) {
        val requestId = MediaManager.get().upload(imageUri)
            .unsigned("ecoscan_unsigned") // As per user's upload preset
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String
                    onResult(url)
                }
                override fun onError(requestId: String, error: ErrorInfo) {
                    onResult(null)
                }
                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            }).dispatch()
    }
}
