package root.phil.trip_report.service

import org.springframework.stereotype.Service
import root.phil.trip_report.model.Driver
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Service
class ReportService {
    fun generateReport(driverMap: Map<String, Driver>): List<String> {
        return driverMap.values.sortedByDescending { it.trips.sumBy { trip -> trip.distance.roundToInt() } }
                .map { driver ->
                    val distance = driver.trips.sumBy { it.distance.roundToInt() }
                    val totalTime = driver.trips.sumBy { ChronoUnit.MINUTES.between(it.startTime, it.endTime).toInt() }
                    val mph = distance.toDouble() / totalTime.toDouble() * SIXTY_MINUTES
                    "${driver.driverName}: $distance miles${if (mph > 0) " @ ${mph.roundToInt()} mph" else ""}"
                }
    }

    companion object {
        const val SIXTY_MINUTES = 60
    }
}