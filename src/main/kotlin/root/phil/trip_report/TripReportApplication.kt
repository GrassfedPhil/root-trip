package root.phil.trip_report

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import root.phil.trip_report.service.CommandParser
import root.phil.trip_report.service.ReportService
import root.phil.trip_report.service.TripService
import java.io.File

@SpringBootApplication
class TripReportApplication


fun main(args: Array<String>) {
    runApplication<TripReportApplication>(*args)
}

@Component
class Runner: CommandLineRunner{
    @Autowired
    lateinit var commandParser: CommandParser

    @Autowired
    lateinit var tripService: TripService

    @Autowired
    lateinit var reportService: ReportService

    override fun run(vararg args: String?) {
        println("Enter the path of the raw data file")
        val path = readLine()
        val file = File(path)
        val lines = commandParser.readLinesFromFile(file)
        val driverMap = tripService.createDriverMap(lines)
        val reportLines = reportService.generateReport(driverMap)
        reportLines.forEach{
            println(it)
        }
    }

}

