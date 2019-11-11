package root.phil.trip_report.service

import org.springframework.stereotype.Service
import root.phil.trip_report.model.Driver
import root.phil.trip_report.model.Trip
import java.io.File
import java.time.LocalTime

@Service
class CommandParser {

    fun readLinesFromFile(file: File): List<String> {
        return file.readLines()
    }

    fun createDriverMap(commandLines: List<String>): Map<String, Driver> {
        val (drivers, trips) = commandLines.partition { line -> line.startsWith("Driver") }
        val driversMap = drivers.map { Driver(it.split(" ").last()) }.associateBy { it.driverName }
        trips.filter { it.startsWith("Trip") }.map { tripString ->
            val split = tripString.split(" ")
            val newTrip = Trip(LocalTime.parse(split[2]), LocalTime.parse(split[3]), split[4].toBigDecimal())
            driversMap[split[1]]?.trips?.add(newTrip)
        }
        return driversMap
    }

}