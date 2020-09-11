This project is written in Kotlin using Spring Boot as an underlying framework. I've chosen these tools because of my familiarity with them (used them most of my career), and because I've found Spring Boot to be an easy framework to add features to a project quickly.

To build the project, run
```
./gradlew build
```

This will run the tests in the project and produce a runnable jar. You can then run the jar file by executing the following command

```
java -jar build/libs/trip_report-0.0.1-SNAPSHOT.jar 
 ```
You'll then be prompted to provide the path of the data file you wish to process. Enter the full path, and hit return. The results will be printed to the console.

### **The Why and How**

I am a firm believer in test driving code, and so that's where I always start. The first thing to do for this project was to make sure that it could ingest a file. So I wanted to make sure that I had a parser (the CommandParser) that could do just that. Then I build from there. 

Storing the data in an easily accessible format was the thing that was probably most complex for me. Without wanting to introduce an entire database layer, I felt the easiest thing to do would be to store everything in a map structure, using the drivers name as the key, Driver object as the value. This enabled me to easily check if a driver had previously been created when parsing and adding trips, and simply discard a trip line if no driver was found.

I tried to optimize for fault tolerance, and allowing bad data to flow through the system without causing errors. Normally, in an enterprise project, I would make sure to record those errors, but I felt that was beyond the scope of what was being asked for here. Specifically things that I'm aware of, but have ignored for the time being include begin times being after end times, driver lines with no names, trip lines with malformed or missing data, and any other kind of parsing error.

Once all the business logic has been performed, a map of DriverName to Driver Objects is returned. Each of these Driver objects contains a list of Trip objects, which in turn, contain information about each parsed Trip line for that Driver. I wanted to make sure that each individual data line was preserved in its original form in case any future processing needed to be done.

Finally, that map is passed off to the ReportService which just parses through the data for the drivers and returns a list of strings for the main function to print out.
I wanted to make sure that the individual steps of this project (parse, interpret/ingest, and report) were all contained in their own distinct services, so that there was a clear separation of concerns.

This was a fun project to work on, and I hope I get a chance to discuss it further with you!
