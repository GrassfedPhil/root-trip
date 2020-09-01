package root.phil.trip_report.model

import java.time.LocalTime

data class Trip(val startTime: LocalTime, val endTime: LocalTime, val distance: Double, val mph: Double) {
}