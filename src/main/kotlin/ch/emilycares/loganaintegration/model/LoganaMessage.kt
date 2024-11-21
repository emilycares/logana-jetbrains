package ch.emilycares.loganaintegration.model

import java.nio.file.Path

data class LoganaMessage(
    val text: String,
    val path: Path,
    val row: Int,
    val col: Int
)
