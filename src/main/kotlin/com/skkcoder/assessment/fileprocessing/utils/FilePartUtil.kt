package com.skkcoder.assessment.fileprocessing.utils

import java.io.InputStream
import java.io.SequenceInputStream
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withContext
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.Part


suspend fun FilePart.toByteArray(): ByteArray = withContext(Dispatchers.IO) {
    this@toByteArray.asInputStream().readAllBytes()
}

suspend fun Part.asInputStream(): InputStream =
    SequenceInputStream(
        Collections.enumeration(content().asFlow().map { it.asInputStream() }.toList())
    )