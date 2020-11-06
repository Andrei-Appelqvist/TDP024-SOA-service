# TDP024-SOA-service

SOA service for the course TDP024 - Enterprise Systems.
This is simple implementation of a bank system (without any security in place). The multi-tier system is written in Java and the project is made in Maven with Spring Boot.
The system has a JPA driven datalayer with a logic and rest layer. It is dependent on two Python API:s for getting banks and persons(they use cloud storage but this 
was terminated after the project was done).

The maven project is built according to REST principles and available through http. Logging is done by all the parts independently through Kafka to two topics and the code is
tested with Jaccoco. The basic project directory structure was given to us by the course leader.

This project was made by Andrei Moga(Me) and Alexander.

Below you can see the scheme for the system.

https://www.ida.liu.se/~TDP024/labs/archi.png
