package root.phil.trip_report.service

import org.springframework.stereotype.Service
import root.phil.trip_report.model.Driver
import root.phil.trip_report.model.Trip
import java.time.LocalTime
import java.time.temporal.ChronoUnit.MINUTES

@Service
class TripService {

    fun createDriverMap(commandLines: List<String>): Map<String, Driver> {
        val (drivers, trips) = commandLines.partition { line -> line.startsWith("Driver") }
        val driversMap = drivers.map { Driver(it.split(" ").last()) }.associateBy { it.driverName }
        associateTripsToDrivers(trips, driversMap)
        return driversMap
    }

    private fun associateTripsToDrivers(trips: List<String>, driversMap: Map<String, Driver>) {
        trips.filter { it.startsWith("Trip") }.map { tripString ->
            tripString.split(" ")
                    .let { (_, driverName, startTime, endTime, milesDriven) ->
                        val minutesBetween = MINUTES.between(LocalTime.parse(startTime), LocalTime.parse(endTime))
                        val mph = milesDriven.toDouble() / minutesBetween.toDouble() * SIXTY_MINUTES
                        mph.takeIf { it >= LOWER_MPH_LIMIT && it < UPPER_MPH_LIMIT }?.let {
                            Trip(LocalTime.parse(startTime), LocalTime.parse(endTime), milesDriven.toDouble(), mph)
                        }?.run {
                            driversMap[driverName]?.trips?.add(this)
                        }
                    }
        }
    }

    companion object {
        const val LOWER_MPH_LIMIT = 5
        const val UPPER_MPH_LIMIT = 100
        const val SIXTY_MINUTES = 60
    }

}