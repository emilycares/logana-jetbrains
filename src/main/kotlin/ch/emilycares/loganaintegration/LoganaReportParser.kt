package ch.emilycares.loganaintegration

import ch.emilycares.loganaintegration.model.LoganaMessage
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

private fun getReportFile(project: Path): List<String> {
    val path = Path.of("$project/.logana-report")

    if (!Files.exists(path)) {
        return listOf()
    }

    try {
        return Files.readAllLines(path)
    } catch (e: IOException) {
        println("Unable to read the file: $path error: $e")
        return listOf()
    }
}

fun parse(lines: List<String>): List<LoganaMessage> {
    return lines.map { l ->
        var line = l;
        if (line.isEmpty()) {
            return@map null
        }

        var drive = ""
        if (line[1] == ':') {
            drive = line.substring(0, 2)
            line = line.substring(2)
        }

        val message = line.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val location = message[0]
        val text = message[1]
        val split_location = location.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val path = split_location[0]
        val row = split_location[1].toInt() - 1
        val col = split_location[2].toInt() - 1

        val fpath = Path.of(drive + path);

        return@map LoganaMessage(text, fpath, row, col)
    }.filterNotNull()
}

fun parseReportFile(project: Path): List<LoganaMessage> {
    val file = getReportFile(project)

    return parse(file)
}
