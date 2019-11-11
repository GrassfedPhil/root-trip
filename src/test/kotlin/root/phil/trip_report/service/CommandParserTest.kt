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
        val commandFile = resourceLoader.getResource("classpath:driver_file").file
        val driverMap = commandParser.createDriverMap(commandFile)
        assertThat(driverMap).containsOnly(
                entry("Dave", Driver("Dave")),
                entry("Chris", Driver("Chris"))
        )
    }

    @Test
    fun `parser will ignore unknown commands and not explode`(){
        val commandFile = resourceLoader.getResource("classpath:command_file").file
        val driverMap = commandParser.createDriverMap(commandFile)

        assertThat(driverMap).isEmpty()
    }
}