<h1>Take-Home Assignment: Java Server</h1>

<h2>Overview</h2>

For this assignment I had to write a server ("Application") in Java that opens a socket and restricts input to at most 5 concurrent clients.
Clients are then able to connect to the application and write any number of 9 digit numbers, and then close the connection.  The application is then able write a de-duplicated list of the numbers to a log file.

<h2>Approach</h2>

A `Server` class is created that utilizes the `java.net.ServerSocket` class and the `java.util.concurrent.BlockingQueue` interface.  The ServerSocket is used, so that the server can connect to a specific port.  In this case, the server will be connecting to port 4000.
The BlockingQueue is used because there will be concurrent clients using the application.  The BlockingQueue is thread safe
and appropriate for use cases involving concurrency.  The instance of the BlockingQueue will be passed as argument to another class that was 
created for this project: `InputQueue`.

The Server has a `ClientHandler` class that runs until a client chooses to disconnect from the server by entering "terminate" as input.  When this input is entered by the client, the server will shut down.  The ClientHandler takes valid inputs from the clients and sends that information to 
the BlockingQueue.

This is the criteria for valid input:

*  Input is nine characters long

*  Input contains only decimal digits

*  Input is not negative

There is also an `InputQueue` class.  This class keeps track of the inputs from the different clients and counts any duplicate values.  A boolean array was chosen for this purpose.  Some options that were considered for duplicate tracking were a Set, boolean array, and a BitSet.  

Sets are slow in comparison to boolean arrays and BitSets, so that option was dismissed early.  [This article](https://www.baeldung.com/java-boolean-array-bitset-performance) shows that in single-bit write-heavy scenarios, the boolean array exhibits a superior throughput almost all the time except for a very large number of bits.  So the 
boolean array was chosen.

The InputQueue class also utilizes `java.util.Timer` and `java.util.TimerTask` to print a report to standard output every ten seconds.  

That report includes the following information:

*  The difference since the last report of the count of new unique numbers that have been received.

*  The difference since the last report of the count of new duplicate numbers that have been received.

*  The total number of unique numbers received for this run of the Application.

*  Sample report: Received 50 unique numbers, 2 duplicates. Unique total: 567231

Finally there is a `Client` class.  This class was created in order to perform tests on the Server.

<h2>Tests</h2>

*  Only numbers that are nine decimal digits in length will be added to the InputQueue

*  Server shuts down when a client enters "terminate" as input

*  Negative numbers will not be added as input to the InputQueue

*  Input that only contains decimal digits will be added to the InputQueue

*  Duplicate values will not be added to the InputQueue, but a count of duplicate values will be kept and updated

*  Five clients will be able to use the server concurrently and be able to submit 2M numbers

All tests were passed.  Although it was specified that the server should be able to handle more than 2M numbers per 10-second reporting period, the laptop used for this testing is about ten years old and took about 40 seconds.  If tested on a more modern laptop, it might have been able to meet the 
specified requirements.

<h2>Fulfilled Requirements</h2>

:heavy_check_mark: The Application must accept input from at most 5 concurrent clients on TCP/IP port 4000.

:heavy_check_mark: Input lines presented to the Application via its socket must either be composed of exactly nine decimal digits (e.g.: 314159265 or 007007009) immediately followed by a server-native newline sequence; or a termination sequence.

:heavy_check_mark: Numbers presented to the Application must include leading zeros as necessary to ensure they are each 9 decimal digits.

:heavy_check_mark: The log file, to be named "numbers.log”, must be created anew and/or cleared when the Application starts.

:heavy_check_mark: Only numbers may be written to the log file. Each number must be followed by a server-native newline sequence.

:heavy_check_mark: No duplicate numbers may be written to the log file.

:heavy_check_mark: Any data that does not conform to a valid line of input should be discarded and the client connection terminated immediately and without comment.

:heavy_check_mark: Every 10 seconds, the Application must print a report to standard output.

:heavy_check_mark: If any connected client writes a single line with only the word "terminate" followed by a server-native newline sequence, the Application must disconnect all clients and perform a clean shutdown as quickly as possible.

:heavy_check_mark: Clearly state all of the assumptions you made in completing the Application along with any instructions on how to set up and run it in a README file.

<h2>To Run</h2>
Running is quite easy.  Just clone the repo and run the main method in the Main class.

To test, just run the desired tests.  Be sure that the Server is running while testing.

<h2>References</h2>

*  [A Guide to Java Sockets](https://www.baeldung.com/a-guide-to-java-sockets)

*  [Socket Programming in Java](https://www.infoworld.com/article/2853780/socket-programming-for-scalable-systems.html)

*  [Writing the Server Side of a Socket](https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html)

*  [Performance Comparison of boolean Array vs BitSet](https://www.baeldung.com/java-boolean-array-bitset-performance)

<h2>Complete Challenge Description</h2>

Write a server (“Application”) in Java that opens a socket and restricts input to at most 5 concurrent clients. Clients will connect to the Application and write any number of 9 digit numbers, and then close the connection. The Application must write a de-duplicated list of these numbers to a log file in no particular order.

<h3>Primary Considerations</h3>

*  The Application should work correctly as defined below in Requirements.

*  The overall structure of the Application should be simple.

*  The code of the Application should be descriptive and easy to read, and the build method and runtime parameters must be well-described and work.

*  The design should be resilient with regard to data loss.

*  The Application should be optimized for maximum throughput, weighed along with the other Primary Considerations and Requirements below.

<h3>Requirements</h3>

*  The Application must accept input from at most 5 concurrent clients on TCP/IP port 4000.

*  Input lines presented to the Application via its socket must either be composed of exactly nine decimal digits (e.g.: 314159265 or 007007009) immediately followed by a server-native newline sequence; or a termination sequence.

*  Numbers presented to the Application must include leading zeros as necessary to ensure they are each 9 decimal digits.

*  The log file, to be named "numbers.log”, must be created anew and/or cleared when the Application starts.

*  Only numbers may be written to the log file. Each number must be followed by a server-native newline sequence.

*  No duplicate numbers may be written to the log file.

*  Any data that does not conform to a valid line of input should be discarded and the client connection terminated immediately and without comment.

*  Every 10 seconds, the Application must print a report to standard output:

    -  The difference since the last report of the count of new unique numbers that have been received.
  
    -  The difference since the last report of the count of new duplicate numbers that have been received.
  
    -  The total number of unique numbers received for this run of the Application.
  
    -  Example text for report: Received 50 unique numbers, 2 duplicates. Unique total: 567231
  
*  If any connected client writes a single line with only the word "terminate" followed by a server-native newline sequence, the Application must disconnect all clients and perform a clean shutdown as quickly as possible.

*  Clearly state all of the assumptions you made in completing the Application along with any instructions on how to set up and run it in a README file.

<h3>Notes</h3>

*  You may write tests at your own discretion. Tests are useful to ensure your Application passes Primary Consideration A.

*  You may use common libraries in your project such as Apache Commons and Google Guava, particularly if their use helps improve Application simplicity and readability. However the use of large frameworks, such as Akka, is prohibited.

*  Your Application may not for any part of its operation use or require the use of external systems, for example Apache Kafka or Redis.

*  At your discretion, leading zeroes present in the input may be stripped—or not used—when writing output to the log or console.

*  Robust implementations of the Application typically handle more than 2M numbers per 10-second reporting period on a modern MacBook Pro laptop (e.g.: 16 GiB of RAM and a 2.5 GHz Intel i7 processor).

We'll initially test your solution using Java (11 LTS) from https://adoptopenjdk.net/. If you have other requirements, please specify.
