package root.phil.trip_report.service

import org.springframework.stereotype.Service
import java.io.File

@Service
class CommandParser {

    fun readLinesFromFile(file: File): List<String> {
        return file.readLines()
    }

}