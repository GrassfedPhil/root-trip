package root.phil.trip_report.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [CommandParser::class])
internal class CommandParserTest {
    @Autowired
    lateinit var resourceLoader: ResourceLoader

    @Autowired
    lateinit var commandParser: CommandParser

    @Test
    fun `parser will create array of commands for each line in file`() {
        val commandFile = resourceLoader.getResource("classpath:command_file").file
        val lines = commandParser.readLinesFromFile(commandFile)

        assertThat(lines).containsExactly("hello", "there", "i'm some", "commands")

    }
}