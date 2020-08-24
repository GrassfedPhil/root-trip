package root.phil.trip_report.service

import org.springframework.stereotype.Service
import root.phil.trip_report.model.Driver
import root.phil.trip_report.model.Trip
import java.io.File
import java.time.LocalTime
import java.time.temporal.ChronoUnit.MINUTES

@Service
class CommandParser {

    fun readLinesFromFile(file: File): List<String> {
        return file.readLines()
    }

    fun createDriverMap(commandLines: List<String>): Map<String, Driver> {
        val (drivers, trips) = commandLines.partition { line -> line.startsWith("Driver") }
        val driversMap = drivers.map { Driver(it.split(" ").last()) }.associateBy { it.driverName }
        trips.filter { it.startsWith("Trip") }.map { tripString ->
            tripString.split(" ")
                    .let { (_, driverName, startTime, endTime, milesDriven) ->
                        val minutesBetween = MINUTES.between(LocalTime.parse(startTime), LocalTime.parse(endTime))
                        val mph = milesDriven.toFloat() / minutesBetween.toFloat() * 60
                        mph.takeIf { it >= 5 && it < 100}?.let {
                            Trip(LocalTime.parse(startTime), LocalTime.parse(endTime), milesDriven.toBigDecimal())
                        }?.run {
                            driversMap[driverName]?.trips?.add(this)
                        }
                    }

        }
        return driversMap
    }

}