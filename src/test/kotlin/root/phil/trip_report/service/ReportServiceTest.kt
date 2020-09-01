package root.phil.trip_report.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import root.phil.trip_report.model.Driver
import root.phil.trip_report.model.Trip
import java.time.LocalTime

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ReportService::class])
internal class ReportServiceTest {
    @Autowired
    lateinit var reportService: ReportService

    @Test
    fun `generateReport will consolidate trips by driver`(){
        val daveTrip1 = Trip(LocalTime.now().minusHours(1), LocalTime.now(), 100.0, 50.0)
        val daveTrip2 = Trip(LocalTime.now().minusHours(1), LocalTime.now(), 50.0, 50.0)
        val chrisTrip = Trip(LocalTime.now().minusHours(1), LocalTime.now(), 50.0, 50.0)
        val dave = Driver("Dave")
        dave.trips.addAll(listOf(daveTrip1, daveTrip2))
        val chris = Driver("Chris")
        chris.trips.add(chrisTrip)

        val trips = mapOf("Dave" to dave, "Chris" to chris)

        val report = reportService.generateReport(trips)
        assertThat(report).containsExactly("Dave: 150 miles @ 75 mph", "Chris: 50 miles @ 50 mph")
    }

    @Test
    fun `generateReport will not attempt to calculate mph when distance traveled is 0`(){
        val daveTrip1 = Trip(LocalTime.now().minusHours(1), LocalTime.now(), 0.0, 0.0)
        val daveTrip2 = Trip(LocalTime.now().minusHours(1), LocalTime.now(), 0.0, 0.0)
        val dave = Driver("Dave")
        dave.trips.addAll(listOf(daveTrip1, daveTrip2))
        val trips = mapOf("Dave" to dave)

        val report = reportService.generateReport(trips)
        assertThat(report).containsExactly("Dave: 0 miles")
    }

    @Test
    fun `generateReport will sort by distance travelled descending`() {
        val daveTrip1 = Trip(LocalTime.now().minusHours(1), LocalTime.now(), 100.0, 50.0)
        val daveTrip2 = Trip(LocalTime.now().minusHours(1), LocalTime.now(), 50.0, 50.0)
        val chrisTrip = Trip(LocalTime.now().minusHours(1), LocalTime.now(), 250.0, 250.0)
        val dave = Driver("Dave")
        dave.trips.addAll(listOf(daveTrip1, daveTrip2))
        val chris = Driver("Chris")
        chris.trips.add(chrisTrip)

        val trips = mapOf("Dave" to dave, "Chris" to chris)

        val report = reportService.generateReport(trips)
        assertThat(report).containsExactly("Chris: 250 miles @ 250 mph", "Dave: 150 miles @ 75 mph")

    }
}