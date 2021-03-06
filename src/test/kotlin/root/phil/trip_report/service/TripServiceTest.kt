package root.phil.trip_report.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.entry
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import root.phil.trip_report.model.Driver
import root.phil.trip_report.model.Trip
import java.time.LocalTime
import java.util.function.BiPredicate

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TripService::class])
internal class TripServiceTest {

    @Autowired
    lateinit var tripService: TripService

    @Test
    fun `service will create driver map`() {
        val driverMap = tripService.createDriverMap(listOf("Driver Dave", "Driver Chris"))
        assertThat(driverMap).containsOnly(
                entry("Dave", Driver("Dave")),
                entry("Chris", Driver("Chris"))
        )
    }

    @Test
    fun `service will ignore unknown commands and not explode`() {
        val driverMap = tripService.createDriverMap(listOf("hello", "there"))

        assertThat(driverMap).isEmpty()
    }

    final var closeEnough = BiPredicate { d1: Double, d2: Double -> Math.abs(d1 - d2) <= .05 }
    var configuration: RecursiveComparisonConfiguration = RecursiveComparisonConfiguration.builder()
            .withEqualsForType(closeEnough, Double::class.javaObjectType)
            .build()

    @Test
    fun `service will add add trips to recognized drivers`() {
        val driverMap = tripService.createDriverMap(listOf(
                "Driver Dave",
                "Driver Chris",
                "Trip Dave 11:11 12:34 55.6",
                "Trip Dave 13:00 16:34 100.6"
        ))

        val dave = driverMap["Dave"]
        val chris = driverMap["Chris"]
        assertThat(dave?.driverName).isEqualTo("Dave")
        assertThat(dave?.trips).usingRecursiveFieldByFieldElementComparator(configuration)
                .containsOnly(
                        Trip(LocalTime.parse("11:11"), LocalTime.parse("12:34"), 55.6, 40.19),
                        Trip(LocalTime.parse("13:00"), LocalTime.parse("16:34"), 100.6, 28.20)
                )
        assertThat(chris?.driverName).isEqualTo("Chris")
        assertThat(chris?.trips).isEmpty()

    }

    @Test
    fun `service will not add trips that have an avg speed of less than 5mph`() {
        val driverMap = tripService.createDriverMap(listOf(
                "Driver Dave",
                "Trip Dave 11:11 12:34 55.6",
                "Trip Dave 13:00 14:00 4.9",
                "Trip Dave 14:00 15:00 5"
        ))

        assertThat(driverMap["Dave"]?.trips).usingRecursiveFieldByFieldElementComparator(configuration)
                .containsOnly(
                        Trip(LocalTime.parse("11:11"), LocalTime.parse("12:34"), 55.6, 40.19),
                        Trip(LocalTime.parse("14:00"), LocalTime.parse("15:00"), 5.0, 5.0)
                )
    }

    @Test
    fun `service will not add trips that have an avg speed of greater than or equal 60mph`() {
        val driverMap = tripService.createDriverMap(listOf(
                "Driver Dave",
                "Trip Dave 11:11 12:34 55.6",
                "Trip Dave 14:00 15:01 100",
                "Trip Dave 15:00 15:59 100"
        ))

        assertThat(driverMap["Dave"]?.trips).usingRecursiveFieldByFieldElementComparator(configuration)
                .containsOnly(
                        Trip(LocalTime.parse("11:11"), LocalTime.parse("12:34"), 55.6, 40.19),
                        Trip(LocalTime.parse("14:00"), LocalTime.parse("15:01"), 100.0, 98.36)
                )
    }
}