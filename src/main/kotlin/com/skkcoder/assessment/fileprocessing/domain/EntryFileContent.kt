package com.skkcoder.assessment.fileprocessing.domain

import java.util.UUID

data class EntryFileContent(
    val entryId: UUID,
    val id: String,
    val name: String,
    val likes: String,
    val transport: String,
    val avgSpeed: Double,
    val topSpeed: Double
)
