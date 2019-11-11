package root.phil.trip_report.model

data class Driver(val driverName: String) {
    val trips = mutableListOf<Trip>()
}