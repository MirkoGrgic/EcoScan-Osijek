package com.example.ecoscanosijek.data

import com.example.ecoscanosijek.model.Report
import com.example.ecoscanosijek.model.ReportStatus
import com.example.ecoscanosijek.model.User
import com.example.ecoscanosijek.model.UserRole

object MockData {
    val citizenUser = User(
        id = "user1",
        username = "Marko",
        email = "marko@example.com",
        points = 150,
        reportCount = 5,
        role = UserRole.CITIZEN
    )

    val workerUser = User(
        id = "user2",
        username = "Komunalac Osijek",
        email = "komunalac@osijek.hr",
        points = 0,
        reportCount = 0,
        role = UserRole.MUNICIPAL_WORKER
    )

    val users = listOf(
        citizenUser,
        User("user3", "Ivan", "ivan@example.com", 200, 7, UserRole.CITIZEN),
        User("user4", "Ana", "ana@example.com", 100, 3, UserRole.CITIZEN),
        workerUser
    )

    val reports = mutableListOf(
        Report(
            id = "1",
            userId = "user1",
            imageUrl = "",
            description = "Ilegalni deponij u Tvrđi",
            latitude = 45.560,
            longitude = 18.694,
            status = ReportStatus.NEW,
            createdAt = System.currentTimeMillis() - 86400000
        ),
        Report(
            id = "2",
            userId = "user1",
            imageUrl = "",
            description = "Prepuna kanta za smeće kod konkatedrale",
            latitude = 45.560,
            longitude = 18.676,
            status = ReportStatus.IN_PROGRESS,
            createdAt = System.currentTimeMillis() - 43200000
        ),
        Report(
            id = "3",
            userId = "user3",
            imageUrl = "",
            description = "Razbijeno staklo na igralištu",
            latitude = 45.550,
            longitude = 18.680,
            status = ReportStatus.RESOLVED,
            createdAt = System.currentTimeMillis() - 172800000
        )
    )
}
