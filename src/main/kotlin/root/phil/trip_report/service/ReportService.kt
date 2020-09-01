package root.phil.trip_report.service

import org.springframework.stereotype.Service
import root.phil.trip_report.model.Driver
import kotlin.math.roundToInt

@Service
class ReportService {
    fun generateReport(driverMap: Map<String, Driver>): List<String> {

        return driverMap.map {(driverName, driver) ->
            val distance = driver.trips.sumBy { it.distance.roundToInt() }
            "$driverName: $distance miles"
        }
    }
}