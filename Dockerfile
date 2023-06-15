FROM openjdk:17
LABEL maintainer="javaguides.net"
ADD target/book-table-0.0.1-SNAPSHOT.jar book-table.jar
ENTRYPOINT ["java", "-jar", "/book-table.jar"]