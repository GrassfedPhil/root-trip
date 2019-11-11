package root.phil.trip_report.service

import org.springframework.stereotype.Service
import root.phil.trip_report.model.Driver
import java.io.File

@Service
class CommandParser {

    fun readLinesFromFile(file: File): List<String> {
        return file.readLines()
    }

    fun createDriverMap(file: File): Map<String, Driver> {
        val commandLines = readLinesFromFile(file)
        val driverMap = commandLines.filter { line -> line.startsWith("Driver") }
                .map { Driver(it.split(" ").last()) }.associateBy { it.driverName }
        return driverMap
    }

}