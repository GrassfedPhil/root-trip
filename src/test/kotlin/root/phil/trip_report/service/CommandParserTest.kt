package root.phil.trip_report.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.entry
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.test.context.junit.jupiter.SpringExtension
import root.phil.trip_report.model.Driver
import root.phil.trip_report.model.Trip
import java.time.LocalTime

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class CommandParserTest{

    @Autowired
    lateinit var resourceLoader: ResourceLoader

    @Autowired
    lateinit var commandParser: CommandParser

    @Test
    fun `parser will create array of commands for each line in file`(){
        val commandFile = resourceLoader.getResource("classpath:command_file").file
        val lines = commandParser.readLinesFromFile(commandFile)

        assertThat(lines).containsExactly("hello", "there", "i'm some", "commands")

    }

    @Test
    fun `parser will create driver map from file`(){
        val driverMap = commandParser.createDriverMap(listOf("Driver Dave", "Driver Chris"))
        assertThat(driverMap).containsOnly(
                entry("Dave", Driver("Dave")),
                entry("Chris", Driver("Chris"))
        )
    }

    @Test
    fun `parser will ignore unknown commands and not explode`(){
        val driverMap = commandParser.createDriverMap(listOf("hello", "there"))

        assertThat(driverMap).isEmpty()
    }

    @Test
    fun `parser will add add trips to recognized drivers`() {
        val driverMap = commandParser.createDriverMap(listOf(
                "Driver Dave",
                "Driver Chris",
                "Trip Dave 11:11 12:34 55.6",
                "Trip Dave 13:00 16:34 100.6"
        ))

        val dave = driverMap["Dave"]
        val chris = driverMap["Chris"]
        assertThat(dave?.driverName).isEqualTo("Dave")
        assertThat(dave?.trips).usingFieldByFieldElementComparator().containsOnly(
                Trip(LocalTime.parse("11:11"), LocalTime.parse("12:34"), 55.6.toBigDecimal()),
                Trip(LocalTime.parse("13:00"), LocalTime.parse("16:34"), 100.6.toBigDecimal())
        )
        assertThat(chris?.driverName).isEqualTo("Chris")
        assertThat(chris?.trips).isEmpty()

    }
}