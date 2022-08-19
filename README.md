# Book Store Application

## Description

This is a Book store application with REST API endpoints exposed for performing operations like add a book to store, fetch book(s), update or delete any book from store. User can also perform checkout operation for the book(s).

## Tech Stack

- Java 11
- Spring Boot 2.7.2
- Maven
- H2 database
- Mockito
- Swagger

## Considerations

1. H2 In-memory database can be accessed via the url - http://localhost:8080/h2-ui with below credentials.

   JDBC URL - jdbc:h2:mem:bookdb

   Username - sa
2. Swagger console is available via url - http://localhost:8080/swagger-ui/index.html
3. For Checkout Operation, 
    a. User can specify the quantity along with bookid to be checked out.
	b. If a promotion code is provided in checkout request, discounts will be applied as per the classification provided below.
	   For Comic books - No discounts applicable
	   For Science books - 3 %
	   For Fiction books - 10 %
	   For Maths books - 15 %
	   For all other classifications - No discounts applicable
    c. If there is no promotion code given in checkout request, no discounts will be applied.
	
## Running the Application using Maven

1. To build the application using maven, run the command,
   **mvn clean install**
   
2. Navigate to the target folder under project root directory and execute below command to start the application
   **java -jar bookstore-0.0.1-SNAPSHOT.jar**

## Running the Application using Docker

1. In project root directory, execute the below command, which will create a docker image for the application
   **mvn spring-boot:build-image**
   
2. Once the above command is successful, execute the below command to spawn a container from the created image
   **docker run -p 8080:8080 bookstore:0.0.1-SNAPSHOT**
 
## Endpoints

1. Get a book detail by its book id - http://localhost:8080/v1​/book​/{id}
2. Add a book to the book store - http://localhost:8080​/v1​/book​/add
3. Add all the books to the book store - http://localhost:8080/v1​/book​/addAll
4. Get all the books in store - http://localhost:8080​/v1​/book​/all
5. Checkout books from store - http://localhost:8080​/v1​/book​/checkout
6. ​Delete a book by using book id - http://localhost:8080/v1​/book​/delete​/{id}
7. Update book information using book id - ​http://localhost:8080/v1​/book​/update​/{id}
