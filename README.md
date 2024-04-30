**Important: Don't forget to update the [Candidate README](#candidate-readme) section**

Real-time Transaction Challenge
===============================
## Overview
Welcome to Current's take-home technical assessment for backend engineers! We appreciate you taking the time to complete this, and we're excited to see what you come up with.

Today, you will be building a small but critical component of Current's core banking enging: real-time balance calculation through [event-sourcing](https://martinfowler.com/eaaDev/EventSourcing.html).

## Schema
The [included service.yml](service.yml) is the OpenAPI 3.0 schema to a service we would like you to create and host. 

## Details
The service accepts two types of transactions:
1) Loads: Add money to a user (credit)

2) Authorizations: Conditionally remove money from a user (debit)

Every load or authorization PUT should return the updated balance following the transaction. Authorization declines should be saved, even if they do not impact balance calculation.

You may use any technologies to support the service. We do not expect you to use a persistent store (you can you in-memory object), but you can if you want. We should be able to bootstrap your project locally to test.

## Expectations
We are looking for attention in the following areas:
1) Do you accept all requests supported by the schema, in the format described?

2) Do your responses conform to the prescribed schema?

3) Does the authorizations endpoint work as documented in the schema?

4) Do you have unit and integrations test on the functionality?

# Candidate README
## Bootstrap instructions

### To run this server locally, follow these steps:

1. Ensure that you have PostgreSQL installed and running on your system.

2. Clone the repository from GitHub: 

    ```
    git clone https://github.com/codescreen/CodeScreen_3yufmg21.git

    ```

3. Navigate to the project directory: 

    ```
    cd C:\Users\<Your_Path>\CodeScreen_3yufmg21
    ```

4. Configure the database connection in the application.properties file with your PostgreSQL database details.

5. Build the project using Maven:

    ```
    mvn clean package
    ```

6. Run the Spring Boot application: 

    ```
    mvn spring-boot:run
    ```

7. The server will start running on the specified port (default is 8080).

8. Open Postman and test the APIs with different JSON payloads for load and authorization requests:

    * For ping (GET):

     ```
     http://localhost:8080/ping
     ```

    * For load (POST):

      ```
      http://localhost:8080/load
      ```

      ```
      Key           Value
      Content-Type: application/json
      ```
      - Select raw
      - example json:

         ```
          {
            "userId": "person1",
            "messageId": "msg56",
            "amount": 1000.00,
            "currency": "USD",
            "debitOrCredit": "DEBIT"
          }

         ```

    * For authorization (POST): 

      ```
      http://localhost:8080/authorization
      ```

      ```
      Key           Value
      Content-Type: application/json
      ```

      - example json
        ```
            {
              "userId": "person1",
              "messageId": "msg57",
              "amount": 500.00,
              "currency": "USD",
              "debitOrCredit": "CREDIT"
            }

        ```

9. The "Postman_Test_APIs_CreditOrDebit" directory contains images for different scenarios, such as:

    - Sending a load request with a valid JSON payload
    - Sending an authorization request for a debit transaction
    - Sending an authorization request for a credit transaction
    - Testing scenarios with insufficient balance, negative amounts, or invalid transaction types

    Refer to these images as a guide while testing the APIs using Postman to ensure that you are sending the requests correctly and with the expected JSON payloads.

10. All requests will be updated in the transaction_db which is our database.

### To run the tests:

1. Ensure that you have the necessary dependencies and plugins configured in your pom.xml file.

2. Open a terminal and navigate to the project directory.

3. Run the command: 

    ```
    mvn test
    ```

4. The unit and integration tests will be executed, and the results will be displayed in the console.

### To build and package the application:

1. Open a terminal and navigate to the project directory.

2. Run the command: 

    ```
    mvn clean package.
    ```

3. Maven will clean the previous build, compile the code, run the tests, and package the application into a JAR file.

4. The JAR file will be generated in the target directory.



## Design considerations

I decided to build the transaction processing system using Spring Boot and PostgreSQL for the following reasons:

1. The /authorization endpoint handles both DEBIT and CREDIT transactions. When a DEBIT request is received, it checks if the requested amount is less than or equal to the current balance. If the DEBIT amount is greater than the balance, the transaction is denied. DEBIT amounts cannot be negative. For a CREDIT request, the amount should be positive to get approved. Negative or zero CREDIT amounts will result in a DENIED response.

2. The /load endpoint only allows CREDIT transactions. The CREDIT amount should be positive. If the CREDIT amount is zero or negative, a bad request response is returned with the message "Amount should always be positive". If a DEBIT request is sent to the /load endpoint, it will result in a bad request response indicating that load only allows credit.

3. The system uses PostgreSQL as the database to store transaction and account information. The transaction_db table is updated with the transaction details whenever a POST request is made to either the /authorization or /load endpoint.

4. The design separates the concerns into different layers - controller, service, and repository. The controller handles the incoming requests, the service contains the business logic, and the repository interacts with the database. This separation allows for easier testing, maintainability, and scalability.

5. The use of Spring Boot simplifies the development process by providing a robust framework for building Java applications. It offers features like dependency injection, auto-configuration, and embedded server support, making it easier to set up and run the application.
The integration with PostgreSQL ensures data persistence and allows for efficient storage and retrieval of transaction and account information. The use of JPA (Java Persistence API) and Spring Data JPA simplifies database operations and reduces boilerplate code.

### Answers to the Expectations

1) Do you accept all requests supported by the schema, in the format described?

Ans. The server accepts all requests supported by the schema, in the format described. It handles both the /authorization and /load endpoints as per the specified schema.

2) Do your responses conform to the prescribed schema?

Ans. The responses from the server conform to the prescribed schema. The response objects (AuthorizationResponse and LoadResponse) are structured according to the schema and include all the required fields.

3) Does the authorizations endpoint work as documented in the schema?

Ans. The /authorization endpoint works as documented in the schema. It performs the necessary validations, updates the account balance based on the request type (DEBIT or CREDIT), and returns the appropriate response with the updated balance and response code.

4) Do you have unit and integrations test on the functionality?

Ans. Unit and integration tests are implemented to verify the functionality of the server. The tests cover various scenarios and ensure that the server behaves as expected. All 9 tests are working properly and passing.

## Bonus: Deployment considerations

For deployment, I have containerized the application using Docker. Here's how you can run the application using Docker:

1. Ensure that you have Docker installed on your system.

2. Start the Docker application and log in with your userID.

3. Navigate to the project directory:

    ```
    cd C:\Users\<Your_Path>\CodeScreen_3yufmg21
    ```

4. Build the Docker image:

    ```
    docker build -t transaction-service .
    ```

5. Run the Docker container:

    ```
    docker-compose up --build
    ```

## ASCII art

```
        +------------------------+
        |     Controller         |
        | - LoadRequest DTO      |
        | - AuthorizationRequest DTO |
        +-----------|------------+
                    | (DTOs: Data Transfer Objects containing request data)
                    v
        +-----------|------------+
        |     Service            |
        | - TransactionDTO       |
        | - BalanceDTO           |
        +-----------|------------+
                    | (DTOs: Data Transfer Objects with business logic data)
                    v
        +-----------|------------+
        |   Repository          |
        | - Entity Models       |
        +-----------|------------+
                    | (Entities: Domain-specific data models)
                    v
        +-----------|------------+
        |      Model             |
        | - User                 |
        | - Account              |
        | - Transaction          |
        +------------------------+                
```
## License

At CodeScreen, we strongly value the integrity and privacy of our assessments. As a result, this repository is under exclusive copyright, which means you **do not** have permission to share your solution to this test publicly (i.e., inside a public GitHub/GitLab repo, on Reddit, etc.). <br>

## Submitting your solution

Please push your changes to the `main branch` of this repository. You can push one or more commits. <br>

Once you are finished with the task, please click the `Submit Solution` link on <a href="https://app.codescreen.com/candidate/28ee958b-c82b-4c5a-bacd-42fb048e7a28" target="_blank">this screen</a>.